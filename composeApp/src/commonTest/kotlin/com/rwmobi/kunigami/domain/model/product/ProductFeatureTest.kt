/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.domain.model.product

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("TooManyFunctions")
class ProductFeatureTest {

    @Test
    fun `fromApiValue should return VARIABLE for variable`() {
        val feature = ProductFeature.fromApiValue("variable")
        assertEquals(ProductFeature.VARIABLE, feature)
    }

    @Test
    fun `fromApiValue should return GREEN for green`() {
        val feature = ProductFeature.fromApiValue("green")
        assertEquals(ProductFeature.GREEN, feature)
    }

    @Test
    fun `fromApiValue should return TRACKER for tracker`() {
        val feature = ProductFeature.fromApiValue("tracker")
        assertEquals(ProductFeature.TRACKER, feature)
    }

    @Test
    fun `fromApiValue should return PREPAY for prepay`() {
        val feature = ProductFeature.fromApiValue("prepay")
        assertEquals(ProductFeature.PREPAY, feature)
    }

    @Test
    fun `fromApiValue should return BUSINESS for business`() {
        val feature = ProductFeature.fromApiValue("business")
        assertEquals(ProductFeature.BUSINESS, feature)
    }

    @Test
    fun `fromApiValue should return RESTRICTED for restricted`() {
        val feature = ProductFeature.fromApiValue("restricted")
        assertEquals(ProductFeature.RESTRICTED, feature)
    }

    // Note: Theoretically - GraphQL doesn't need this and RestAPI doesn't have this
    @Test
    fun `fromApiValue should return CHARGEDHALFHOURLY for restricted`() {
        val feature = ProductFeature.fromApiValue("chargedhalfhourly")
        assertEquals(ProductFeature.CHARGEDHALFHOURLY, feature)
    }

    @Test
    fun `fromApiValue should return null for unknown value`() {
        val feature = ProductFeature.fromApiValue("some_unknown_value")
        assertNull(feature)
    }

    @Test
    fun `fromApiValue should return null for null value`() {
        val feature = ProductFeature.fromApiValue(null)
        assertNull(feature)
    }

    @Test
    fun `fromApiValue should return null for empty string`() {
        val feature = ProductFeature.fromApiValue("")
        assertNull(feature)
    }

    @Test
    fun `fromApiValue should return correct feature for case insensitive match`() {
        val featureVariable = ProductFeature.fromApiValue("VaRiAbLe")
        assertEquals(ProductFeature.VARIABLE, featureVariable)

        val featureGreen = ProductFeature.fromApiValue("GrEeN")
        assertEquals(ProductFeature.GREEN, featureGreen)
    }
}
