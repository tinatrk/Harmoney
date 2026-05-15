package com.example.harmoney.core.uilibrary.icons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmCircularCategoryIcon` - Not clickable circular category icon without title
 */
@UiLibrary
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
                .padding(8.dp)
                .size(24.dp),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = HarmTheme.colors.borderAndScrim
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmCircularCategoryIcon_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmIcon.HarmCircularCategoryIcon(
            backgroundColorValue = CategoryColors.PINK_T75.background,
            iconRes = R.drawable.ic_shop_cart_24px,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmCircularCategoryIcon_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmIcon.HarmCircularCategoryIcon(
            backgroundColorValue = CategoryColors.PINK_T75.background,
            iconRes = R.drawable.ic_shop_cart_24px,
            contentDescription = null
        )
    }
}
