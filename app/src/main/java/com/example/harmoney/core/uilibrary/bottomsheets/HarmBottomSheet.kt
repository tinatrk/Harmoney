package com.example.harmoney.core.uilibrary.bottomsheets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.ui.theme.HarmTheme

@UiLibrary
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean = false,
    content: @Composable (ColumnScope.() -> Unit)
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = RoundedCornerShape(32.dp),
            tonalElevation = 6.dp,
            containerColor = HarmTheme.colors.surfaceContainerLow,
            contentColor = HarmTheme.colors.onSurfaceVariant,
            scrimColor = Color.Transparent,
            dragHandle = {
                BottomSheetDefaults
                    .DragHandle(color = HarmTheme.colors.onSurfaceContainerLow)
            },
            content = content
        )
    }
}
