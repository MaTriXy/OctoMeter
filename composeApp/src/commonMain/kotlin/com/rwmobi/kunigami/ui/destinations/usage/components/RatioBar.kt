/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import io.github.koalaplot.core.util.toString
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.unit_percent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RatioBar(
    modifier: Modifier = Modifier,
    consumptionRatio: Double, // Ratio between 0.0 and 1.0
) {
    val consumptionColor = MaterialTheme.colorScheme.primary
    val standingChargeColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    val effectiveRatio = consumptionRatio.coerceIn(minimumValue = 0.0, maximumValue = 1.0)

    Box(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.large)
            .drawBehind {
                val consumptionWidth = size.width * effectiveRatio.toFloat()
                val standingChargeWidth = size.width - consumptionWidth

                drawIntoCanvas {
                    // Draw the consumption part
                    drawRect(
                        color = consumptionColor,
                        size = size.copy(width = consumptionWidth),
                    )

                    // Draw the standing charge part
                    drawRect(
                        color = standingChargeColor,
                        topLeft = androidx.compose.ui.geometry.Offset(x = consumptionWidth, y = 0f),
                        size = size.copy(width = standingChargeWidth),
                    )
                }
            },
    ) {
        Text(
            modifier = Modifier.align(alignment = Alignment.Center),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            text = stringResource(resource = Res.string.unit_percent, (consumptionRatio * 100).toString(precision = 0)),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup(
        modifier = Modifier.padding(all = 8.dp),
    ) { dimension ->
        RatioBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimension.grid_2),
            consumptionRatio = 0.64,
        )
    }
}
