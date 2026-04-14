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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmPrimaryTabRow` - PrimaryTabRow with HarmTheme colors
 * - `HarmTab` - Tab with HarmTheme colors
 * */
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
            indicator = {},
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = HarmTheme.colors.outline
                )
            },
            tabs = tabs
        )
    }

    /** Tab with HarmTheme colors */
    @Composable
    fun HarmTab(
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
