package com.example.harmoney.core.uilibrary.topbars

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.menus.HarmMenu
import com.example.harmoney.core.uilibrary.other.HarmTitleWithEndIcon
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.toImmutableList

/**
 * - `HarmCommonTopBar` - Basic top app bar
 * - `HarmMainTopBar` - Top app bar with an input custom title
 * - `HarmSimpleTopBar` - Top app bar with navigation button (without actions and subtitle).
 * By default, arrow back icon is used.
 */
@UiLibrary
object HarmTopBar {
    /** Basic top app bar */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HarmCommonTopBar(
        title: String,
        modifier: Modifier = Modifier,
        subtitle: String? = null,
        navigationIcon: @Composable (() -> Unit)? = null,
        actionIcons: @Composable (RowScope.() -> Unit)? = null,
    ) {
        TopAppBar(
            modifier = modifier,
            title = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = title, style = HarmTheme.typography.titleLargeSemiBold)
                    if (subtitle != null) {
                        Text(text = subtitle, style = HarmTheme.typography.titleMedium)
                    }
                }
            },
            navigationIcon = navigationIcon ?: {},
            actions = actionIcons ?: {},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = HarmTheme.colors.surface,
                navigationIconContentColor = HarmTheme.colors.onSurfaceVariant,
                actionIconContentColor = HarmTheme.colors.onSurfaceVariant,
                titleContentColor = HarmTheme.colors.onSurface
            )
        )
    }

    /** Top app bar with an input custom title */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HarmMainTopBar(
        title: @Composable (() -> Unit),
        modifier: Modifier = Modifier,
        navigationIcon: @Composable (() -> Unit)? = null,
        actionIcons: @Composable (RowScope.() -> Unit)? = null,
    ) {
        // пришлось вручную прописать размер иконок, чтобы правильно центрировать заголовок
        // (центрирование будет корректным, если в navigationIcon и actionIcons не более одной
        // иконки стандартного размера)
        val topAppBarIconSize = 48.dp

        var startTitleOffset = 0.dp
        var endTitleOffset = 0.dp

        val titleAlignment = Alignment.CenterHorizontally
        if (navigationIcon == null && actionIcons != null) startTitleOffset =
            topAppBarIconSize
        if (navigationIcon != null && actionIcons == null) endTitleOffset =
            topAppBarIconSize

        TopAppBar(
            modifier = modifier,
            title = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = startTitleOffset, end = endTitleOffset),
                    horizontalAlignment = titleAlignment
                ) {
                    title()
                }
            },
            navigationIcon = navigationIcon ?: {},
            actions = actionIcons ?: {},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = HarmTheme.colors.surface,
                navigationIconContentColor = HarmTheme.colors.onSurfaceVariant,
                actionIconContentColor = HarmTheme.colors.onSurfaceVariant,
                titleContentColor = HarmTheme.colors.onSurface
            )
        )
    }

    /** Top app bar with navigation button (without actions and subtitle)
     *
     * By default, arrow back icon is used.
     * */
    @Composable
    fun HarmSimpleTopBar(
        title: String,
        onNavigationIconClick: () -> Unit,
        modifier: Modifier = Modifier,
        @DrawableRes navigationIconRes: Int? = null,
        navigationIconDesc: String? = null,
    ) {
        HarmCommonTopBar(
            modifier = modifier,
            title = title,
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = navigationIconRes ?: R.drawable.ic_arrow_back_24px,
                    contentDescription =
                        navigationIconDesc ?: stringResource(R.string.ic_arrow_back_desc),
                    onClick = onNavigationIconClick,
                )
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmSimpleTopBar_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTopBar.HarmSimpleTopBar(
            title = stringResource(R.string.title_top_app_bar_create_transaction),
            onNavigationIconClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmSimpleTopBar_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTopBar.HarmSimpleTopBar(
            title = stringResource(R.string.title_top_app_bar_create_transaction),
            onNavigationIconClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmMainTopBar_WithTwoIconsAndCloseMenu_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTopBar.HarmMainTopBar(
            title = {
                HarmMenu.HarmDropdownMenu(
                    expanded = false,
                    menuOptions = PreviewData.getAccountScopeMenu().toImmutableList(),
                    onDismissRequest = {},
                    menuSource = {
                        HarmTitleWithEndIcon(
                            title = "100 000 ₽",
                            subTitle = "Оcновная карта",
                            iconRes = R.drawable.ic_menu_arrow_down_24,
                            iconContentDescription = "",
                            onIconClick = {}
                        )
                    })
            },
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_arrow_back_24px,
                    contentDescription = stringResource(R.string.ic_arrow_back_desc),
                    onClick = {}
                )
            },
            actionIcons = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_list_24px,
                    contentDescription = stringResource(R.string.ic_list_desc),
                    onClick = {}
                )
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmMainTopBar_WithTwoIconsAndCloseMenu_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTopBar.HarmMainTopBar(
            title = {
                HarmMenu.HarmDropdownMenu(
                    expanded = false,
                    menuOptions = PreviewData.getAccountScopeMenu().toImmutableList(),
                    onDismissRequest = {},
                    menuSource = {
                        HarmTitleWithEndIcon(
                            title = "100 000 ₽",
                            subTitle = "Оcновная карта",
                            iconRes = R.drawable.ic_menu_arrow_down_24,
                            iconContentDescription = "",
                            onIconClick = {}
                        )
                    })
            },
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_arrow_back_24px,
                    contentDescription = stringResource(R.string.ic_arrow_back_desc),
                    onClick = {}
                )
            },
            actionIcons = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_list_24px,
                    contentDescription = stringResource(R.string.ic_list_desc),
                    onClick = {}
                )
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmMainTopBar_WithTwoIconsAndOpenMenu_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTopBar.HarmMainTopBar(
            title = {
                HarmMenu.HarmDropdownMenu(
                    expanded = true,
                    menuOptions = PreviewData.getAccountScopeMenu().toImmutableList(),
                    onDismissRequest = {},
                    menuSource = {
                        HarmTitleWithEndIcon(
                            title = "100 000 ₽",
                            subTitle = "Оcновная карта",
                            iconRes = R.drawable.ic_menu_arrow_down_24,
                            iconContentDescription = "",
                            onIconClick = {}
                        )
                    })
            },
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_arrow_back_24px,
                    contentDescription = stringResource(R.string.ic_arrow_back_desc),
                    onClick = {}
                )
            },
            actionIcons = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_list_24px,
                    contentDescription = stringResource(R.string.ic_list_desc),
                    onClick = {}
                )
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmMainTopBar_WithTwoIconsAndOpenMenu_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTopBar.HarmMainTopBar(
            title = {
                HarmMenu.HarmDropdownMenu(
                    expanded = true,
                    menuOptions = PreviewData.getAccountScopeMenu().toImmutableList(),
                    onDismissRequest = {},
                    menuSource = {
                        HarmTitleWithEndIcon(
                            title = "100 000 ₽",
                            subTitle = "Оcновная карта",
                            iconRes = R.drawable.ic_menu_arrow_down_24,
                            iconContentDescription = "",
                            onIconClick = {}
                        )
                    })
            },
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_arrow_back_24px,
                    contentDescription = stringResource(R.string.ic_arrow_back_desc),
                    onClick = {}
                )
            },
            actionIcons = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_list_24px,
                    contentDescription = stringResource(R.string.ic_list_desc),
                    onClick = {}
                )
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmMainTopBar_WithOneIconAndCloseMenu_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTopBar.HarmMainTopBar(
            title = {
                HarmMenu.HarmDropdownMenu(
                    expanded = false,
                    menuOptions = PreviewData.getAccountScopeMenu().toImmutableList(),
                    onDismissRequest = {},
                    menuSource = {
                        HarmTitleWithEndIcon(
                            title = "100 000 ₽",
                            subTitle = "Оcновная карта",
                            iconRes = R.drawable.ic_menu_arrow_down_24,
                            iconContentDescription = "",
                            onIconClick = {}
                        )
                    })
            },
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_arrow_back_24px,
                    contentDescription = stringResource(R.string.ic_arrow_back_desc),
                    onClick = {}
                )
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmMainTopBar_WithOneIconAndCloseMenu_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTopBar.HarmMainTopBar(
            title = {
                HarmMenu.HarmDropdownMenu(
                    expanded = false,
                    menuOptions = PreviewData.getAccountScopeMenu().toImmutableList(),
                    onDismissRequest = {},
                    menuSource = {
                        HarmTitleWithEndIcon(
                            title = "100 000 ₽",
                            subTitle = "Оcновная карта",
                            iconRes = R.drawable.ic_menu_arrow_down_24,
                            iconContentDescription = "",
                            onIconClick = {}
                        )
                    })
            },
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_arrow_back_24px,
                    contentDescription = stringResource(R.string.ic_arrow_back_desc),
                    onClick = {}
                )
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmMainTopBar_WithOneIconAndOpenMenu_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTopBar.HarmMainTopBar(
            title = {
                HarmMenu.HarmDropdownMenu(
                    expanded = true,
                    menuOptions = PreviewData.getAccountScopeMenu().toImmutableList(),
                    onDismissRequest = {},
                    menuSource = {
                        HarmTitleWithEndIcon(
                            title = "100 000 ₽",
                            subTitle = "Оcновная карта",
                            iconRes = R.drawable.ic_menu_arrow_down_24,
                            iconContentDescription = "",
                            onIconClick = {}
                        )
                    })
            },
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_arrow_back_24px,
                    contentDescription = stringResource(R.string.ic_arrow_back_desc),
                    onClick = {}
                )
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmMainTopBar_WithOneIconAndOpenMenu_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTopBar.HarmMainTopBar(
            title = {
                HarmMenu.HarmDropdownMenu(
                    expanded = true,
                    menuOptions = PreviewData.getAccountScopeMenu().toImmutableList(),
                    onDismissRequest = {},
                    menuSource = {
                        HarmTitleWithEndIcon(
                            title = "100 000 ₽",
                            subTitle = "Оcновная карта",
                            iconRes = R.drawable.ic_menu_arrow_down_24,
                            iconContentDescription = "",
                            onIconClick = {}
                        )
                    })
            },
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_arrow_back_24px,
                    contentDescription = stringResource(R.string.ic_arrow_back_desc),
                    onClick = {}
                )
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCommonTopBar_WithTwoIcons_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTopBar.HarmCommonTopBar(
            title = stringResource(R.string.title_top_app_bar_category_list),
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_arrow_back_24px,
                    contentDescription = stringResource(R.string.ic_arrow_back_desc),
                    onClick = {}
                )
            },
            actionIcons = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_swap_vert_24px,
                    contentDescription = stringResource(R.string.ic_swap_vert_desc),
                    onClick = {}
                )
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCommonTopBar_WithTwoIcons_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTopBar.HarmCommonTopBar(
            title = stringResource(R.string.title_top_app_bar_category_list),
            navigationIcon = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_arrow_back_24px,
                    contentDescription = stringResource(R.string.ic_arrow_back_desc),
                    onClick = {}
                )
            },
            actionIcons = {
                HarmButton.HarmTopBarIconButton(
                    iconRes = R.drawable.ic_swap_vert_24px,
                    contentDescription = stringResource(R.string.ic_swap_vert_desc),
                    onClick = {}
                )
            },
        )
    }
}
