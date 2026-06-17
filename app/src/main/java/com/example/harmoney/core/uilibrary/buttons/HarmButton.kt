package com.example.harmoney.core.uilibrary.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.core.uilibrary.menus.HarmMenu
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.presentation.models.MenuOptions
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * - `HarmCardIconButton` - IconButton for cards
 * - `HarmCheckableIconWithTitle` - Circular checkBox with title
 * - `HarmCircularCheckBox` - Circular checkBox
 * - `HarmCircularIconButton` - Circular iconButton
 * - `HarmCircularIconButtonWithTitle` - Circular iconButton with title
 * - `HarmCommonButton` - Common button
 * - `HarmCommonTextButton` - Common button which looks like text
 * - `HarmDangerousButton` - Common button with colors for dangerous logic (for example, delete)
 * - `HarmDropdownMenuIcon` - IconButton with dropdown menu logic
 * - `HarmFloatingActionButton` - Floating action button
 * - `HarmPrimaryButton` - Primary button for the main action on a screen
 * - `HarmPrimaryTextButton` - Primary text button for a low priority action on a screen
 * - `HarmSecondaryButton` - Secondary button for a secondary action on a screen
 * - `HarmSecondaryTextButton` - Secondary button for a low priority action on a screen
 * - `HarmSwitch` - Switch with Harm theme colors
 * - `HarmTopBarIconButton` - IconButton for appTopBar
 * */
@UiLibrary
object HarmButton {
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
            Icon(painter = painterResource(iconRes), contentDescription = contentDescription)
        }
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
                .clickable { onCheckChanged(!checked) },
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
                style = HarmTheme.typography.bodyLarge,
                color = HarmTheme.colors.onSurface
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

    /** Circular iconButton */
    @Composable
    fun HarmCircularIconButton(
        onClick: () -> Unit,
        @DrawableRes iconRes: Int?,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        iconBackground: Color = Color(CategoryColors.VIOLET_T68.background),
        iconTint: Color = HarmTheme.colors.borderAndScrim
    ) {
        IconButton(
            modifier = modifier,
            shape = CircleShape,
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = iconBackground,
                contentColor = iconTint
            )
        ) {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = contentDescription,
                    tint = iconTint
                )
            }
        }
    }

    /** Circular iconButton with title */
    @Composable
    fun HarmCircularIconButtonWithTitle(
        @DrawableRes iconRes: Int,
        iconBackground: Color,
        iconTitle: String,
        onClick: () -> Unit,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        iconTint: Color = HarmTheme.colors.borderAndScrim,
        selected: Boolean = false,
    ) {
        Column(
            modifier = modifier
                .then(
                    if (selected) {
                        Modifier.border(
                            width = 1.dp,
                            color = HarmTheme.colors.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HarmCircularIconButton(
                iconRes = iconRes,
                iconBackground = iconBackground,
                iconTint = iconTint,
                onClick = onClick,
                contentDescription = contentDescription,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = iconTitle,
                color = HarmTheme.colors.onSurfaceContainer,
                style = HarmTheme.typography.labelLarge,
                modifier = Modifier.clickable { onClick() },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

    /** Common button */
    @Composable
    fun HarmCommonButton(
        text: String,
        colors: ButtonColors,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(corner = CornerSize(54.dp)),
            enabled = enabled,
            colors = colors
        ) {
            Text(text = text, style = HarmTheme.typography.bodyLargeSemiBold)
        }
    }

    /** Common button which looks like text */
    @Composable
    fun HarmCommonTextButton(
        text: String,
        colors: ButtonColors,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            colors = colors,
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(text = text, style = HarmTheme.typography.bodyLargeSemiBold)
        }
    }

    /** Common button with colors for dangerous logic (for example, delete) */
    @Composable
    fun HarmDangerousButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        HarmCommonButton(
            modifier = modifier,
            text = text,
            enabled = enabled,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = HarmTheme.colors.error,
                contentColor = HarmTheme.colors.onError,
                disabledContainerColor = HarmTheme.colors.surfaceContainerLow,
                disabledContentColor = HarmTheme.colors.onSurfaceContainerLow
            )
        )
    }

    /** Card Dropdown menu icon */
    @Composable
    fun HarmDropdownMenuIcon(
        @DrawableRes iconRes: Int,
        expanded: Boolean,
        menuOptions: ImmutableList<MenuOptions>,
        contentDescription: String?,
        onMenuClick: () -> Unit,
        onMenuDismiss: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HarmMenu.HarmDropdownMenu(
            expanded = expanded,
            menuOptions = menuOptions,
            onDismissRequest = onMenuDismiss,
            modifier = modifier
        ) {
            HarmCardIconButton(
                iconRes = iconRes,
                contentDescription = contentDescription,
                onClick = onMenuClick
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
            Icon(painter = painterResource(iconRes), contentDescription = contentDescription)
        }
    }

    /** Primary button for the main action on a screen */
    @Composable
    fun HarmPrimaryButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        HarmCommonButton(
            modifier = modifier,
            text = text,
            enabled = enabled,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = HarmTheme.colors.primary,
                contentColor = HarmTheme.colors.onPrimary,
                disabledContainerColor = HarmTheme.colors.surfaceContainerLow,
                disabledContentColor = HarmTheme.colors.onSurfaceContainerLow
            )
        )
    }

    /** Primary text button for a low priority action on a screen*/
    @Composable
    fun HarmPrimaryTextButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        HarmCommonTextButton(
            modifier = modifier,
            text = text,
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = HarmTheme.colors.primary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = HarmTheme.colors.onSurfaceContainerLow
            )
        )
    }

    /** Secondary button for a secondary action on a screen */
    @Composable
    fun HarmSecondaryButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        HarmCommonButton(
            modifier = modifier,
            text = text,
            enabled = enabled,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = HarmTheme.colors.secondary,
                contentColor = HarmTheme.colors.onSecondary,
                disabledContainerColor = HarmTheme.colors.surfaceContainerLow,
                disabledContentColor = HarmTheme.colors.onSurfaceContainerLow
            )
        )
    }

    /** Secondary text button for a low priority action on a screen */
    @Composable
    fun HarmSecondaryTextButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        HarmCommonTextButton(
            modifier = modifier,
            text = text,
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = HarmTheme.colors.onSecondaryContainer,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = HarmTheme.colors.onSurfaceContainerLow
            )
        )
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
            onCheckedChange = { onClick() },
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
            Icon(painter = painterResource(iconRes), contentDescription = contentDescription)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCardIconButton_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmCardIconButton(
            iconRes = R.drawable.ic_delete_24px,
            contentDescription = null,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCardIconButton_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmCardIconButton(
            iconRes = R.drawable.ic_delete_24px,
            contentDescription = null,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCheckableIconWithTitle_UncheckedDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmCheckableIconWithTitle(
            title = stringResource(R.string.category_type_expenses_title),
            checked = false,
            onCheckChanged = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCheckableIconWithTitle_CheckedDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmCheckableIconWithTitle(
            title = stringResource(R.string.category_type_expenses_title),
            checked = true,
            onCheckChanged = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCheckableIconWithTitle_UncheckedLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmCheckableIconWithTitle(
            title = stringResource(R.string.category_type_expenses_title),
            checked = false,
            onCheckChanged = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCheckableIconWithTitle_CheckedLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmCheckableIconWithTitle(
            title = stringResource(R.string.category_type_expenses_title),
            checked = true,
            onCheckChanged = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCircularCheckBox_UncheckedDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmCircularCheckBox(checked = false, contentDescription = null)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCircularCheckBox_CheckedDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmCircularCheckBox(checked = true, contentDescription = null)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCircularCheckBox_UncheckedLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmCircularCheckBox(checked = false, contentDescription = null)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCircularCheckBox_CheckedLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmCircularCheckBox(checked = true, contentDescription = null)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCircularIconButton_Preview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmCircularIconButton(
            iconRes = R.drawable.ic_clothes_24px,
            contentDescription = null,
            onClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCircularIconButtonWithTitle_DarkUnselectedPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmCircularIconButtonWithTitle(
            iconRes = R.drawable.ic_transport_3_24px,
            contentDescription = null,
            onClick = {},
            iconTitle = stringResource(R.string.category_icon_sub_type_transport),
            iconBackground = Color(CategoryColors.RED_T53.background),
            selected = false
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCircularIconButtonWithTitle_DarkSelectedPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmCircularIconButtonWithTitle(
            iconRes = R.drawable.ic_transport_3_24px,
            contentDescription = null,
            onClick = {},
            iconTitle = stringResource(R.string.category_icon_sub_type_transport),
            iconBackground = Color(CategoryColors.RED_T53.background),
            selected = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCircularIconButtonWithTitle_LightUnselectedPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmCircularIconButtonWithTitle(
            iconRes = R.drawable.ic_transport_3_24px,
            contentDescription = null,
            onClick = {},
            iconTitle = stringResource(R.string.category_icon_sub_type_transport),
            iconBackground = Color(CategoryColors.RED_T53.background),
            selected = false
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCircularIconButtonWithTitle_LightSelectedPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmCircularIconButtonWithTitle(
            iconRes = R.drawable.ic_transport_3_24px,
            contentDescription = null,
            onClick = {},
            iconTitle = stringResource(R.string.category_icon_sub_type_transport),
            iconBackground = Color(CategoryColors.RED_T53.background),
            selected = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmDangerousButton_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmDangerousButton(
            text = stringResource(R.string.btn_delete_text),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmDangerousButton_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmDangerousButton(
            text = stringResource(R.string.btn_delete_text),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmDropdownMenuIcon_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmDropdownMenuIcon(
            iconRes = R.drawable.ic_menu_24px,
            contentDescription = null,
            menuOptions = persistentListOf(
                MenuOptions(text = stringResource(R.string.ic_edit_desc), onClick = {}),
                MenuOptions(text = stringResource(R.string.ic_delete_desc), onClick = {}),
            ),
            expanded = true,
            onMenuClick = {},
            onMenuDismiss = {}
        )
    }
}

@Preview
@Composable
private fun HarmDropdownMenuIcon_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmDropdownMenuIcon(
            iconRes = R.drawable.ic_menu_24px,
            contentDescription = null,
            menuOptions = persistentListOf(
                MenuOptions(text = stringResource(R.string.ic_edit_desc), onClick = {}),
                MenuOptions(text = stringResource(R.string.ic_delete_desc), onClick = {}),
            ),
            expanded = true,
            onMenuClick = {},
            onMenuDismiss = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmFloatingActionButton_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmFloatingActionButton(
            iconRes = R.drawable.ic_add_24px,
            contentDescription = null,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmFloatingActionButton_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmFloatingActionButton(
            iconRes = R.drawable.ic_add_24px,
            contentDescription = null,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmPrimaryButton_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmPrimaryButton(
            text = stringResource(R.string.btn_save_text),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmPrimaryButton_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmPrimaryButton(
            text = stringResource(R.string.btn_save_text),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmPrimaryText_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmPrimaryTextButton(
            text = stringResource(R.string.btn_save_text),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmPrimaryText_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmPrimaryTextButton(
            text = stringResource(R.string.btn_save_text),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmSecondaryButton_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmSecondaryButton(
            text = stringResource(R.string.btn_dialog_cancel_text),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmSecondaryButton_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmSecondaryButton(
            text = stringResource(R.string.btn_dialog_cancel_text),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmSecondaryText_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmSecondaryTextButton(
            text = stringResource(R.string.btn_dialog_cancel_text),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmSecondaryText_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmSecondaryTextButton(
            text = stringResource(R.string.btn_dialog_cancel_text),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmSwitch_UncheckedDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmSwitch(isChecked = false, onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmSwitch_CheckedDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmSwitch(isChecked = true, onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmSwitch_UncheckedLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmSwitch(isChecked = false, onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmSwitch_CheckedLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmSwitch(isChecked = true, onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmTopBarIconButton_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmButton.HarmTopBarIconButton(
            iconRes = R.drawable.ic_arrow_back_24px,
            contentDescription = null,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmTopBarIconButton_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmButton.HarmTopBarIconButton(
            iconRes = R.drawable.ic_arrow_back_24px,
            contentDescription = null,
            onClick = {}
        )
    }
}
