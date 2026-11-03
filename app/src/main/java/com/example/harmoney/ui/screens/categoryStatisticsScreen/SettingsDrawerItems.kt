package com.example.harmoney.ui.screens.categoryStatisticsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.drawers.HarmDrawer
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun SettingsDrawerItems(
    isThemeDark: Boolean,
    firstDayMonth: String,
    currentCurrencyCode: String,
    modifier: Modifier = Modifier,
    onEvent: (CategoryStatisticsEvent) -> Unit,
) {
    Column(modifier = modifier) {
        HarmDrawer.HarmDrawerItem(
            label = stringResource(R.string.title_drawer_item_theme),
            selected = false,
            onClick = { onEvent(CategoryStatisticsEvent.OnChangeTheme(!isThemeDark)) },
            badge = {
                HarmButton.HarmSwitch(
                    isChecked = isThemeDark,
                    onClick = { onEvent(CategoryStatisticsEvent.OnChangeTheme(!isThemeDark)) }
                )
            }
        )
        HarmDrawer.HarmDrawerItem(
            label = stringResource(R.string.title_drawer_item_first_day_month),
            selected = false,
            onClick = { onEvent(CategoryStatisticsEvent.OnFirstDayMonthClick) },
            badge = {
                Text(
                    text = firstDayMonth,
                    style = HarmTheme.typography.bodyLarge,
                    color = HarmTheme.colors.onSurface
                )
            }
        )
        HarmDrawer.HarmDrawerItem(
            label = stringResource(R.string.title_drawer_currency),
            selected = false,
            onClick = { onEvent(CategoryStatisticsEvent.OnCurrencySettingsClick) },
            badge = {
                Text(
                    text = currentCurrencyCode,
                    style = HarmTheme.typography.bodyLarge,
                    color = HarmTheme.colors.onSurface
                )
            }
        )
        HarmDrawer.HarmDrawerItem(
            label = stringResource(R.string.title_drawer_item_category_list),
            selected = false,
            onClick = { onEvent(CategoryStatisticsEvent.OnCategoryListClick) }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsDrawerItems_DarkPreview() {
    val isThemeDark = true
    HarmTheme(darkTheme = isThemeDark) {
        SettingsDrawerItems(
            onEvent = {},
            firstDayMonth = "1",
            currentCurrencyCode = "RUB",
            isThemeDark = isThemeDark
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsDrawerItems_LightPreview() {
    val isThemeDark = false
    HarmTheme(darkTheme = isThemeDark) {
        SettingsDrawerItems(
            onEvent = {},
            firstDayMonth = "1",
            currentCurrencyCode = "RUB",
            isThemeDark = isThemeDark
        )
    }
}
