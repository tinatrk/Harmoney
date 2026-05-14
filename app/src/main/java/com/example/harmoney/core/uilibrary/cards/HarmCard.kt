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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.graphics.HarmGraphic
import com.example.harmoney.core.uilibrary.icons.HarmIcon
import com.example.harmoney.core.uilibrary.others.HarmOther
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.category.models.MenuOptions
import com.example.harmoney.presentation.models.CategoryStatisticsUi
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
@UiLibrary
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
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(
                containerColor = HarmTheme.colors.surfaceContainerHigh,
                contentColor = HarmTheme.colors.onSurface,
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
        category: CategoryStatisticsUi,
        onCardClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HarmCategoryCard(
            modifier = modifier,
            onCardClick = onCardClick,
            mainContent = {
                HarmCategoryCardBody(
                    modifier = Modifier.weight(1f), categoryInfo = category.category
                )
            },
            endContent = {
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = category.percentage,
                    style = HarmTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = category.totalAmount,
                    style = HarmTheme.typography.bodyLarge
                )
            }
        )
    }

    /** A card for displaying one transaction of a category */
    @Composable
    fun HarmCategoryCardOneTransaction(
        categoryInfo: CategoryUi,
        transactionAmount: String,
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
                    text = transactionAmount,
                    style = HarmTheme.typography.bodyLarge
                )
            }
        )
    }

    /** A card for displaying categories when creating a transaction */
    @Composable
    fun HarmSimpleCategoryCard(
        category: CategoryStatisticsUi,
        onCardClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HarmCategoryCard(
            modifier = modifier,
            onCardClick = onCardClick,
            mainContent = {
                HarmCategoryCardBody(
                    modifier = Modifier.weight(1f), categoryInfo = category.category
                )
            },
        )
    }

    /** A card for displaying categories when creating a category (with dropdown menu) */
    @Composable
    fun HarmCategoryCardWithMenu(
        category: CategoryStatisticsUi,
        @DrawableRes iconRes: Int,
        isMenuOpened: Boolean,
        menuOptions: List<MenuOptions>,
        onCardClick: () -> Unit,
        onMenuClick: () -> Unit,
        onMenuDismiss: () -> Unit,
        modifier: Modifier = Modifier,
        iconContentDescription: String?,
    ) {
        HarmCategoryCard(
            modifier = modifier,
            onCardClick = onCardClick,
            mainContent = {
                HarmCategoryCardBody(
                    modifier = Modifier.weight(1f), categoryInfo = category.category
                )
            },
            endContent = {
                Spacer(modifier = Modifier.width(8.dp))

                HarmButton.HarmDropdownMenuIcon(
                    iconRes = iconRes,
                    contentDescription = iconContentDescription,
                    menuOptions = menuOptions,
                    expanded = isMenuOpened,
                    onMenuClick = onMenuClick,
                    onMenuDismiss = onMenuDismiss,
                )
            }
        )
    }

    /** The main content that is repeated on all category cards */
    @Composable
    private fun HarmCategoryCardBody(
        categoryInfo: CategoryUi,
        modifier: Modifier = Modifier,
        subText: String? = null
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HarmIcon.HarmCircularCategoryIcon(
                backgroundColorValue = categoryInfo.icon.colors.background,
                iconRes = categoryInfo.icon.ids.resIconId,
                contentDescription = stringResource(
                    R.string.ic_category_desc,
                    categoryInfo.name
                ),
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
        total: String,
        selectedPeriodId: Long,
        onPeriodClick: (Long) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                if (pieChartItems.isEmpty()) {
                    0.dp
                } else {
                    6.dp
                }
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (pieChartItems.isEmpty()) {
                    Color.Transparent
                } else {
                    HarmTheme.colors.surfaceContainerHigh
                },
                contentColor = HarmTheme.colors.onSurface,
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
                    if (pieChartItems.isNotEmpty()) {
                        HarmGraphic.PieChart(
                            items = pieChartItems
                        )
                        Box(
                            modifier = Modifier.size(110.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = total,
                                style = HarmTheme.typography.bodyLarge,
                                color = HarmTheme.colors.onSurface,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }

    /** A list with data and transactions for one day */
    @Composable
    fun HarmCardTransactionList(
        data: String,
        totalAmount: String,
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
                    color = HarmTheme.colors.onSurface,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = totalAmount,
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

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCategoryCardSumTransactions_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmCard.HarmCategoryCardSumTransactions(
            category = CategoryStatisticsUi(
                category = CategoryUi(
                    id = 0,
                    name = "Продукты",
                    type = CategoryType.Expenses,
                    icon = CategoryIcon(
                        ids = CategoryIcons.IC_SHOP_CART,
                        colors = CategoryColors.PINK_T75
                    )
                ),
                totalAmount = "2 000 ₽",
                percentage = "10.0%"
            ),
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCategoryCardSumTransactions_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmCard.HarmCategoryCardSumTransactions(
            category = CategoryStatisticsUi(
                category = CategoryUi(
                    id = 0,
                    name = "Продукты",
                    type = CategoryType.Expenses,
                    icon = CategoryIcon(
                        ids = CategoryIcons.IC_SHOP_CART,
                        colors = CategoryColors.PINK_T75
                    )
                ),
                totalAmount = "2 000 ₽",
                percentage = "10.0%"
            ),
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCategoryCardOneTransaction_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmCard.HarmCategoryCardOneTransaction(
            categoryInfo = CategoryUi(
                id = 0,
                name = "Продукты",
                type = CategoryType.Expenses,
                icon = CategoryIcon(
                    ids = CategoryIcons.IC_SHOP_CART,
                    colors = CategoryColors.PINK_T75
                )
            ),
            transactionAmount = "2 000 ₽",
            transactionNote = "На ужин",
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCategoryCardOneTransaction_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmCard.HarmCategoryCardOneTransaction(
            categoryInfo = CategoryUi(
                id = 0,
                name = "Продукты",
                type = CategoryType.Expenses,
                icon = CategoryIcon(
                    ids = CategoryIcons.IC_SHOP_CART,
                    colors = CategoryColors.PINK_T75
                )
            ),
            transactionAmount = "2 000 ₽",
            transactionNote = "На ужин",
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmSimpleCategoryCard_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmCard.HarmSimpleCategoryCard(
            category = CategoryStatisticsUi(
                category = CategoryUi(
                    id = 0,
                    name = "Продукты",
                    type = CategoryType.Expenses,
                    icon = CategoryIcon(
                        ids = CategoryIcons.IC_SHOP_CART,
                        colors = CategoryColors.PINK_T75
                    )
                ),
                totalAmount = "2 000 ₽",
                percentage = "10.0%"
            ),
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmSimpleCategoryCard_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmCard.HarmSimpleCategoryCard(
            category = CategoryStatisticsUi(
                category = CategoryUi(
                    id = 0,
                    name = "Продукты",
                    type = CategoryType.Expenses,
                    icon = CategoryIcon(
                        ids = CategoryIcons.IC_SHOP_CART,
                        colors = CategoryColors.PINK_T75
                    )
                ),
                totalAmount = "2 000 ₽",
                percentage = "10.0%"
            ),
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCategoryCardWithMenu_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmCard.HarmCategoryCardWithMenu(
            category = CategoryStatisticsUi(
                category = CategoryUi(
                    id = 0,
                    name = "Продукты",
                    type = CategoryType.Expenses,
                    icon = CategoryIcon(
                        ids = CategoryIcons.IC_SHOP_CART,
                        colors = CategoryColors.PINK_T75
                    )
                ),
                totalAmount = "2 000 ₽",
                percentage = "10.0%"
            ),
            onCardClick = {},
            iconRes = R.drawable.ic_menu_24px,
            iconContentDescription = null,
            isMenuOpened = true,
            menuOptions = listOf(
                MenuOptions(stringResource(R.string.ic_edit_desc), {}),
                MenuOptions(stringResource(R.string.ic_delete_desc), {}),
            ),
            onMenuClick = {},
            onMenuDismiss = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCategoryCardWithMenu_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmCard.HarmCategoryCardWithMenu(
            category = CategoryStatisticsUi(
                category = CategoryUi(
                    id = 0,
                    name = "Продукты",
                    type = CategoryType.Expenses,
                    icon = CategoryIcon(
                        ids = CategoryIcons.IC_SHOP_CART,
                        colors = CategoryColors.PINK_T75
                    )
                ),
                totalAmount = "2 000 ₽",
                percentage = "10.0%"
            ),
            onCardClick = {},
            iconRes = R.drawable.ic_menu_24px,
            iconContentDescription = null,
            isMenuOpened = true,
            menuOptions = listOf(
                MenuOptions(stringResource(R.string.ic_edit_desc), {}),
                MenuOptions(stringResource(R.string.ic_delete_desc), {}),
            ),
            onMenuClick = {},
            onMenuDismiss = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCardTransactionList_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmCard.HarmCardTransactionList(
            data = "01.03.2026",
            transactions = getPreviewTransactionList(),
            onTransactionClick = {},
            totalAmount = "5 000 ₽"
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCardTransactionList_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmCard.HarmCardTransactionList(
            data = "01.03.2026",
            transactions = getPreviewTransactionList(),
            onTransactionClick = {},
            totalAmount = "5 000 ₽"
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmStatisticCard_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmCard.HarmStatisticCard(
            periods = StatisticPeriod.entries,
            data = "01.03.2026 - 31.03.2026",
            pieChartItems = getPreviewDataPieChartCategories(),
            selectedPeriodId = StatisticPeriod.CURRENT_MONTH.id,
            onPeriodClick = {},
            total = "5 000 ₽"
        )
    }
}

private fun getPreviewTransactionList(): List<TransactionUi> {
    return listOf(
        TransactionUi(
            id = 1,
            category = CategoryUi(
                id = 1,
                name = "Продукты",
                type = CategoryType.Expenses,
                icon = CategoryIcon(
                    ids = CategoryIcons.IC_SHOP_CART,
                    colors = CategoryColors.PINK_T75
                ),
            ),
               amount = "2 000 ₽",
            note = "На ужин"
        ),
        TransactionUi(
            id = 2,
            category = CategoryUi(
                id = 2,
                name = "Подарки",
                type = CategoryType.Expenses,
                icon = CategoryIcon(
                    ids = CategoryIcons.IC_GIFT,
                    colors = CategoryColors.VIOLET_T68
                ),
            ),
            amount = "3 000 ₽",
        )
    )
}

private fun getPreviewDataPieChartCategories(
): List<PieChartItem> {
    return listOf(
        PieChartItem(
            value = "15 000 ₽",
            colorValue = CategoryColors.BLUE_T80.background,
            startAngle = -90f,
            sweepAngle = 205.69f,

            ),
        PieChartItem(
            value = "7 500 ₽",
            colorValue = CategoryColors.ORANGE_T70.background,
            startAngle = 117.69f,
            sweepAngle = 101.84f,

            ),
        PieChartItem(
            value = "3 500 ₽",
            colorValue = CategoryColors.VIOLET_T68.background,
            startAngle = 221.53f,
            sweepAngle = 46.46f,
        )
    )
}
