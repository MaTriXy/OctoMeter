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

package com.rwmobi.kunigami.ui.extensions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListExtensionsKtTest {

    @Test
    fun `partitionList should return original list wrapped in a list when columns is zero`() {
        val list = listOf(1, 2, 3)
        val result = list.partitionList(0)
        assertEquals(1, result.size)
        assertEquals(list, result[0])
    }

    @Test
    fun `partitionList should return original list wrapped in a list when columns is negative`() {
        val list = listOf(1, 2, 3)
        val result = list.partitionList(-1)
        assertEquals(1, result.size)
        assertEquals(list, result[0])
    }

    @Test
    fun `partitionList should partition list into given number of columns`() {
        val list = listOf(1, 2, 3, 4, 5, 6)
        val result = list.partitionList(2)
        assertEquals(2, result.size)
        assertEquals(listOf(1, 2, 3), result[0])
        assertEquals(listOf(4, 5, 6), result[1])
    }

    @Test
    fun `partitionList should handle list size not evenly divisible by columns`() {
        val list = listOf(1, 2, 3, 4, 5)
        val result = list.partitionList(2)
        assertEquals(2, result.size)
        assertEquals(listOf(1, 2, 3), result[0])
        assertEquals(listOf(4, 5), result[1])
    }

    @Test
    fun `partitionList should handle more columns than items`() {
        val list = listOf(1, 2, 3)
        val result = list.partitionList(4)
        assertEquals(4, result.size)
        assertEquals(listOf(1), result[0])
        assertEquals(listOf(2), result[1])
        assertEquals(listOf(3), result[2])
        assertTrue(result[3].isEmpty())
    }

    @Test
    fun `partitionList should handle empty list`() {
        val list = emptyList<Int>()
        val result = list.partitionList(3)
        assertEquals(3, result.size)
        result.forEach { assertTrue(it.isEmpty()) }
    }

    @Test
    fun `partitionList should not remove the last column if columns is 2`() {
        val list = listOf(1, 2, 3, 4, 5)
        val result = list.partitionList(2)
        assertEquals(2, result.size)
        assertEquals(listOf(1, 2, 3), result[0])
        assertEquals(listOf(4, 5), result[1])
    }

    @Test
    fun `partitionList should remove the last empty column if columns is greater than 2`() {
        val list = listOf(1, 2, 3, 4)
        val result = list.partitionList(3)
        assertEquals(3, result.size)
        assertEquals(listOf(1, 2), result[0])
        assertEquals(listOf(3, 4), result[1])
        assertTrue(result[2].isEmpty())
    }
}
