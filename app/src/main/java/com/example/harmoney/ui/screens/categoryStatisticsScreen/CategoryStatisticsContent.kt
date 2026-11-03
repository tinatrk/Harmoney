package com.example.harmoney.ui.screens.categoryStatisticsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.cards.HarmCard
import com.example.harmoney.core.uilibrary.date.HarmDate
import com.example.harmoney.core.uilibrary.other.HarmPopup
import com.example.harmoney.core.uilibrary.progressbar.HarmProgressBar
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsContent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.models.CategoryStatisticsContentError
import com.example.harmoney.presentation.models.ContentState
import com.example.harmoney.presentation.models.StatisticsPeriodUi
import com.example.harmoney.ui.components.HarmMessage
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CategoryStatisticsContent(
    contentState: ContentState<CategoryStatisticsContent, CategoryStatisticsContentError>,
    exchangeRateStatus: String?,
    selectedPeriod: StatisticsPeriodUi,
    onEvent: (CategoryStatisticsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
    ) {
        when (contentState) {
            is ContentState.Idle -> Unit
            is ContentState.Loading -> {
                StatisticsLoadingContent(
                    selectedPeriod = selectedPeriod,
                    onEvent = onEvent
                )
            }

            is ContentState.Error -> {
                StatisticsErrorContent(
                    error = contentState.errorType,
                    selectedPeriod = selectedPeriod,
                    onEvent = onEvent
                )
            }

            is ContentState.Success -> {
                StatisticsSuccessContent(
                    content = contentState.content,
                    exchangeRateStatus = exchangeRateStatus,
                    selectedPeriod = selectedPeriod,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun StatisticsLoadingContent(
    selectedPeriod: StatisticsPeriodUi,
    onEvent: (CategoryStatisticsEvent) -> Unit
) {
    StatisticsPeriodSelector(
        period = selectedPeriod,
        onPeriodClick = { newPeriod ->
            onEvent(CategoryStatisticsEvent.OnStatisticsPeriodClick(newPeriod))
        }
    )
    Spacer(modifier = Modifier.height(16.dp))
    HarmProgressBar(modifier = Modifier.fillMaxSize())
}

@Composable
private fun StatisticsErrorContent(
    error: CategoryStatisticsContentError,
    selectedPeriod: StatisticsPeriodUi,
    onEvent: (CategoryStatisticsEvent) -> Unit
) {
    StatisticsPeriodSelector(
        period = selectedPeriod,
        onPeriodClick = { newPeriod ->
            onEvent(CategoryStatisticsEvent.OnStatisticsPeriodClick(newPeriod))
        }
    )
    Spacer(modifier = Modifier.height(16.dp))
    when (error) {
        CategoryStatisticsContentError.UPDATE_DATA_ERROR -> HarmMessage(
            modifier = Modifier.fillMaxSize(),
            message = stringResource(R.string.toast_load_statistics_error)
        )

        CategoryStatisticsContentError.NETWORK_CONNECTION_ERROR -> HarmMessage(
            modifier = Modifier.fillMaxSize(),
            message = stringResource(R.string.toast_network_connection_error)
        )
    }
}

@Composable
private fun StatisticsSuccessContent(
    content: CategoryStatisticsContent,
    exchangeRateStatus: String?,
    selectedPeriod: StatisticsPeriodUi,
    onEvent: (CategoryStatisticsEvent) -> Unit
) {
    if (content.pieChartCategories.isEmpty() || content.categories.isEmpty()
    ) {
        StatisticsPeriodSelector(
            period = selectedPeriod,
            onPeriodClick = { newPeriod ->
                onEvent(CategoryStatisticsEvent.OnStatisticsPeriodClick(newPeriod))
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        ExchangeRateStatus(exchangeRateStatus)
        HarmMessage(
            modifier = Modifier.fillMaxSize(),
            message = stringResource(R.string.placeholder_empty_transaction_list),
            imageRes = R.drawable.im_empty_screen,
        )
    } else {
        HarmCard.HarmStatisticCard(
            periods = StatisticsPeriodType.entries.toImmutableList(),
            data = selectedPeriod.date,
            pieChartItems = content.pieChartCategories,
            selectedPeriod = selectedPeriod.type,
            onPeriodClick = { newPeriod ->
                onEvent(CategoryStatisticsEvent.OnStatisticsPeriodClick(newPeriod))
            },
            total = {
                StatisticsTotal(
                    total = content.totalByCategoryType,
                    popupInfo = content.totalOriginalsByCategoryType
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ExchangeRateStatus(exchangeRateStatus)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(content.categories) { category ->
                HarmCard.HarmCategoryCardSumTransactions(
                    modifier = Modifier.fillMaxWidth(),
                    category = category,
                    onCardClick = {
                        onEvent(
                            CategoryStatisticsEvent.OnCategoryClick(
                                category.category.id
                            )
                        )
                    },
                    popupInfo = category.totalOrigins
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

@Composable
private fun StatisticsPeriodSelector(
    period: StatisticsPeriodUi,
    onPeriodClick: (StatisticsPeriodType) -> Unit,
    modifier: Modifier = Modifier
) {
    HarmDate.HarmStatisticPeriodList(
        modifier = modifier,
        data = period.date,
        periods = StatisticsPeriodType.entries.toImmutableList(),
        selectedPeriod = period.type,
        onPeriodClick = onPeriodClick
    )
}

@Composable
private fun ExchangeRateStatus(
    exchangeRateStatus: String?,
    modifier: Modifier = Modifier,
) {
    val status = exchangeRateStatus
        ?.takeIf { it.isNotBlank() } ?: return

    HarmPopup.HarmInfoPopup(
        modifier = modifier,
        info = status,
        popupSourceText = stringResource(R.string.title_exchange_rate_status)
    )
}

@Composable
private fun StatisticsTotal(
    total: String,
    modifier: Modifier = Modifier,
    popupInfo: String? = null,
) {
    popupInfo?.let {
        HarmPopup.HarmInfoPopup(
            modifier = modifier,
            info = popupInfo,
            popupSourceText = total,
        )
    } ?: Text(
        modifier = modifier,
        text = total,
        style = HarmTheme.typography.bodyLarge,
        color = HarmTheme.colors.onSurface,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsContent_Idle_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsContent(
            contentState = ContentState.Idle,
            exchangeRateStatus = null,
            selectedPeriod = StatisticsPeriodUi(
                type = StatisticsPeriodType.CURRENT_MONTH,
                date = "01.03.2026 - 31.03.2026"
            ),
            onEvent = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsContent_Idle_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsContent(
            contentState = ContentState.Idle,
            exchangeRateStatus = null,
            selectedPeriod = StatisticsPeriodUi(
                type = StatisticsPeriodType.CURRENT_MONTH,
                date = "01.03.2026 - 31.03.2026"
            ),
            onEvent = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsContent_Loading_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsContent(
            contentState = ContentState.Loading,
            exchangeRateStatus = null,
            selectedPeriod = StatisticsPeriodUi(
                type = StatisticsPeriodType.CURRENT_MONTH,
                date = "01.03.2026 - 31.03.2026"
            ),
            onEvent = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsContent_Loading_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsContent(
            contentState = ContentState.Loading,
            exchangeRateStatus = null,
            selectedPeriod = StatisticsPeriodUi(
                type = StatisticsPeriodType.CURRENT_MONTH,
                date = "01.03.2026 - 31.03.2026"
            ),
            onEvent = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsContent_Error_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsContent(
            contentState = ContentState
                .Error(CategoryStatisticsContentError.UPDATE_DATA_ERROR),
            exchangeRateStatus = null,
            selectedPeriod = StatisticsPeriodUi(
                type = StatisticsPeriodType.CURRENT_MONTH,
                date = "01.03.2026 - 31.03.2026"
            ),
            onEvent = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsContent_Error_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsContent(
            contentState = ContentState
                .Error(CategoryStatisticsContentError.UPDATE_DATA_ERROR),
            exchangeRateStatus = null,
            selectedPeriod = StatisticsPeriodUi(
                type = StatisticsPeriodType.CURRENT_MONTH,
                date = "01.03.2026 - 31.03.2026"
            ),
            onEvent = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsContent_Empty_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsContent(
            contentState = ContentState.Success(
                CategoryStatisticsContent(
                    categories = persistentListOf(),
                    pieChartCategories = persistentListOf()
                )
            ),
            exchangeRateStatus = null,
            selectedPeriod = StatisticsPeriodUi(
                type = StatisticsPeriodType.CURRENT_MONTH,
                date = "01.03.2026 - 31.03.2026"
            ),
            onEvent = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsContent_Empty_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsContent(
            contentState = ContentState.Success(
                CategoryStatisticsContent(
                    categories = persistentListOf(),
                    pieChartCategories = persistentListOf()
                )
            ),
            exchangeRateStatus = null,
            selectedPeriod = StatisticsPeriodUi(
                type = StatisticsPeriodType.CURRENT_MONTH,
                date = "01.03.2026 - 31.03.2026"
            ),
            onEvent = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsContent_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsContent(
            contentState = ContentState.Success(
                CategoryStatisticsContent(
                    categories = PreviewData.getExpensesCategoryStatistics(),
                    pieChartCategories = PreviewData.getExpensesPieChartCategories(),
                    totalByCategoryType = "26 000 ₽"
                )
            ),
            exchangeRateStatus = PreviewData.getExchangeRateStatus(),
            selectedPeriod = StatisticsPeriodUi(
                type = StatisticsPeriodType.CURRENT_MONTH,
                date = "01.03.2026 - 31.03.2026"
            ),
            onEvent = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsContent_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsContent(
            contentState = ContentState.Success(
                CategoryStatisticsContent(
                    categories = PreviewData.getExpensesCategoryStatistics(),
                    pieChartCategories = PreviewData.getExpensesPieChartCategories(),
                    totalByCategoryType = "26 000 ₽"
                )
            ),
            exchangeRateStatus = PreviewData.getExchangeRateStatus(),
            selectedPeriod = StatisticsPeriodUi(
                type = StatisticsPeriodType.CURRENT_MONTH,
                date = "01.03.2026 - 31.03.2026"
            ),
            onEvent = {}
        )
    }
}

