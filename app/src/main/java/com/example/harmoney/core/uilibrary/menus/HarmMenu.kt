package com.example.harmoney.core.uilibrary.menus

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.width
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
 * - `HarmDropdownMenu` - dropdown menu with app theme
 *
 * - `HarmMenuTextItem` - simple Text for using as menu item
 *
 * - `HarmMenuTextWithIconItem` - simple Text with unclickable leading icon for using as menu item
 *
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
                        val containerColor = if (option.expanded) {
                            HarmTheme.colors.primary
                        } else {
                            Color.Unspecified
                        }

                        val contentColor = if (option.expanded) {
                            HarmTheme.colors.onPrimary
                        } else {
                            HarmTheme.colors.onSurfaceVariant
                        }

                        DropdownMenuItem(
                            modifier = Modifier.background(containerColor),
                            text = option.body,
                            onClick = option.onClick,
                            colors = MenuDefaults.itemColors(
                                textColor = contentColor,
                                leadingIconColor = contentColor
                            )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(HarmTheme.colors.outlineVariant)
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

    /** Simple Text for using as menu item */
    @Composable
    fun HarmMenuTextItem(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        Text(
            modifier = modifier.background(Color.Unspecified),
            text = text,
            style = HarmTheme.typography.bodyMedium
        )
    }

    /** Simple Text with unclickable leading icon for using as menu item */
    @Composable
    fun HarmMenuTextWithIconItem(
        text: String,
        @DrawableRes leadingIconResId: Int,
        leadingIconContentDescription: String,
        modifier: Modifier = Modifier,
    ) {
        Row(modifier = modifier.background(Color.Unspecified)) {
            Icon(
                painter = painterResource(leadingIconResId),
                contentDescription = leadingIconContentDescription,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Unspecified),
                text = text,
                style = HarmTheme.typography.bodyMedium
            )
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
                MenuOption(body = { HarmMenu.HarmMenuTextItem("EUR") }, onClick = {}),
                MenuOption(body = { HarmMenu.HarmMenuTextItem("RUB") }, onClick = {}),
                MenuOption(body = { HarmMenu.HarmMenuTextItem("USD") }, onClick = {})
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
                MenuOption(body = { HarmMenu.HarmMenuTextItem("EUR") }, onClick = {}),
                MenuOption(body = { HarmMenu.HarmMenuTextItem("RUB") }, onClick = {}),
                MenuOption(body = { HarmMenu.HarmMenuTextItem("USD") }, onClick = {})
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

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmDropdownMenu_WithIconInItems_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmMenu.HarmDropdownMenu(
            expanded = true,
            onDismissRequest = {},
            menuOptions = persistentListOf(
                MenuOption(body = {
                    HarmMenu.HarmMenuTextWithIconItem(
                        stringResource(R.string.title_sort_alphabet),
                        R.drawable.ic_sort_by_alpha_24px,
                        leadingIconContentDescription = ""
                    )
                }, onClick = {}),
                MenuOption(body = {
                    HarmMenu.HarmMenuTextWithIconItem(
                        stringResource(R.string.title_sort_time_created),
                        R.drawable.ic_sort_numeric_24,
                        leadingIconContentDescription = ""
                    )
                }, onClick = {}),
                MenuOption(body = {
                    HarmMenu.HarmMenuTextWithIconItem(
                        stringResource(R.string.title_sort_user_order),
                        R.drawable.ic_user_sort_24px,
                        leadingIconContentDescription = ""
                    )
                }, onClick = {})
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
private fun HarmDropdownMenu_WithIconInItems_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmMenu.HarmDropdownMenu(
            expanded = true,
            onDismissRequest = {},
            menuOptions = persistentListOf(
                MenuOption(body = {
                    HarmMenu.HarmMenuTextWithIconItem(
                        stringResource(R.string.title_sort_alphabet),
                        R.drawable.ic_sort_by_alpha_24px,
                        leadingIconContentDescription = ""
                    )
                }, onClick = {}),
                MenuOption(body = {
                    HarmMenu.HarmMenuTextWithIconItem(
                        stringResource(R.string.title_sort_time_created),
                        R.drawable.ic_sort_numeric_24,
                        leadingIconContentDescription = ""
                    )
                }, onClick = {}),
                MenuOption(body = {
                    HarmMenu.HarmMenuTextWithIconItem(
                        stringResource(R.string.title_sort_user_order),
                        R.drawable.ic_user_sort_24px,
                        leadingIconContentDescription = ""
                    )
                }, onClick = {})
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
