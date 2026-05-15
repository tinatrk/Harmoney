package com.example.harmoney.core.uilibrary.topbars

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmCommonTopBar` - Basic top app bar
 * - `HarmSimpleTopBar` - Top app bar with navigation button (without actions and subtitle).
 * By default, back icon is used.
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
        @DrawableRes navigationIconRes: Int? = null,
        navigationIconDesc: String? = null,
        @DrawableRes actionIconRes: Int? = null,
        actionIconDesc: String? = null,
        isTitleCenterAlignment: Boolean = false,
        onNavigationIconClick: (() -> Unit)? = null,
        onActionIconClick: (() -> Unit)? = null
    ) {
        // пришлось вручную прописать размер иконок, чтобы правильно центрировать заголовок
        val topAppBarIconSize = 48.dp

        // Сделала variable переменные, чтобы complexity функции была меньше 15
        var titleAlignment = Alignment.Start
        var startTitleOffset = 0.dp
        var endTitleOffset = 0.dp

        if (isTitleCenterAlignment) {
            titleAlignment = Alignment.CenterHorizontally
            if (navigationIconRes == null && actionIconRes != null) startTitleOffset =
                topAppBarIconSize
            if (navigationIconRes != null && actionIconRes == null) endTitleOffset =
                topAppBarIconSize
        }

        TopAppBar(
            modifier = modifier,
            title = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = startTitleOffset, end = endTitleOffset),
                    horizontalAlignment = titleAlignment
                ) {
                    Text(
                        text = title,
                        style = HarmTheme.typography.titleLargeSemiBold
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = HarmTheme.typography.titleMedium
                        )
                    }
                }
            },
            navigationIcon = navigationIconRes?.let {
                {
                    HarmButton.HarmTopBarIconButton(
                        modifier = Modifier.size(topAppBarIconSize),
                        iconRes = navigationIconRes,
                        onClick = onNavigationIconClick ?: {},
                        contentDescription = navigationIconDesc
                    )
                }
            } ?: {},
            actions = actionIconRes?.let {
                {
                    HarmButton.HarmTopBarIconButton(
                        modifier = Modifier.size(topAppBarIconSize),
                        iconRes = actionIconRes,
                        onClick = onActionIconClick ?: {},
                        contentDescription = actionIconDesc
                    )
                }
            } ?: {},
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
     * By default, back icon is used.
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
            navigationIconRes = navigationIconRes ?: R.drawable.ic_arrow_back_24px,
            navigationIconDesc =
                navigationIconDesc ?: stringResource(R.string.ic_arrow_back_desc),
            onNavigationIconClick = onNavigationIconClick,
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
private fun TwoIconsCenterTitleTopBar_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTopBar.HarmCommonTopBar(
            title = "100 000 ₽",
            subtitle = stringResource(R.string.title_balance),
            navigationIconRes = R.drawable.ic_drawer_menu_24px,
            navigationIconDesc = stringResource(R.string.ic_drawer_menu_desc),
            onNavigationIconClick = {},
            actionIconRes = R.drawable.ic_list_24px,
            actionIconDesc = stringResource(R.string.ic_list_desc),
            onActionIconClick = {},
            isTitleCenterAlignment = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun TwoIconsCenterTitleTopBar_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTopBar.HarmCommonTopBar(
            title = "100 000 ₽",
            subtitle = stringResource(R.string.title_balance),
            navigationIconRes = R.drawable.ic_drawer_menu_24px,
            navigationIconDesc = stringResource(R.string.ic_drawer_menu_desc),
            onNavigationIconClick = {},
            actionIconRes = R.drawable.ic_list_24px,
            actionIconDesc = stringResource(R.string.ic_list_desc),
            onActionIconClick = {},
            isTitleCenterAlignment = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun OneIconCenterTitle_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTopBar.HarmCommonTopBar(
            title = "100 000 ₽",
            subtitle = stringResource(R.string.title_balance),
            navigationIconRes = R.drawable.ic_arrow_back_24px,
            navigationIconDesc = stringResource(R.string.ic_arrow_back_desc),
            onNavigationIconClick = {},
            isTitleCenterAlignment = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun OneIconCenterTitle_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTopBar.HarmCommonTopBar(
            title = "100 000 ₽",
            subtitle = stringResource(R.string.title_balance),
            navigationIconRes = R.drawable.ic_arrow_back_24px,
            navigationIconDesc = stringResource(R.string.ic_arrow_back_desc),
            onNavigationIconClick = {},
            isTitleCenterAlignment = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun TwoIconsTopBar_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTopBar.HarmCommonTopBar(
            title = stringResource(R.string.title_top_app_bar_category_list),
            navigationIconRes = R.drawable.ic_arrow_back_24px,
            navigationIconDesc = stringResource(R.string.ic_arrow_back_desc),
            onNavigationIconClick = {},
            actionIconRes = R.drawable.ic_swap_vert_24px,
            actionIconDesc = stringResource(R.string.ic_swap_vert_desc),
            onActionIconClick = {},
            isTitleCenterAlignment = false
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun TwoIconsTopBar_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTopBar.HarmCommonTopBar(
            title = stringResource(R.string.title_top_app_bar_category_list),
            navigationIconRes = R.drawable.ic_arrow_back_24px,
            navigationIconDesc = stringResource(R.string.ic_arrow_back_desc),
            onNavigationIconClick = {},
            actionIconRes = R.drawable.ic_swap_vert_24px,
            actionIconDesc = stringResource(R.string.ic_swap_vert_desc),
            onActionIconClick = {},
            isTitleCenterAlignment = false
        )
    }
}
