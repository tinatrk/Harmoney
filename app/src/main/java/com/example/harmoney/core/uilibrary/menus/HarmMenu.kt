package com.example.harmoney.core.uilibrary.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.presentation.models.MenuOption
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * `HarmDropdownMenu`
 * */
@UiLibrary
object HarmMenu {
    /** DropdownMenu
     *
     * `expanded` - state is menu opened
     *
     * `menuSource` - the object that opens the menu when clicked on. For example icon (optional)
     *
     * `isNeededHighlightSelectedOption` - is needed highlight of the selected option
     * (color indication)
     * */
    @Composable
    fun HarmDropdownMenu(
        expanded: Boolean,
        menuOptions: ImmutableList<MenuOption>,
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        isNeededHighlightSelectedOption: Boolean = false,
        menuSource: @Composable (() -> Unit)? = null,
    ) {
        val scrollState = rememberScrollState()

        Box(
            modifier = modifier
                .wrapContentWidth()
                .background(Color.Unspecified),
            contentAlignment = Alignment.CenterEnd
        ) {
            menuSource?.let { menuSource() }

            DropdownMenu(
                modifier = modifier
                    .requiredHeightIn(max = 400.dp)
                    .align(Alignment.CenterEnd)
                    .background(Color.Unspecified),
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                containerColor = HarmTheme.colors.surfaceContainer,
                shape = RoundedCornerShape(12.dp),
                scrollState = scrollState,
            ) {
                Column(modifier = Modifier.background(Color.Unspecified)) {
                    menuOptions.forEach { option ->
                        val containerColor = if (isNeededHighlightSelectedOption
                            && option.expanded
                        ) {
                            HarmTheme.colors.primary
                        } else {
                            Color.Unspecified
                        }

                        val contentColor = if (isNeededHighlightSelectedOption
                            && option.expanded
                        ) {
                            HarmTheme.colors.onPrimary
                        } else {
                            HarmTheme.colors.onSurfaceVariant
                        }

                        DropdownMenuItem(
                            modifier = Modifier.background(containerColor),
                            text = {
                                Text(
                                    modifier = Modifier.background(Color.Unspecified),
                                    text = option.text,
                                    style = HarmTheme.typography.bodyMedium
                                )
                            },
                            onClick = option.onClick,
                            leadingIcon = option.leadingIconRes?.let {
                                {
                                    Icon(
                                        painter = painterResource(option.leadingIconRes),
                                        contentDescription =
                                            stringResource(R.string.ic_sort_option_desc),
                                    )
                                }
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = contentColor,
                                leadingIconColor = contentColor
                            )
                        )
                    }
                }
                LaunchedEffect(expanded) {
                    if (expanded) {
                        scrollState.scrollTo(0)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmDropdownMenu_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmMenu.HarmDropdownMenu(
            expanded = true,
            onDismissRequest = {},
            menuOptions = persistentListOf(
                MenuOption(text = "EUR", onClick = {}),
                MenuOption(text = "RUB", onClick = {}),
                MenuOption(text = "USD", onClick = {})
            ),
            menuSource = {
                Icon(
                    painter = painterResource(R.drawable.ic_menu_24px),
                    contentDescription = null,
                    tint = HarmTheme.colors.onSurface
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmDropdownMenu_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmMenu.HarmDropdownMenu(
            expanded = true,
            onDismissRequest = {},
            menuOptions = persistentListOf(
                MenuOption(text = "EUR", onClick = {}),
                MenuOption(text = "RUB", onClick = {}),
                MenuOption(text = "USD", onClick = {})
            ),
            menuSource = {
                Icon(
                    painter = painterResource(R.drawable.ic_menu_24px),
                    contentDescription = null,
                    tint = HarmTheme.colors.onSurface
                )
            }
        )
    }
}
