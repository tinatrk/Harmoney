package com.example.harmoney.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.textfields.HarmTextField
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.presentation.currency.models.CurrencyAction
import com.example.harmoney.presentation.currency.models.CurrencyEvent
import com.example.harmoney.presentation.currency.models.CurrencyState
import com.example.harmoney.presentation.currency.viewModel.CurrencyViewModel
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.action.flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    is CurrencyAction.NavigateBack -> onBackClick()
                    is CurrencyAction.SetCurrencyError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_set_reporting_currency_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    null -> Unit
                }
            }
    }

    CurrencyScreen(
        state = state,
        onEvent = viewModel::obtainEvent
    )
}

@Composable
fun CurrencyScreen(
    state: CurrencyState,
    onEvent: (CurrencyEvent) -> Unit
) {
    Scaffold(
        topBar = {
            HarmTopBar.HarmSimpleTopBar(
                title = stringResource(R.string.title_top_app_bar_currency),
                navigationIconRes = R.drawable.ic_arrow_back_24px,
                navigationIconDesc = stringResource(R.string.ic_arrow_back_desc),
                onNavigationIconClick = { onEvent(CurrencyEvent.OnBackClick) }
            )
        },
        containerColor = HarmTheme.colors.surface
    ) { paddingValues ->
        CurrencyContent(
            state = state,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
fun CurrencyContent(
    state: CurrencyState,
    onEvent: (CurrencyEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(R.string.title_currency_screen),
            style = HarmTheme.typography.bodyLarge,
            color = HarmTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmTextField.HarmBaseTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = state.searchText,
            placeholder = stringResource(R.string.label_text_field_currency),
            label = stringResource(R.string.label_text_field_currency),
            onValueChange = { newValue ->
                onEvent(CurrencyEvent.OnSearchTextChanged(newValue))
            },
            focusManager = focusManager,
            onDoneAction = { onEvent(CurrencyEvent.OnSearchStartClick) },
            trailingIcon = {
                if (state.searchText.isEmpty()) {
                    HarmButton.HarmCardIconButton(
                        iconRes = R.drawable.ic_search_24px,
                        contentDescription = stringResource(R.string.ic_search_line_desc),
                        onClick = {}
                    )
                } else {
                    HarmButton.HarmCardIconButton(
                        iconRes = R.drawable.ic_clear_24px,
                        contentDescription = stringResource(R.string.ic_clear_search_line_desc),
                        onClick = { onEvent(CurrencyEvent.OnClearSearchLineClick) }
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (state.currencyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.placeholder_nothing_found),
                    style = HarmTheme.typography.bodyLarge,
                    color = HarmTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(items = state.currencyList) { currency ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                onEvent(
                                    CurrencyEvent.OnCurrencyClick(currency)
                                )
                            })
                            .background(
                                if (state.selectedCurrencyId == currency.currency.id) {
                                    HarmTheme.colors.surfaceContainerHighest
                                } else {
                                    HarmTheme.colors.surface
                                }
                            )
                    ) {

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f),
                                text = currency.name,
                                style = HarmTheme.typography.bodyLarge,
                                color = HarmTheme.colors.onSurface
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                modifier = Modifier,
                                text = currency.currency.code,
                                style = HarmTheme.typography.bodyLarge,
                                color = HarmTheme.colors.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(HarmTheme.colors.outlineVariant)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun CurrencyScreen_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CurrencyScreen(
            onEvent = {},
            state = CurrencyState(
                searchText = "",
                currencyList = PreviewData.getFilteredCurrencyList(query = ""),
                selectedCurrencyId = 117L
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CurrencyScreen_LightPreview() {
    HarmTheme(darkTheme = false) {
        CurrencyScreen(
            onEvent = {},
            state = CurrencyState(
                searchText = "",
                currencyList = PreviewData.getFilteredCurrencyList(query = ""),
                selectedCurrencyId = 117L
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CurrencyScreen_Search_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CurrencyScreen(
            onEvent = {},
            state = CurrencyState(
                searchText = "дол",
                currencyList = PreviewData.getFilteredCurrencyList(query = "дол"),
                selectedCurrencyId = 117L
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CurrencyScreen_Search_LightPreview() {
    HarmTheme(darkTheme = false) {
        CurrencyScreen(
            onEvent = {},
            state = CurrencyState(
                searchText = "дол",
                currencyList = PreviewData.getFilteredCurrencyList(query = "дол"),
                selectedCurrencyId = 117L
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CurrencyScreen_NotFound_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CurrencyScreen(
            onEvent = {},
            state = CurrencyState(
                searchText = "клмн",
                currencyList = PreviewData.getFilteredCurrencyList(query = "клмн"),
                selectedCurrencyId = 117L
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CurrencyScreen_NotFound_LightPreview() {
    HarmTheme(darkTheme = false) {
        CurrencyScreen(
            onEvent = {},
            state = CurrencyState(
                searchText = "клмн",
                currencyList = PreviewData.getFilteredCurrencyList(query = "клмн"),
                selectedCurrencyId = 117L
            )
        )
    }
}
