package com.example.harmoney.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.drawable.im_empty_screen),
            contentDescription = stringResource(R.string.placeholder_empty_transaction_list)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 44.dp),
            text = stringResource(R.string.placeholder_empty_transaction_list),
            style = HarmTheme.typography.titleMediumSemiBold,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
fun EmptyScreen_DarkPreview() {
    HarmTheme(darkTheme = true) { EmptyScreen() }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
fun EmptyScreen_LightPreview() {
    HarmTheme(darkTheme = false) { EmptyScreen() }
}
