package com.example.harmoney.core.uilibrary.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.core.uilibrary.menus.HarmMenu
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.presentation.category.models.MenuOptions
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmTopBarIconButton` - IconButton for appTopBar
 * - `HarmFloatingActionButton` - Floating action button
 * - `HarmCircularIconButton` - Circular iconButton
 * - `HarmCircularIconButtonWithTitle` - Circular iconButton with title
 * - `HarmPrimaryButton` - Primary button for the main action on a screen
 * - `HarmSecondaryButton` - Secondary button for a secondary action on a screen
 * - `HarmPrimaryTextButton` - Primary text button for a low priority action on a screen
 * - `HarmSecondaryTextButton` - Secondary button for a low priority action on a screen
 * - `HarmCardIconButton` - IconButton for cards
 * - `HarmDropdownMenuIcon` - IconButton with dropdown menu logic
 * - `HarmCircularCheckBox` - Circular checkBox
 * - `HarmCheckableIconWithTitle` - Circular checkBox with title
 * - `HarmSwitch` - Switch with Harm theme colors
 * */
@UiLibrary
object HarmButton {
    /** TopBar IconButton */
    @Composable
    fun HarmTopBarIconButton(
        @DrawableRes iconRes: Int,
        onClick: () -> Unit,
        contentDescription: String?,
        modifier: Modifier = Modifier,
    ) {
        IconButton(
            modifier = modifier.size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
            ),
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription
            )
        }
    }

    /** Floating action button */
    @Composable
    fun HarmFloatingActionButton(
        @DrawableRes iconRes: Int,
        onClick: () -> Unit,
        contentDescription: String?,
        modifier: Modifier = Modifier,
    ) {
        FloatingActionButton(
            modifier = modifier,
            containerColor = HarmTheme.colors.primary,
            contentColor = HarmTheme.colors.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
            onClick = onClick,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription
            )
        }
    }

    /** Circular iconButton */
    @Composable
    fun HarmCircularIconButton(
        onClick: () -> Unit,
        @DrawableRes iconRes: Int?,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        iconBackgroundValue: Long = CategoryColors.VIOLET_T68.background,
        selected: Boolean = false,
    ) {
        Box(
            modifier = modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onClick() }
                    .background(Color(iconBackgroundValue))
                    .then(
                        if (selected) {
                            Modifier.border(
                                width = 2.dp,
                                color = HarmTheme.colors.primary,
                                shape = CircleShape
                            )
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (iconRes != null) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(iconRes),
                        contentDescription = contentDescription,
                        tint = HarmTheme.colors.borderAndScrim
                    )
                }
            }
        }
    }

    /** Circular iconButton with title */
    @Composable
    fun HarmCircularIconButtonWithTitle(
        @DrawableRes iconRes: Int,
        iconBackgroundValue: Long,
        iconTitle: String,
        onClick: () -> Unit,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        selected: Boolean = false,
    ) {
        Column(
            modifier = modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HarmCircularIconButton(
                iconRes = iconRes,
                iconBackgroundValue = iconBackgroundValue,
                onClick = onClick,
                contentDescription = contentDescription,
                selected = selected
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = iconTitle,
                color = if (selected) {
                    HarmTheme.colors.primary
                } else HarmTheme.colors.onSurfaceContainer,
                style = HarmTheme.typography.labelLarge
            )
        }
    }

    /** Primary button for the main action on a screen*/
    @Composable
    fun HarmPrimaryButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(corner = CornerSize(54.dp)),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = HarmTheme.colors.primary,
                contentColor = HarmTheme.colors.onPrimary,
                disabledContainerColor = HarmTheme.colors.surfaceContainerLow,
                disabledContentColor = HarmTheme.colors.onSurfaceContainerLow
            )
        ) {
            Text(
                text = text,
                style = HarmTheme.typography.bodyLargeSemiBold,
            )
        }
    }

    /** Secondary button for a secondary action on a screen */
    @Composable
    fun HarmSecondaryButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(corner = CornerSize(54.dp)),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = HarmTheme.colors.secondary,
                contentColor = HarmTheme.colors.onSecondary,
                disabledContainerColor = HarmTheme.colors.surfaceContainerLow,
                disabledContentColor = HarmTheme.colors.onSurfaceContainerLow
            )
        ) {
            Text(
                text = text,
                style = HarmTheme.typography.bodyLargeSemiBold,
            )
        }
    }

    /** Primary text button for a low priority action on a screen*/
    @Composable
    fun HarmPrimaryTextButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = HarmTheme.colors.primary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = HarmTheme.colors.onSurfaceContainerLow
            ),
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(
                text = text,
                style = HarmTheme.typography.bodyLargeSemiBold,
            )
        }
    }

    /** Secondary text button for a low priority action on a screen */
    @Composable
    fun HarmSecondaryTextButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = HarmTheme.colors.onSecondaryContainer,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = HarmTheme.colors.onSurfaceContainerLow
            ),
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(
                text = text,
                style = HarmTheme.typography.bodyLargeSemiBold,
            )
        }
    }

    /** Card IconButton */
    @Composable
    fun HarmCardIconButton(
        @DrawableRes iconRes: Int,
        onClick: () -> Unit,
        contentDescription: String?,
        modifier: Modifier = Modifier,
    ) {
        IconButton(
            modifier = modifier.size(40.dp),
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = HarmTheme.colors.onSurfaceVariant,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = HarmTheme.colors.onSurfaceContainerLow,
            )
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription
            )
        }
    }

    /** Card Dropdown menu icon */
    @Composable
    fun HarmDropdownMenuIcon(
        @DrawableRes iconRes: Int,
        menuOptions: List<MenuOptions>,
        contentDescription: String?,
        modifier: Modifier = Modifier,
    ) {
        val expanded = remember { mutableStateOf(false) }

        HarmMenu.HarmDropdownMenu(
            expanded = expanded.value,
            menuOptions = menuOptions,
            onDismissRequest = {expanded.value = false},
            modifier = modifier
        ) {
            HarmCardIconButton(
                iconRes = iconRes,
                contentDescription = contentDescription,
                onClick = { expanded.value = !expanded.value }
            )
        }
    }

    /** Circular checkbox */
    @Composable
    fun HarmCircularCheckBox(
        checked: Boolean,
        contentDescription: String?
    ) {
        Icon(
            painter = if (checked) {
                painterResource(R.drawable.ic_radio_button_on_24px)
            } else {
                painterResource(R.drawable.ic_radio_button_off_24px)
            },
            contentDescription = contentDescription,
            tint = if (checked) {
                HarmTheme.colors.primary
            } else {
                HarmTheme.colors.onSurfaceContainerLow
            },
        )
    }

    /** Checkable icon with title */
    @Composable
    fun HarmCheckableIconWithTitle(
        title: String,
        checked: Boolean,
        onCheckChanged: (Boolean) -> Unit,
        modifier: Modifier = Modifier,

        ) {
        Row(
            modifier = modifier
                .padding(vertical = 8.dp)
                .clickable {
                    onCheckChanged(!checked)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            HarmCircularCheckBox(
                checked = checked,
                contentDescription = if (checked) {
                    stringResource(R.string.ic_radio_button_on_desc)
                } else {
                    stringResource(R.string.ic_radio_button_off_desc)
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = HarmTheme.typography.bodyMedium,
                color = HarmTheme.colors.onSurface
            )
        }
    }

    /** Switch with Harm theme colors */
    @Composable
    fun HarmSwitch(
        isChecked: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val colors = HarmTheme.colors

        Switch(
            modifier = modifier,
            checked = isChecked,
            onCheckedChange = {
                onClick()
            },
            thumbContent = {
                if (isChecked) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.ic_ok_24px),
                        contentDescription = stringResource(R.string.ic_switch_on_desc),
                        tint = colors.primary
                    )
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = colors.onPrimary,
                uncheckedThumbColor = colors.outline,
                checkedTrackColor = colors.primary,
                uncheckedTrackColor = colors.surfaceContainerHighest,
                checkedBorderColor = Color.Transparent,
                uncheckedBorderColor = colors.outline,
            )
        )
    }
}
