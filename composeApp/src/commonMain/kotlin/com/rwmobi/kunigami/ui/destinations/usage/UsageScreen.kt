/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.extensions.toLocalDay
import com.rwmobi.kunigami.domain.extensions.toLocalDayMonth
import com.rwmobi.kunigami.domain.extensions.toLocalHourMinuteString
import com.rwmobi.kunigami.domain.extensions.toLocalMonth
import com.rwmobi.kunigami.domain.extensions.toLocalWeekdayDay
import com.rwmobi.kunigami.ui.components.IndicatorTextValueGridItem
import com.rwmobi.kunigami.ui.components.LargeTitleWithIcon
import com.rwmobi.kunigami.ui.components.LoadingScreen
import com.rwmobi.kunigami.ui.components.ScrollbarMultiplatform
import com.rwmobi.kunigami.ui.components.koalaplot.VerticalBarChart
import com.rwmobi.kunigami.ui.composehelper.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.destinations.usage.components.TariffProjectionsCardAdaptive
import com.rwmobi.kunigami.ui.destinations.usage.components.TitleNavigationBar
import com.rwmobi.kunigami.ui.extensions.getPercentageColorIndex
import com.rwmobi.kunigami.ui.extensions.partitionList
import com.rwmobi.kunigami.ui.model.chart.RequestedChartLayout
import com.rwmobi.kunigami.ui.model.consumption.ConsumptionPresentationStyle
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kotlinx.datetime.Instant
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.bolt
import kunigami.composeapp.generated.resources.kwh
import kunigami.composeapp.generated.resources.unit_kwh
import kunigami.composeapp.generated.resources.usage_energy_consumption_breakdown
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
        if (uiState.consumptionGroupedCells.isNotEmpty() || !uiState.isLoading) {
            ScrollbarMultiplatform(
                modifier = Modifier.fillMaxSize(),
                enabled = uiState.consumptionGroupedCells.isNotEmpty(),
                lazyListState = lazyListState,
            ) { contentModifier ->
                LazyColumn(
                    modifier = contentModifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = dimension.grid_4),
                    state = lazyListState,
                ) {
                    stickyHeader {
                        with(uiState.consumptionQueryFilter) {
                            TitleNavigationBar(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.secondary)
                                    .fillMaxWidth()
                                    .height(height = dimension.minListItemHeight),
                                currentPresentationStyle = uiState.consumptionQueryFilter.presentationStyle,
                                title = getConsumptionPeriodString(),
                                canNavigateBack = uiState.consumptionQueryFilter.canNavigateBackward(accountMoveInDate = uiState.userProfile?.account?.movedInAt ?: Instant.DISTANT_PAST),
                                canNavigateForward = uiState.consumptionQueryFilter.canNavigateForward(),
                                onNavigateBack = uiEvent.onPreviousTimeFrame,
                                onNavigateForward = uiEvent.onNextTimeFrame,
                                onSwitchPresentationStyle = { uiEvent.onSwitchPresentationStyle(it) },
                            )
                        }
                    }

                    if (uiState.consumptionGroupedCells.isEmpty()) {
                        item(key = "noData") {
                            Text("no data")
                        }
                    } else {
                        uiState.barChartData?.let { barChartData ->
                            item(key = "chart") {
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
                                        yAxisTitle = stringResource(resource = Res.string.kwh),
                                        colorPalette = colorPalette,
                                        labelGenerator = { index ->
                                            barChartData.labels[index]
                                        },
                                        tooltipGenerator = { index ->
                                            barChartData.tooltips[index]
                                        },
                                    )
                                }
                            }
                        }

                        item(key = "tariffAndProjections") {
                            TariffProjectionsCardAdaptive(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = dimension.grid_3,
                                        vertical = dimension.grid_1,
                                    ),
                                layoutType = uiState.requestedAdaptiveLayout,
                                tariff = uiState.userProfile?.tariff,
                                insights = uiState.insights,
                                mpan = uiState.userProfile?.selectedMpan,
                            )
                        }

                        item(key = "headingConsumptionBreakdowns") {
                            LargeTitleWithIcon(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = dimension.grid_2),
                                icon = painterResource(resource = Res.drawable.bolt),
                                label = stringResource(resource = Res.string.usage_energy_consumption_breakdown),
                            )
                        }

                        uiState.consumptionGroupedCells.forEach { consumptionGroup ->
                            item(key = "${consumptionGroup.title}Title") {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = dimension.grid_2,
                                            horizontal = dimension.grid_4,
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        text = consumptionGroup.title,
                                    )

                                    Text(
                                        modifier = Modifier.wrapContentSize(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        text = stringResource(
                                            resource = Res.string.unit_kwh,
                                            consumptionGroup.consumptions.sumOf { it.consumption }.roundToTwoDecimalPlaces(),
                                        ),
                                    )
                                }
                            }

                            // We can do fancier grouping, but for now evenly-distributed is ok
                            val partitionedItems = consumptionGroup.consumptions.partitionList(columns = uiState.requestedUsageColumns)
                            val maxRows = partitionedItems.maxOf { it.size }

                            items(maxRows) { rowIndex ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = dimension.grid_4,
                                            vertical = dimension.grid_0_25,
                                        ),
                                    horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_3),
                                ) {
                                    for (columnIndex in partitionedItems.indices) {
                                        val item = partitionedItems.getOrNull(columnIndex)?.getOrNull(rowIndex)
                                        if (item != null) {
                                            val label = when (uiState.consumptionQueryFilter.presentationStyle) {
                                                ConsumptionPresentationStyle.DAY_HALF_HOURLY -> item.intervalStart.toLocalHourMinuteString()
                                                ConsumptionPresentationStyle.WEEK_SEVEN_DAYS -> item.intervalStart.toLocalWeekdayDay()
                                                ConsumptionPresentationStyle.MONTH_WEEKS -> item.intervalStart.toLocalDayMonth()
                                                ConsumptionPresentationStyle.MONTH_THIRTY_DAYS -> item.intervalStart.toLocalDay()
                                                ConsumptionPresentationStyle.YEAR_TWELVE_MONTHS -> item.intervalStart.toLocalMonth()
                                            }
                                            IndicatorTextValueGridItem(
                                                modifier = Modifier.weight(1f),
                                                indicatorColor = colorPalette[
                                                    item.consumption.getPercentageColorIndex(
                                                        maxValue = uiState.consumptionRange.endInclusive,
                                                    ),
                                                ],
                                                label = label,
                                                value = item.consumption.toString(precision = 2),
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
            }
        }

        if (uiState.isLoading) {
            LoadingScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    LaunchedEffect(true) {
        uiEvent.onInitialLoad()
    }

    LaunchedEffect(uiState.consumptionGroupedCells) {
        lazyListState.scrollToItem(index = 0)
    }

    LaunchedEffect(uiState.requestScrollToTop) {
        if (uiState.requestScrollToTop) {
            lazyListState.scrollToItem(index = 0)
            uiEvent.onScrolledToTop()
        }
    }
}
