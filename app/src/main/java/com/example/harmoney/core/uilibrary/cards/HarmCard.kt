package com.example.harmoney.core.uilibrary.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.icons.HarmIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.presentation.category.models.MenuOptions
import com.example.harmoney.presentation.models.CategoryUi
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmCategoryCard` - A card for displaying all types of category cards
 * - `HarmCategoryCardSumTransactions` - A card for displaying the total amount of transactions
 * for a category (with percentage)
 * - `HarmCategoryCardOneTransaction` - A card for displaying one transaction of a category
 * - `HarmSimpleCategoryCard` - A card for displaying categories when creating a transaction
 * - `HarmCategoryCardWithMenu` - A card for displaying categories when creating a category
 * (with dropdown menu)
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
                HarmCategoryCardBody(modifier = Modifier.weight(1f), category = category)
            },
            endContent = {
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = stringResource(
                        R.string.money_percentage_pattern,
                        category.percentage
                    ),
                    style = HarmTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = stringResource(R.string.money_russian_pattern, category.totalAmount),
                    style = HarmTheme.typography.bodyLarge
                )
            }
        )
    }

    /** A card for displaying one transaction of a category */
    @Composable
    fun HarmCategoryCardOneTransaction(
        category: CategoryUi,
        onCardClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HarmCategoryCard(
            modifier = modifier,
            onCardClick = onCardClick,
            mainContent = {
                HarmCategoryCardBody(modifier = Modifier.weight(1f), category = category)
            },
            endContent = {
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = stringResource(R.string.money_russian_pattern, category.totalAmount),
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
                HarmCategoryCardBody(modifier = Modifier.weight(1f), category = category)
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
                HarmCategoryCardBody(modifier = Modifier.weight(1f), category = category)
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
        category: CategoryUi,
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HarmIcon.HarmCircularCategoryIcon(
                backgroundColorValue = category.icon.backgroundColor.colorValue,
                iconRes = CategoryIcons.fromId(category.icon.ids.id).resIconId,
                contentDescription = stringResource(R.string.category_icon_desc, category.name),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = category.name,
                style = HarmTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
