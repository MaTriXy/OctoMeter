/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.account

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant

@Immutable
data class Agreement(
    val tariffCode: String,
    val period: ClosedRange<Instant>,
    val fullName: String,
    val displayName: String,
    val description: String,
    val isHalfHourlyTariff: Boolean,
    val vatInclusiveStandingCharge: Double,
    val vatInclusiveStandardUnitRate: Double?,
    val vatInclusiveDayUnitRate: Double?,
    val vatInclusiveNightUnitRate: Double?,
    val vatInclusiveOffPeakRate: Double?,
    val agilePriceCap: Double?,
)
