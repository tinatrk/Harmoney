package com.example.harmoney.core.uilibrary.drawers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmModalDrawer` - ModalNavigationDrawer with Harm theme colors
 * - `HarmDrawerItem` - NavigationDrawerItem with Harm theme colors
 * */
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
            scrimColor = colors.categoryIconTint,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = colors.surface,
                    drawerContentColor = colors.onSurface
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = title,
                            style = typography.titleLargeSemiBold,
                            color = colors.onSurface
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
                    color = colors.onSurface
                )
            },
            selected = selected,
            onClick = onClick,
            badge = badge,
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = colors.primaryContainer,
                unselectedContainerColor = colors.surface,
                selectedIconColor = colors.onPrimaryContainer,
                unselectedIconColor = colors.onPrimaryContainer,
                selectedTextColor = colors.onPrimaryContainer,
                unselectedTextColor = colors.onPrimaryContainer,
                selectedBadgeColor = colors.onPrimaryContainer,
                unselectedBadgeColor = colors.onPrimaryContainer,
            )
        )
    }
}
