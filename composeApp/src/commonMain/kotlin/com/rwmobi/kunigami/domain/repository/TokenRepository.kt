/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.repository

import com.rwmobi.kunigami.data.source.network.dto.auth.Token

interface TokenRepository {
    suspend fun getToken(forceRefresh: Boolean): Token?
}
