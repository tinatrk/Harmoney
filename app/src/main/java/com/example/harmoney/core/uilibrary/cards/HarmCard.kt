package com.example.harmoney.core.uilibrary.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.graphics.HarmGraphic
import com.example.harmoney.core.uilibrary.icons.HarmIcon
import com.example.harmoney.core.uilibrary.others.HarmOther
import com.example.harmoney.presentation.category.models.MenuOptions
import com.example.harmoney.presentation.models.CategoryInfoUi
import com.example.harmoney.presentation.models.CategoryUi
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.presentation.models.StatisticPeriod
import com.example.harmoney.presentation.models.TransactionUi
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmCategoryCard` - A card for displaying all types of category cards
 * - `HarmCategoryCardSumTransactions` - A card for displaying the total amount of transactions
 * for a category (with percentage)
 * - `HarmCategoryCardOneTransaction` - A card for displaying one transaction of a category
 * - `HarmSimpleCategoryCard` - A card for displaying categories when creating a transaction
 * - `HarmCategoryCardWithMenu` - A card for displaying categories when creating a category
 * (with dropdown menu)
 * - `HarmStatisticCard` - A card with a pie chart (shows the total amount of transactions
 * of each category)
 * - `HarmCardTransactionList` - A list with data and transactions for one day
 */
object HarmCard {
    /** A card for displaying all types of category cards */
    @Composable
    fun HarmCategoryCard(
        onCardClick: () -> Unit,
        mainContent: @Composable (RowScope.() -> Unit),
        modifier: Modifier = Modifier,
        endContent: @Composable (RowScope.() -> Unit)? = null,
    ) {
        Card(
            modifier = modifier.clickable { onCardClick() },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = HarmTheme.colors.surfaceContainer,
                contentColor = HarmTheme.colors.onSurfaceContainer,
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                mainContent()

                endContent?.let {
                    endContent()
                }
            }
        }
    }

    /** A card for displaying the total amount of transactions for a category (with percentage) */
    @Composable
    fun HarmCategoryCardSumTransactions(
        category: CategoryUi,
        onCardClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HarmCategoryCard(
            modifier = modifier,
            onCardClick = onCardClick,
            mainContent = {
                HarmCategoryCardBody(
                    modifier = Modifier.weight(1f), categoryInfo = category.info
                )
            },
            endContent = {
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = stringResource(
                        R.string.pattern_money_percentage,
                        category.percentage
                    ),
                    style = HarmTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = stringResource(R.string.pattern_money_russian, category.totalAmount),
                    style = HarmTheme.typography.bodyLarge
                )
            }
        )
    }

    /** A card for displaying one transaction of a category */
    @Composable
    fun HarmCategoryCardOneTransaction(
        categoryInfo: CategoryInfoUi,
        transactionAmount: Float,
        onCardClick: () -> Unit,
        transactionNote: String?,
        modifier: Modifier = Modifier,
    ) {
        HarmCategoryCard(
            modifier = modifier,
            onCardClick = onCardClick,
            mainContent = {
                HarmCategoryCardBody(
                    modifier = Modifier.weight(1f),
                    categoryInfo = categoryInfo,
                    subText = transactionNote
                )
            },
            endContent = {
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = stringResource(R.string.pattern_money_russian, transactionAmount),
                    style = HarmTheme.typography.bodyLarge
                )
            }
        )
    }

    /** A card for displaying categories when creating a transaction */
    @Composable
    fun HarmSimpleCategoryCard(
        category: CategoryUi,
        onCardClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HarmCategoryCard(
            modifier = modifier,
            onCardClick = onCardClick,
            mainContent = {
                HarmCategoryCardBody(
                    modifier = Modifier.weight(1f), categoryInfo = category.info
                )
            },
        )
    }

    /** A card for displaying categories when creating a category (with dropdown menu) */
    @Composable
    fun HarmCategoryCardWithMenu(
        category: CategoryUi,
        @DrawableRes iconRes: Int,
        menuOptions: List<MenuOptions>,
        onCardClick: () -> Unit,
        modifier: Modifier = Modifier,
        iconContentDescription: String?,
    ) {
        HarmCategoryCard(
            modifier = modifier,
            onCardClick = onCardClick,
            mainContent = {
                HarmCategoryCardBody(
                    modifier = Modifier.weight(1f), categoryInfo = category.info
                )
            },
            endContent = {
                Spacer(modifier = Modifier.width(8.dp))

                HarmButton.HarmDropdownMenuIcon(
                    iconRes = iconRes,
                    contentDescription = iconContentDescription,
                    menuOptions = menuOptions
                )
            }
        )
    }

    /** The main content that is repeated on all category cards */
    @Composable
    private fun HarmCategoryCardBody(
        categoryInfo: CategoryInfoUi,
        modifier: Modifier = Modifier,
        subText: String? = null
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HarmIcon.HarmCircularCategoryIcon(
                backgroundColorValue = categoryInfo.icon.backgroundColor.colorValue,
                iconRes = categoryInfo.icon.ids.resIconId,
                contentDescription = stringResource(R.string.ic_category_desc, categoryInfo.name),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = categoryInfo.name,
                    style = HarmTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (!subText.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subText,
                        style = HarmTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }

    /** A card with a pie chart (shows the total amount of transactions of each category) */
    @Composable
    fun HarmStatisticCard(
        periods: List<StatisticPeriod>,
        data: String,
        pieChartItems: List<PieChartItem>,
        total: Float,
        selectedPeriodId: Long,
        onPeriodClick: (Long) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = HarmTheme.colors.surfaceContainer,
                contentColor = HarmTheme.colors.onSurfaceContainer,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HarmOther.HarmStatisticPeriodList(
                    data = data,
                    periods = periods,
                    selectedPeriodId = selectedPeriodId,
                    onPeriodClick = onPeriodClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (total > 0) {
                        HarmGraphic.PieChart(
                            items = pieChartItems,
                            total = total
                        )
                        Box(
                            modifier = Modifier.size(110.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.pattern_money_russian, total),
                                style = HarmTheme.typography.bodyLarge,
                                color = HarmTheme.colors.onSurfaceContainer,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.placeholder_empty_transaction_list),
                            style = HarmTheme.typography.titleLarge,
                        )
                    }

                }
            }
        }
    }

    /** A list with data and transactions for one day */
    @Composable
    fun HarmCardTransactionList(
        data: String,
        totalAmount: Float,
        transactions: List<TransactionUi>,
        onTransactionClick: (Long) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.pattern_text_with_colon, data),
                    style = HarmTheme.typography.titleMedium,
                    color = HarmTheme.colors.onSurfaceContainer,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.pattern_money_russian, totalAmount),
                    style = HarmTheme.typography.bodyLarge,
                    color = HarmTheme.colors.onSurface,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            transactions.forEach { transaction ->
                HarmCategoryCardOneTransaction(
                    categoryInfo = transaction.category,
                    transactionAmount = transaction.amount,
                    onCardClick = { onTransactionClick(transaction.id) },
                    transactionNote = transaction.note
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
