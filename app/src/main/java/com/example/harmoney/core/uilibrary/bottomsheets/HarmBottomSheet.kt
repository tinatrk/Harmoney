package com.example.harmoney.core.uilibrary.bottomsheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.ui.theme.HarmTheme
import it.lucf15.compose.bottomsheet.ModalBottomSheet
import it.lucf15.compose.bottomsheet.rememberModalBottomSheetState

@OptIn(ExperimentalMaterial3Api::class)
@UiLibrary
@Composable
fun HarmBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    isNeedScrim: Boolean = true,
    skipPartiallyExpanded: Boolean = false,
    nestedScrollableState: ScrollableState? = null,
    content: @Composable (() -> Unit)
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        nestedScrollableState = nestedScrollableState,
        shape = RoundedCornerShape(32.dp),
        tonalElevation = 6.dp,
        containerColor = HarmTheme.colors.surfaceContainerLow,
        contentColor = HarmTheme.colors.onSurfaceVariant,
        scrimColor = if (isNeedScrim) {
            HarmTheme.colors.borderAndScrim.copy(alpha = .32f)
        } else {
            Color.Transparent
        },
        dragHandle = {
            BottomSheetDefaults
                .DragHandle(color = HarmTheme.colors.onSurfaceContainerLow)
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .wrapContentHeight(Alignment.Top)
            ) {
                content()
            }

        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun HarmBottomSheetDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmBottomSheet(
            onDismissRequest = {},
        ) {
            // тестовый контент
            Image(
                painter = painterResource(R.drawable.im_empty_screen),
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun HarmBottomSheetLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmBottomSheet(
            onDismissRequest = {},
        ) {
            // тестовый контент
            Image(
                painter = painterResource(R.drawable.im_empty_screen),
                contentDescription = null
            )
        }
    }
}
