package com.example.harmoney.core.uilibrary.drawers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.menus.HarmMenu
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.category.models.MenuOptions
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmModalDrawer` - ModalNavigationDrawer with Harm theme colors
 * - `HarmDrawerItem` - NavigationDrawerItem with Harm theme colors
 * */
@UiLibrary
object HarmDrawer {
    /** ModalNavigationDrawer with Harm theme colors */
    @Composable
    fun HarmModalDrawer(
        title: String,
        drawerState: DrawerState,
        modifier: Modifier = Modifier,
        drawerItems: @Composable () -> Unit,
        screen: @Composable () -> Unit,
    ) {
        val colors = HarmTheme.colors
        val typography = HarmTheme.typography

        ModalNavigationDrawer(
            modifier = modifier,
            drawerState = drawerState,
            scrimColor = colors.borderAndScrim,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = colors.surfaceContainerLow,
                    drawerContentColor = colors.onSurfaceVariant
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = title,
                            style = typography.titleLargeSemiBold,
                            color = colors.onSurfaceVariant
                        )
                        HorizontalDivider(thickness = 1.dp, color = colors.outline)

                        drawerItems()
                    }
                }
            },
        ) {
            screen()
        }
    }

    /** NavigationDrawerItem with Harm theme colors */
    @Composable
    fun HarmDrawerItem(
        label: String,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        badge: @Composable (() -> Unit)? = null,
    ) {
        val colors = HarmTheme.colors
        val typography = HarmTheme.typography

        NavigationDrawerItem(
            modifier = modifier,
            label = {
                Text(
                    text = label,
                    style = typography.bodyLarge,
                    color = colors.onSurfaceVariant
                )
            },
            selected = selected,
            onClick = onClick,
            badge = badge,
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = colors.secondaryContainer,
                unselectedContainerColor = colors.surfaceContainerLow,
                selectedIconColor = colors.onSurfaceContainer,
                unselectedIconColor = colors.surfaceContainerLow,
                selectedTextColor = colors.onSurfaceContainer,
                unselectedTextColor = colors.surfaceContainerLow,
                selectedBadgeColor = colors.onSurfaceContainer,
                unselectedBadgeColor = colors.surfaceContainerLow,
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923, showSystemUi = true)
@Composable
private fun HarmModalDrawer_DarkPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    HarmTheme(darkTheme = true) {
        HarmDrawer.HarmModalDrawer(
            title = stringResource(R.string.title_drawer_settings),
            drawerState = drawerState,
            drawerItems = {
                PreviewDrawerItems(
                    isThemeDark = true,
                    firstDayMonth = "1",
                    isCurrencyMenuOpened = false,
                    currentCurrency = "RUB"
                )
            }
        ) { }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF, showSystemUi = true)
@Composable
private fun HarmModalDrawer_LightPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    HarmTheme(darkTheme = false) {
        HarmDrawer.HarmModalDrawer(
            title = stringResource(R.string.title_drawer_settings),
            drawerState = drawerState,
            drawerItems = {
                PreviewDrawerItems(
                    isThemeDark = false,
                    firstDayMonth = "1",
                    isCurrencyMenuOpened = false,
                    currentCurrency = "RUB"
                )
            }
        ) { }
    }
}

@Composable
private fun PreviewDrawerItems(
    isThemeDark: Boolean,
    firstDayMonth: String,
    isCurrencyMenuOpened: Boolean,
    currentCurrency: String
) {
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_item_theme),
        selected = false,
        onClick = {},
        badge = {
            HarmButton.HarmSwitch(
                isChecked = isThemeDark,
                onClick = {}
            )
        }
    )
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_item_first_day_month),
        selected = false,
        onClick = {},
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
        onClick = {},
        badge = {
            HarmMenu.HarmDropdownMenu(
                expanded = isCurrencyMenuOpened,
                menuOptions = Currency.entries.sortedBy { it.code }.map { currency ->
                    MenuOptions(
                        text = currency.code
                    ) {}
                },
                onDismissRequest = {}
            ) {
                Text(
                    text = currentCurrency,
                    style = HarmTheme.typography.bodyLarge,
                    color = HarmTheme.colors.onSurface
                )
            }
        }
    )
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_item_category_list),
        selected = false,
        onClick = {}
    )
}
