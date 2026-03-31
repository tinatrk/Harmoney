package com.example.harmoney.core.uilibrary.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
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
                contentColor = HarmTheme.colors.onSurfaceContainer
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
        iconBackground: Color = CategoryColors.CyanT88.color,
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

    /** Circular iconButton with title*/
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
}
