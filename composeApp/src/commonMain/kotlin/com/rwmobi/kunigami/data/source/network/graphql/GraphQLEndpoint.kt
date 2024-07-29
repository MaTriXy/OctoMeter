/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.graphql

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.exception.ApolloGraphQLException
import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.graphql.EnergyProductsQuery
import com.rwmobi.kunigami.graphql.ObtainKrakenTokenMutation
import com.rwmobi.kunigami.graphql.PropertiesQuery
import com.rwmobi.kunigami.graphql.SingleEnergyProductQuery
import com.rwmobi.kunigami.graphql.type.ObtainJSONWebTokenInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class GraphQLEndpoint(
    private val apolloClient: ApolloClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val defaultPageSize = 100

    /***
     * Authorization Token not required.
     */
    suspend fun getEnergyProducts(
        postcode: String,
        afterCursor: String? = null,
        pageSize: Int = defaultPageSize,
    ): EnergyProductsQuery.Data {
        return withContext(dispatcher) {
            runQuery(
                query = EnergyProductsQuery(
                    postcode = postcode,
                    pageSize = pageSize,
                    afterCursor = afterCursor?.let { Optional.present(it) } ?: Optional.absent(),
                ),
                requireAuthentication = false,
            )
        }
    }

    /***
     * Authorization Token not required.
     */
    suspend fun getSingleEnergyProduct(
        productCode: String,
        postcode: String,
        afterCursor: String? = null,
        pageSize: Int = defaultPageSize,
    ): SingleEnergyProductQuery.Data {
        return withContext(dispatcher) {
            runQuery(
                query = SingleEnergyProductQuery(
                    productCode = productCode,
                    postcode = postcode,
                    pageSize = pageSize,
                    afterCursor = afterCursor?.let { Optional.present(it) } ?: Optional.absent(),
                ),
                requireAuthentication = false,
            )
        }
    }

    /***
     * The GraphQL Account query can't return everything we need. Underlying we call PropertiesQuery in this implementation.
     * Authorization Token required.
     */
    suspend fun getAccount(
        accountNumber: String,
    ): PropertiesQuery.Data {
        return withContext(dispatcher) {
            runQuery(
                query = PropertiesQuery(
                    accountNumber = accountNumber,
                ),
                requireAuthentication = true,
            )
        }
    }

    //region Token Management
    suspend fun getAuthorizationToken(apiKey: String): Result<Token> {
        val input = ObtainJSONWebTokenInput(APIKey = Optional.present(apiKey))
        return obtainKrakenToken(input = input)
    }

    suspend fun refreshAuthorizationToken(refreshToken: String): Result<Token> {
        val input = ObtainJSONWebTokenInput(refreshToken = Optional.present(refreshToken))
        return obtainKrakenToken(input = input)
    }

    private suspend fun <D : Query.Data> runQuery(query: Query<D>, requireAuthentication: Boolean): D {
        // AuthorisationInterceptor will manage token insertion
        val response = if (requireAuthentication) {
            apolloClient
                .query(query)
                .addHttpHeader("Requires-Authorization", "true")
                .execute()
        } else {
            apolloClient.query(query).execute()
        }

        response.exception?.let { exception ->
            exception.printStackTrace()
            throw exception
        }

        response.errors?.let {
            // Handle GraphQL errors
            throw ApolloGraphQLException(it)
        }

        response.data?.let {
            return it
        } ?: run {
            throw IllegalStateException("Unexpected GraphQL Error")
        }
    }

    private suspend fun obtainKrakenToken(input: ObtainJSONWebTokenInput): Result<Token> {
        return withContext(dispatcher) {
            runCatching {
                val response = apolloClient.mutation(ObtainKrakenTokenMutation(input)).execute()

                response.data?.obtainKrakenToken?.let {
                    // Handle (potentially partial) data
                    Token.fromObtainKrakenToken(obtainKrakenToken = it)
                } ?: run {
                    // Something wrong happened
                    response.exception?.let {
                        // Handle fetch errors
                        it.printStackTrace()
                        throw it
                    } ?: run {
                        // Handle GraphQL errors in response.errors
                        val concatenatedMessages = response.errors?.joinToString(separator = ",") { it.message }
                        throw IllegalStateException("Unhandled response errors: $concatenatedMessages")
                    }
                }
            }
        }.except<CancellationException, _>()
    }
    //endregion
}
