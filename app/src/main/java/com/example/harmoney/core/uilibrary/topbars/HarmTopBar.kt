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
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmCommonTopBar` - Basic top app bar
 * - `HarmSimpleTopBar` - Top app bar with navigation button (without actions and subtitle).
 * By default, back icon is used.
 */
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
        val startTitleOffset =
            if (navigationIconRes == null && actionIconRes != null && isTitleCenterAlignment) {
                topAppBarIconSize
            } else {
                0.dp
            }
        val endTitleOffset =
            if (navigationIconRes != null && actionIconRes == null && isTitleCenterAlignment) {
                topAppBarIconSize
            } else {
                0.dp
            }
        TopAppBar(
            modifier = modifier,
            title = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = startTitleOffset, end = endTitleOffset),
                    horizontalAlignment = if (isTitleCenterAlignment) {
                        Alignment.CenterHorizontally
                    } else {
                        Alignment.Start
                    }
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
                containerColor = HarmTheme.colors.surfaceVariant,
                navigationIconContentColor = HarmTheme.colors.onSurfaceContainer,
                actionIconContentColor = HarmTheme.colors.onSurfaceContainer,
                titleContentColor = HarmTheme.colors.onSurfaceContainer
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
            navigationIconDesc = navigationIconDesc ?: stringResource(R.string.ic_arrow_back_desc),
            onNavigationIconClick = onNavigationIconClick,
        )
    }
}
