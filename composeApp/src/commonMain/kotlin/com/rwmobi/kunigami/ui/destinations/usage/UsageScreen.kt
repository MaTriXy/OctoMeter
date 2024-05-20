/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.extensions.toLocalHourMinuteString
import com.rwmobi.kunigami.domain.model.Consumption
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.components.koalaplot.VerticalBarChart
import com.rwmobi.kunigami.ui.model.RequestedChartLayout
import com.rwmobi.kunigami.ui.theme.getDimension
import com.rwmobi.kunigami.ui.utils.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.utils.partitionList
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.TickPosition
import kotlin.math.min

@Composable
fun UsageScreen(
    modifier: Modifier = Modifier,
    uiState: UsageUIState,
    uiEvent: UsageUIEvent,
) {
    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            uiEvent.onShowSnackbar(errorMessageText)
            uiEvent.onErrorShown(errorMessage.id)
        }
    }

    val dimension = LocalDensity.current.getDimension()
    val lazyListState = rememberLazyListState()
    val colorPalette = remember {
        generateGYRHueColorPalette(
            saturation = 0.6f,
            lightness = 0.6f,
        )
    }

    Box(modifier = modifier) {
        if (uiState.consumptions.isNotEmpty()) {
            ScrollbarMultiplatform(
                modifier = Modifier.fillMaxSize(),
                enabled = uiState.consumptions.isNotEmpty(),
                lazyListState = lazyListState,
            ) { contentModifier ->
                LazyColumn(
                    modifier = contentModifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = dimension.grid_4),
                    state = lazyListState,
                ) {
                    uiState.barChartData?.let { barChartData ->
                        item {
                            BoxWithConstraints {
                                val constraintModifier = when (uiState.requestedChartLayout) {
                                    is RequestedChartLayout.Portrait -> {
                                        Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(4 / 3f)
                                    }

                                    is RequestedChartLayout.LandScape -> {
                                        Modifier
                                            .fillMaxWidth()
                                            .height(uiState.requestedChartLayout.requestedMaxHeight)
                                    }
                                }

                                VerticalBarChart(
                                    modifier = constraintModifier.padding(all = dimension.grid_2),
                                    entries = barChartData.verticalBarPlotEntries,
                                    yAxisRange = uiState.consumptionRange,
                                    yAxisTickPosition = TickPosition.Outside,
                                    xAxisTickPosition = TickPosition.Outside,
                                    yAxisTitle = "kWh",
                                    barWidth = 0.8f,
                                    colorPalette = colorPalette,
                                    labelGenerator = { index ->
                                        barChartData.labels[index]?.toString()?.padStart(2, '0')
                                    },
                                    tooltipGenerator = { index ->
                                        barChartData.tooltips[index]
                                    },
                                )
                            }
                        }
                    }

                    uiState.consumptions.forEach { consumptionGroup ->
                        item(key = "${consumptionGroup}Title") {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = dimension.grid_2),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                text = "${consumptionGroup.title}: ${consumptionGroup.consumptions.sumOf { it.consumption }.roundToTwoDecimalPlaces()} kWh",
                            )
                        }

                        // We can do fancier grouping, but for now evenly-distributed is ok
                        val partitionedItems = consumptionGroup.consumptions.partitionList(columns = uiState.requestedUsageColumns)
                        val maxRows = partitionedItems.maxOf { it.size }

                        items(maxRows) { rowIndex ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = dimension.grid_2),
                                horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
                            ) {
                                for (columnIndex in partitionedItems.indices) {
                                    val item = partitionedItems.getOrNull(columnIndex)?.getOrNull(rowIndex)
                                    if (item != null) {
                                        GridItem(
                                            modifier = Modifier.weight(1f),
                                            indicatorColor = colorPalette[
                                                getPercentageColorIndex(
                                                    value = item.consumption,
                                                    maxValue = uiState.consumptionRange.endInclusive,
                                                ),
                                            ],
                                            consumption = item,
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (!uiState.isLoading) {
            // no data
            Text("Placeholder for no data")
        }

        if (uiState.isLoading) {
            LoadingScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    LaunchedEffect(true) {
        uiEvent.onRefresh()
    }
}

private fun getPercentageColorIndex(value: Double, maxValue: Double): Int {
    return min(((value / maxValue) * 100).toInt() - 1, 99)
}

@Composable
private fun GridItem(
    modifier: Modifier = Modifier,
    indicatorColor: Color,
    consumption: Consumption,
) {
    val dimension = LocalDensity.current.getDimension()
    Row(
        modifier = modifier.fillMaxWidth()
            .background(color = indicatorColor),
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
    ) {
        Text(
            modifier = Modifier.weight(1.0f),
            text = consumption.intervalStart.toLocalHourMinuteString(),
        )

        Text(
            modifier = Modifier.wrapContentWidth(),
            fontWeight = FontWeight.Bold,
            text = consumption.consumption.toString(precision = 2),
        )
    }
}
