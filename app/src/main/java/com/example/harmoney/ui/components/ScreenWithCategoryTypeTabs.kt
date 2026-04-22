package com.example.harmoney.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.harmoney.core.uilibrary.tabs.HarmTab
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun ScreenWithCategoryTypeTabs(
    tabs: List<CategoryType>,
    selectedTabIndex: Int,
    onTabClick: (categoryType: CategoryType) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)
) {
    Column(
        modifier = modifier
    ) {
        HarmTab.HarmPrimaryTabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            tabs.forEachIndexed { index, tab ->
                HarmTab.HarmCommonTab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        onTabClick(tab)
                    },
                    text = stringResource(tab.titleId)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(HarmTheme.colors.surface)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            content()
        }
    }
}
