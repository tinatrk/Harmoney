package com.example.harmoney.core.uilibrary.progressbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.ui.theme.HarmTheme

@UiLibrary
@Composable
fun HarmProgressBar(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(24.dp)
                .size(36.dp),
            color = HarmTheme.colors.surface,
            strokeWidth = 4.dp,
            trackColor = HarmTheme.colors.primary
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
fun HarmProgressBar_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmProgressBar(modifier = Modifier.fillMaxSize())
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
fun HarmProgressBar_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmProgressBar(modifier = Modifier.fillMaxSize())
    }
}
