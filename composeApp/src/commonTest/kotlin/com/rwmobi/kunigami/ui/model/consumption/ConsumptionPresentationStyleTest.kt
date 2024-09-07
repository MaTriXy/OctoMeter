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

package com.rwmobi.kunigami.ui.model.consumption

import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.presentation_style_day_half_hourly
import kunigami.composeapp.generated.resources.presentation_style_month_thirty_days
import kunigami.composeapp.generated.resources.presentation_style_month_weeks
import kunigami.composeapp.generated.resources.presentation_style_week_seven_days
import kunigami.composeapp.generated.resources.presentation_style_year_twelve_months
import kotlin.test.Test
import kotlin.test.assertEquals

class ConsumptionPresentationStyleTest {

    @Test
    fun `DAY_HALF_HOURLY should map to HALF_HOURLY ConsumptionDataGroup`() {
        assertEquals(ConsumptionTimeFrame.HALF_HOURLY, ConsumptionPresentationStyle.DAY_HALF_HOURLY.getConsumptionDataGroup())
    }

    @Test
    fun `WEEK_SEVEN_DAYS should map to DAY ConsumptionDataGroup`() {
        assertEquals(ConsumptionTimeFrame.DAY, ConsumptionPresentationStyle.WEEK_SEVEN_DAYS.getConsumptionDataGroup())
    }

    @Test
    fun `MONTH_WEEKS should map to WEEK ConsumptionDataGroup`() {
        assertEquals(ConsumptionTimeFrame.WEEK, ConsumptionPresentationStyle.MONTH_WEEKS.getConsumptionDataGroup())
    }

    @Test
    fun `MONTH_THIRTY_DAYS should map to DAY ConsumptionDataGroup`() {
        assertEquals(ConsumptionTimeFrame.DAY, ConsumptionPresentationStyle.MONTH_THIRTY_DAYS.getConsumptionDataGroup())
    }

    @Test
    fun `YEAR_TWELVE_MONTHS should map to MONTH ConsumptionDataGroup`() {
        assertEquals(ConsumptionTimeFrame.MONTH, ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS.getConsumptionDataGroup())
    }

    // Not a very good test, but we keep it for now.
    @Test
    fun `stringResource should match the expected resource`() {
        assertEquals(Res.string.presentation_style_day_half_hourly, ConsumptionPresentationStyle.DAY_HALF_HOURLY.stringResource)
        assertEquals(Res.string.presentation_style_week_seven_days, ConsumptionPresentationStyle.WEEK_SEVEN_DAYS.stringResource)
        assertEquals(Res.string.presentation_style_month_weeks, ConsumptionPresentationStyle.MONTH_WEEKS.stringResource)
        assertEquals(Res.string.presentation_style_month_thirty_days, ConsumptionPresentationStyle.MONTH_THIRTY_DAYS.stringResource)
        assertEquals(Res.string.presentation_style_year_twelve_months, ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS.stringResource)
    }
}
