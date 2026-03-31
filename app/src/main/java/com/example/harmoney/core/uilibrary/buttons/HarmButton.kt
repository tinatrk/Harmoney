package com.example.harmoney.core.uilibrary.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.harmoney.ui.theme.HarmTheme

object HarmButton {
    /** TopBar IconButton*/
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
}
