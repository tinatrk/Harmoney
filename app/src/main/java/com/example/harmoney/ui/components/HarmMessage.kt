package com.example.harmoney.ui.components

import androidx.annotation.DrawableRes
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
fun HarmMessage(
    modifier: Modifier = Modifier,
    message: String,
    @DrawableRes imageRes: Int? = null,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageRes != null) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(imageRes),
                contentDescription = message
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 44.dp),
            text = message,
            style = HarmTheme.typography.titleMediumSemiBold,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
fun HarmMessage_Empty_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmMessage(
            modifier = Modifier.fillMaxSize(),
            message = stringResource(R.string.placeholder_empty_screen),
            imageRes = R.drawable.im_empty_screen
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
fun HarmMessage_Empty_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmMessage(
            modifier = Modifier.fillMaxSize(),
            message = stringResource(R.string.placeholder_empty_screen),
            imageRes = R.drawable.im_empty_screen
        )
    }
}
@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
fun HarmMessage_Error_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmMessage(
            modifier = Modifier.fillMaxSize(),
            message = "Отсутствует интернет соединение",
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
fun HarmMessage_Error_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmMessage(
            modifier = Modifier.fillMaxSize(),
            message = "Отсутствует интернет соединение",
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
fun HarmMessage_ErrorWithImage_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmMessage(
            modifier = Modifier.fillMaxSize(),
            message = "Отсутствует интернет соединение",
            imageRes = R.drawable.im_empty_screen
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
fun HarmMessage_ErrorWithImage_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmMessage(
            modifier = Modifier.fillMaxSize(),
            message = "Отсутствует интернет соединение",
            imageRes = R.drawable.im_empty_screen
        )
    }
}

