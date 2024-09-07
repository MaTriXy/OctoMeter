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

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.domain.model.product

import co.touchlab.kermit.Logger
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.briefcase
import kunigami.composeapp.generated.resources.dashboard
import kunigami.composeapp.generated.resources.lock
import kunigami.composeapp.generated.resources.product_feature_business
import kunigami.composeapp.generated.resources.product_feature_green
import kunigami.composeapp.generated.resources.product_feature_halfhourly
import kunigami.composeapp.generated.resources.product_feature_prepay
import kunigami.composeapp.generated.resources.product_feature_restricted
import kunigami.composeapp.generated.resources.product_feature_tracker
import kunigami.composeapp.generated.resources.product_feature_variable
import kunigami.composeapp.generated.resources.shuffle
import kunigami.composeapp.generated.resources.time_duration_30
import kunigami.composeapp.generated.resources.trees
import kunigami.composeapp.generated.resources.wallet
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

enum class ProductFeature(val stringResource: StringResource, val iconResource: DrawableResource) {
    VARIABLE(stringResource = Res.string.product_feature_variable, iconResource = Res.drawable.shuffle),
    CHARGEDHALFHOURLY(stringResource = Res.string.product_feature_halfhourly, iconResource = Res.drawable.time_duration_30),
    GREEN(stringResource = Res.string.product_feature_green, iconResource = Res.drawable.trees),
    TRACKER(stringResource = Res.string.product_feature_tracker, iconResource = Res.drawable.dashboard),
    PREPAY(stringResource = Res.string.product_feature_prepay, iconResource = Res.drawable.wallet),
    BUSINESS(stringResource = Res.string.product_feature_business, iconResource = Res.drawable.briefcase),
    RESTRICTED(stringResource = Res.string.product_feature_restricted, iconResource = Res.drawable.lock),
    ;

    companion object {
        fun fromApiValue(value: String?): ProductFeature? = try {
            value?.let {
                ProductFeature.valueOf(it.uppercase())
            }
        } catch (e: IllegalArgumentException) {
            Logger.e(tag = "ProductFeature", messageString = "failed to convert $value")
            null
        }
    }
}
