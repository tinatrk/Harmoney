package com.example.harmoney.core.uilibrary.menus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.presentation.category.models.MenuOptions
import com.example.harmoney.ui.theme.HarmTheme

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
     * */
    @Composable
    fun HarmDropdownMenu(
        expanded: Boolean,
        menuOptions: List<MenuOptions>,
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        menuSource: @Composable (() -> Unit)? = null
    ) {
        Box(
            modifier = modifier,
        ) {
            menuSource?.let { menuSource() }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                containerColor = HarmTheme.colors.surfaceContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                menuOptions.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.text,
                                style = HarmTheme.typography.bodyMedium
                            )
                        },
                        onClick = option.onClick,
                        colors = MenuDefaults.itemColors(
                            textColor = HarmTheme.colors.onSurfaceVariant,
                        )
                    )
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
            menuOptions = listOf(
                MenuOptions(text = "EUR", {}),
                MenuOptions(text = "RUB", {}),
                MenuOptions(text = "USD", {})
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
            menuOptions = listOf(
                MenuOptions(text = "EUR", {}),
                MenuOptions(text = "RUB", {}),
                MenuOptions(text = "USD", {})
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
