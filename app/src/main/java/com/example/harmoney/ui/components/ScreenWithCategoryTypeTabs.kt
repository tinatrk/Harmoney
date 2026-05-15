package com.example.harmoney.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
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

@Preview(showBackground = true, backgroundColor = 0xFF201923, showSystemUi = true)
@Composable
private fun ScreenWithCategoryTypeTabs_DarkPreview() {
    HarmTheme(darkTheme = true) {
        ScreenWithCategoryTypeTabs(
            tabs = CategoryType.entries,
            selectedTabIndex = 0,
            onTabClick = {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(HarmTheme.colors.surface),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.im_empty_screen),
                    contentDescription = stringResource(
                        R.string.placeholder_empty_transaction_list
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.placeholder_empty_transaction_list),
                    style = HarmTheme.typography.titleMediumSemiBold,
                    color = HarmTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF, showSystemUi = true)
@Composable
private fun ScreenWithCategoryTypeTabs_LightPreview() {
    HarmTheme(darkTheme = false) {
        ScreenWithCategoryTypeTabs(
            tabs = CategoryType.entries,
            selectedTabIndex = 0,
            onTabClick = {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(HarmTheme.colors.surface),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.im_empty_screen),
                    contentDescription = stringResource(
                        R.string.placeholder_empty_transaction_list
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.placeholder_empty_transaction_list),
                    style = HarmTheme.typography.titleMediumSemiBold,
                    color = HarmTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
