package com.example.harmoney.core.uilibrary.other

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmInfoPopup` - Shows text (`popupSourceText`) with arrow-down icon.
 * When you click on the text appears popup with another text (`info`)
 * */
@UiLibrary
object HarmPopup {
    /**  Shows text (`popupSourceText`) with arrow-down icon.
     * When you click on the text appears popup with another text (`info`)
     * */
    @Composable
    fun HarmInfoPopup(
        info: String,
        popupSourceText: String,
        modifier: Modifier = Modifier,
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier
                .background(Color.Unspecified)
                .clickable { expanded = !expanded },
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f, fill = false),
                    text = popupSourceText,
                    style = HarmTheme.typography.bodyLarge,
                    color = HarmTheme.colors.onSurface,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_menu_arrow_down_24),
                    contentDescription = stringResource(R.string.ic_info_arrow_down_desc),
                    tint = HarmTheme.colors.onSurface
                )
            }

            if (expanded) {
                Popup(
                    alignment = Alignment.BottomStart,
                    offset = IntOffset(0, 8),
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(focusable = true)
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = HarmTheme.colors.surfaceContainerHigh,
                            contentColor = HarmTheme.colors.onSurface,
                        )
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(12.dp),
                            text = info,
                            style = HarmTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmInfoPopup_DarkPreview() {
    HarmTheme(darkTheme = true) {
        Currency.EUR
        HarmPopup.HarmInfoPopup(
            info = PreviewData.getExchangeRateStatus(),
            popupSourceText = stringResource(R.string.title_exchange_rate_status)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmInfoPopup_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmPopup.HarmInfoPopup(
            info = PreviewData.getExchangeRateStatus(),
            popupSourceText = stringResource(R.string.title_exchange_rate_status)
        )
    }
}
