package com.example.harmoney.core.uilibrary.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.presentation.models.CategoryUi
import com.example.harmoney.ui.theme.HarmTheme

object HarmCard {
    /** Card for displaying the total amount of transactions for a category */
    @Composable
    fun HarmCategoryCard(
        category: CategoryUi,
        onCardClick: (Long) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Card(
            modifier = modifier.clickable { onCardClick(category.id) },
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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .background(
                            color = Color(category.icon.backgroundColor.colorValue),
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            shape = CircleShape,
                            color = HarmTheme.colors.categoryIconTint
                        )
                        .padding(8.dp)
                        .size(24.dp),
                    painter = painterResource(CategoryIcons.fromId(category.icon.ids.id).resIconId),
                    contentDescription = stringResource(R.string.category_icon_desc, category.name),
                    tint = HarmTheme.colors.categoryIconTint
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.weight(1f),
                    text = category.name,
                    style = HarmTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = stringResource(R.string.money_percentage_pattern, category.percentage),
                    style = HarmTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = stringResource(R.string.money_russian_pattern, category.totalAmount),
                    style = HarmTheme.typography.bodyLarge
                )
            }
        }
    }
}
