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

package com.rwmobi.kunigami.ui.model.product

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("TooManyFunctions")
class RetailRegionTest {

    // Grouping tests for valid codes
    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsA() {
        assertEquals(RetailRegion.EASTERN_ENGLAND, RetailRegion.fromCode("A"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsB() {
        assertEquals(RetailRegion.EAST_MIDLANDS, RetailRegion.fromCode("B"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsC() {
        assertEquals(RetailRegion.LONDON, RetailRegion.fromCode("C"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsD() {
        assertEquals(RetailRegion.MERSEYSIDE_NORTHERN_WALES, RetailRegion.fromCode("D"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsE() {
        assertEquals(RetailRegion.WEST_MIDLANDS, RetailRegion.fromCode("E"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsF() {
        assertEquals(RetailRegion.NORTH_EASTERN_ENGLAND, RetailRegion.fromCode("F"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsG() {
        assertEquals(RetailRegion.NORTH_WESTERN_ENGLAND, RetailRegion.fromCode("G"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsH() {
        assertEquals(RetailRegion.SOUTHERN_ENGLAND, RetailRegion.fromCode("H"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsJ() {
        assertEquals(RetailRegion.SOUTH_EASTERN_ENGLAND, RetailRegion.fromCode("J"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsK() {
        assertEquals(RetailRegion.SOUTHERN_WALES, RetailRegion.fromCode("K"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsL() {
        assertEquals(RetailRegion.SOUTH_WESTERN_ENGLAND, RetailRegion.fromCode("L"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsN() {
        assertEquals(RetailRegion.SOUTHERN_SCOTLAND, RetailRegion.fromCode("N"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsM() {
        assertEquals(RetailRegion.YORKSHIRE, RetailRegion.fromCode("M"))
    }

    @Test
    fun fromCode_ShouldReturnCorrectRegion_WhenCodeIsP() {
        assertEquals(RetailRegion.NORTHERN_SCOTLAND, RetailRegion.fromCode("P"))
    }

    // Grouping tests for invalid codes
    @Test
    fun fromCode_ShouldReturnNull_WhenCodeIsInvalid() {
        assertNull(RetailRegion.fromCode("X"))
    }

    @Test
    fun fromCode_ShouldReturnNull_WhenCodeIsEmpty() {
        assertNull(RetailRegion.fromCode(""))
    }
}
