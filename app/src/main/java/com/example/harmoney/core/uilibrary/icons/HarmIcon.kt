package com.example.harmoney.core.uilibrary.icons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmCircularCategoryIcon` - Not clickable circular category icon without title
 */
object HarmIcon {
    /** Not clickable circular category icon without title */
    @Composable
    fun HarmCircularCategoryIcon(
        backgroundColorValue: Long,
        @DrawableRes iconRes: Int,
        contentDescription: String?,
        modifier: Modifier = Modifier,
    ) {
        Icon(
            modifier = modifier
                .background(
                    color = Color(backgroundColorValue),
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    shape = CircleShape,
                    color = HarmTheme.colors.borderAndScrim
                )
                .padding(8.dp)
                .size(24.dp),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = HarmTheme.colors.borderAndScrim
        )
    }
}
