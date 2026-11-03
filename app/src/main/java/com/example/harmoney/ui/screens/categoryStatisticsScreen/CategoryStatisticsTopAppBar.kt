package com.example.harmoney.ui.screens.categoryStatisticsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.menus.HarmMenu
import com.example.harmoney.core.uilibrary.other.HarmAccountScopeContent
import com.example.harmoney.core.uilibrary.other.HarmTitleWithEndIcon
import com.example.harmoney.core.uilibrary.progressbar.HarmProgressBar
import com.example.harmoney.presentation.categoryStatistics.models.AccountContent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.models.CategoryStatisticsContentError
import com.example.harmoney.presentation.models.ContentState
import com.example.harmoney.presentation.models.MenuOption
import com.example.harmoney.ui.components.HarmMessage
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CategoryStatisticsTopAppBar(
    accountContentState: ContentState<AccountContent, CategoryStatisticsContentError>,
    isAccountScopesMenuOpen: Boolean,
    modifier: Modifier = Modifier,
    onEvent: (CategoryStatisticsEvent) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        when (accountContentState) {
            is ContentState.Idle -> Unit
            is ContentState.Loading -> HarmProgressBar()
            is ContentState.Error -> {
                when (accountContentState.errorType) {
                    CategoryStatisticsContentError.UPDATE_DATA_ERROR ->
                        HarmMessage(message = stringResource(R.string.toast_load_accounts_error))

                    else -> {}
                }
            }

            is ContentState.Success -> {
                HarmMenu.HarmDropdownMenu(
                    expanded = isAccountScopesMenuOpen,
                    onDismissRequest = {
                        onEvent(
                            CategoryStatisticsEvent.OnAccountScopeMenuDismiss
                        )
                    },
                    menuOptions = accountContentState.content.accountScopes.map { scope ->
                        MenuOption(
                            body = { HarmAccountScopeContent(scope) },
                            expanded = scope.id == accountContentState
                                .content.selectedAccountScope?.id
                        ) {
                            onEvent(
                                CategoryStatisticsEvent
                                    .OnAccountScopeChanged(scope)
                            )
                        }
                    }.toImmutableList(),
                    menuSource = accountContentState.content.selectedAccountScope?.let {
                        {
                            HarmTitleWithEndIcon(
                                modifier = Modifier.clickable {
                                    onEvent(CategoryStatisticsEvent.OnAccountScopeMenuClick)
                                },
                                title = accountContentState.content.selectedAccountScope.balance,
                                subTitle = accountContentState.content.selectedAccountScope.name,
                                iconRes = R.drawable.ic_menu_arrow_down_24,
                                iconContentDescription = stringResource(
                                    R.string.ic_menu_arrow_down_desc
                                ),
                                onIconClick = {
                                    onEvent(
                                        CategoryStatisticsEvent
                                            .OnAccountScopeMenuClick
                                    )
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun CategoryStatisticsTopAppBar_Idle_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState = ContentState.Idle,
            isAccountScopesMenuOpen = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun CategoryStatisticsTopAppBar_Idle_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState = ContentState.Idle,
            isAccountScopesMenuOpen = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun CategoryStatisticsTopAppBar_Loading_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState = ContentState.Loading,
            isAccountScopesMenuOpen = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun CategoryStatisticsTopAppBar_Loading_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState = ContentState.Loading,
            isAccountScopesMenuOpen = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun CategoryStatisticsTopAppBar_DataLoadingError_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState =
                ContentState.Error(CategoryStatisticsContentError.UPDATE_DATA_ERROR),
            isAccountScopesMenuOpen = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun CategoryStatisticsTopAppBar_DataLoadingError_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState =
                ContentState.Error(CategoryStatisticsContentError.UPDATE_DATA_ERROR),
            isAccountScopesMenuOpen = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun CategoryStatisticsTopAppBar_NetworkConnectionError_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState = ContentState
                .Error(CategoryStatisticsContentError.NETWORK_CONNECTION_ERROR),
            isAccountScopesMenuOpen = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun CategoryStatisticsTopAppBar_NetworkConnectionError_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState = ContentState
                .Error(CategoryStatisticsContentError.NETWORK_CONNECTION_ERROR),
            isAccountScopesMenuOpen = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun CategoryStatisticsTopAppBar_Content_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState = ContentState.Success(PreviewData.getAccountContent()),
            isAccountScopesMenuOpen = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun CategoryStatisticsTopAppBar_Content_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState = ContentState.Success(PreviewData.getAccountContent()),
            isAccountScopesMenuOpen = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun CategoryStatisticsTopAppBar_ContentWithOpenMenu_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState = ContentState.Success(PreviewData.getAccountContent()),
            isAccountScopesMenuOpen = true
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun CategoryStatisticsTopAppBar_ContentWithOpenMenu_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsTopAppBar(
            onEvent = {},
            accountContentState = ContentState.Success(PreviewData.getAccountContent()),
            isAccountScopesMenuOpen = true
        )
    }
}
