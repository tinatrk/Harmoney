package com.example.harmoney.core.uilibrary.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.ui.mappers.CategoryTypeUiMapper.toStringRes
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmPrimaryTabRow` - PrimaryTabRow with HarmTheme colors
 * - `HarmCommonTab` - Tab with HarmTheme colors
 * */
@UiLibrary
object HarmTab {
    /** PrimaryTabRow with HarmTheme colors */
    @Composable
    fun HarmPrimaryTabRow(
        selectedTabIndex: Int,
        tabs: @Composable (() -> Unit)
    ) {
        val colors = HarmTheme.colors

        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = colors.surfaceContainer,
            contentColor = colors.onSurfaceContainer,
            indicator = {
            },
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Transparent
                )
            },
            tabs = tabs
        )
    }

    /** Tab with HarmTheme colors */
    @Composable
    fun HarmCommonTab(
        selected: Boolean = false,
        onClick: () -> Unit,
        text: String,
    ) {
        val backgroundColor =
            if (selected) HarmTheme.colors.primary else HarmTheme.colors.surfaceContainer

        Tab(
            selected = selected,
            onClick = onClick,
            selectedContentColor = HarmTheme.colors.onPrimary,
            unselectedContentColor = HarmTheme.colors.onSurfaceContainerLow
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(backgroundColor),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = text,
                    style = HarmTheme.typography.bodyLargeSemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmPrimaryTabRow_DarkPreview() {
    val selectedTabIndex = 0
    HarmTheme(darkTheme = true) {
        HarmTab.HarmPrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            tabs = {
                CategoryType.entries.forEachIndexed { index, tab ->
                    HarmTab.HarmCommonTab(
                        selected = selectedTabIndex == index,
                        onClick = {},
                        text = stringResource(tab.toStringRes())
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmPrimaryTabRow_LightPreview() {
    val selectedTabIndex = 0
    HarmTheme(darkTheme = false) {
        HarmTab.HarmPrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            tabs = {
                CategoryType.entries.forEachIndexed { index, tab ->
                    HarmTab.HarmCommonTab(
                        selected = selectedTabIndex == index,
                        onClick = {},
                        text = stringResource(tab.toStringRes())
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCommonTab_SelectedDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTab.HarmCommonTab(
            selected = true,
            onClick = {},
            text = stringResource(R.string.category_type_expenses_title)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCommonTab_SelectedLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTab.HarmCommonTab(
            selected = true,
            onClick = {},
            text = stringResource(R.string.category_type_expenses_title)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCommonTab_UnselectedDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTab.HarmCommonTab(
            selected = false,
            onClick = {},
            text = stringResource(R.string.category_type_expenses_title)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCommonTab_UnselectedLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTab.HarmCommonTab(
            selected = false,
            onClick = {},
            text = stringResource(R.string.category_type_expenses_title)
        )
    }
}
