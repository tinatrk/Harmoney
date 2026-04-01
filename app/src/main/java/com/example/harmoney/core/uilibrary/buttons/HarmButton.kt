package com.example.harmoney.core.uilibrary.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.presentation.category.models.CategoryColors
import com.example.harmoney.ui.theme.HarmColor
import com.example.harmoney.ui.theme.HarmTheme

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
        iconBackground: Color = CategoryColors.CYAN_T88.color,
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
                    .background(iconBackground)
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
                        tint = HarmColor.NeutralT0
                    )
                }
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
        selected: Boolean = false,
    ) {
        Column(
            modifier = modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HarmCircularIconButton(
                iconRes = iconRes,
                iconBackground = iconBackground,
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

    /** Common button */
    @Composable
    fun HarmCommonButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            shape = RoundedCornerShape(corner = CornerSize(54.dp)),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = HarmTheme.colors.primary,
                contentColor = HarmTheme.colors.onPrimary,
                disabledContainerColor = HarmTheme.colors.surfaceVariant,
                disabledContentColor = HarmTheme.colors.onSurfaceVariant
            )
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
                contentColor = HarmTheme.colors.onSurfaceContainerLow,
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
}
