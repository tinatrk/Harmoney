package com.example.harmoney.core.uilibrary.other

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.presentation.models.AccountScopeUi
import com.example.harmoney.ui.theme.HarmTheme

/** Show accountScopeUi as list item (for example, in dropdown menu) */
@UiLibrary
@Composable
fun HarmAccountScopeContent(
    scope: AccountScopeUi,
    modifier: Modifier = Modifier,
    textColor: Color? = null,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .background(Color.Unspecified)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = scope.name,
            style = HarmTheme.typography.titleMedium,
            color = textColor ?: Color.Unspecified,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.title_balance) + " " + scope.balance,
            style = HarmTheme.typography.bodyLarge,
            color = textColor ?: Color.Unspecified,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        if (scope.balanceOriginals != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = scope.balanceOriginals,
                style = HarmTheme.typography.bodyMedium,
                color = textColor ?: Color.Unspecified,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (scope.isArchivedMark) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier
                    .background(HarmTheme.colors.info)
                    .padding(2.dp),
                text = stringResource(R.string.title_marked_as_archived),
                style = HarmTheme.typography.bodySmall,
                color = HarmTheme.colors.onInfo,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Show title and optional subtitle with clickable ending icon */
@UiLibrary
@Composable
fun HarmTitleWithEndIcon(
    title: String,
    @DrawableRes iconRes: Int,
    iconContentDescription: String,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = title,
                style = HarmTheme.typography.titleLarge,
                color = HarmTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onIconClick() },
                painter = painterResource(iconRes),
                contentDescription = iconContentDescription,
                tint = HarmTheme.colors.onSurface
            )
        }

        if (subTitle != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subTitle,
                style = HarmTheme.typography.titleMedium,
                color = HarmTheme.colors.onSurface
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmAccountScopeContent_WithSimpleAccount_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmAccountScopeContent(
            scope = AccountScopeUi(
                id = 0L,
                name = "Основная карта",
                balance = "30 000 ₽",
                balanceOriginals = null,
                isArchivedMark = false
            ),
            // Когда этот элемент будет использоваться в качестве body MenuOption и передаваться в
            // HarmMenu.HarmDropDownMenu, цвет текста установится автоматически
            textColor = HarmTheme.colors.onSurface
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmAccountScopeContent_WithSimpleAccount_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmAccountScopeContent(
            scope = AccountScopeUi(
                id = 0L,
                name = "Основная карта",
                balance = "30 000 ₽",
                balanceOriginals = null,
                isArchivedMark = false
            ),
            // Когда этот элемент будет использоваться в качестве body MenuOption и передаваться в
            // HarmMenu.HarmDropDownMenu, цвет текста установится автоматически
            textColor = HarmTheme.colors.onSurface
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmAccountScopeContent_WithMarkedAccount_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmAccountScopeContent(
            scope = AccountScopeUi(
                id = 0L,
                name = "Иностранная карта",
                balance = "1 000 $",
                balanceOriginals = null,
                isArchivedMark = true
            ),
            // Когда этот элемент будет использоваться в качестве body MenuOption и передаваться в
            // HarmMenu.HarmDropDownMenu, цвет текста установится автоматически
            textColor = HarmTheme.colors.onSurface
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmAccountScopeContent_WithMarkedAccount_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmAccountScopeContent(
            scope = AccountScopeUi(
                id = 0L,
                name = "Иностранная карта",
                balance = "1 000 $",
                balanceOriginals = null,
                isArchivedMark = true
            ),
            // Когда этот элемент будет использоваться в качестве body MenuOption и передаваться в
            // HarmMenu.HarmDropDownMenu, цвет текста установится автоматически
            textColor = HarmTheme.colors.onSurface
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmAccountScopeContent_WithConversion_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmAccountScopeContent(
            scope = AccountScopeUi(
                id = 0L,
                name = "Все счета",
                balance = "40 000 ₽",
                balanceOriginals = "30 000 ₽ = 30 000 ₽\n" + "1000 $ = 10 000 ₽",
                isArchivedMark = false
            ),
            // Когда этот элемент будет использоваться в качестве body MenuOption и передаваться в
            // HarmMenu.HarmDropDownMenu, цвет текста установится автоматически
            textColor = HarmTheme.colors.onSurface
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmAccountScopeContent_WithConversion_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmAccountScopeContent(
            scope = AccountScopeUi(
                id = 0L,
                name = "Все счета",
                balance = "40 000 ₽",
                balanceOriginals = "30 000 ₽ = 30 000 ₽\n" + "1000 $ = 10 000 ₽",
                isArchivedMark = false
            ),
            // Когда этот элемент будет использоваться в качестве body MenuOption и передаваться в
            // HarmMenu.HarmDropDownMenu, цвет текста установится автоматически
            textColor = HarmTheme.colors.onSurface
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmTitleWithEndIcon_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTitleWithEndIcon(
            title = "30 000 ₽",
            subTitle = "Основная карта",
            iconRes = R.drawable.ic_menu_arrow_down_24,
            iconContentDescription = "",
            onIconClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmTitleWithEndIcon_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTitleWithEndIcon(
            title = "30 000 ₽",
            subTitle = "Основная карта",
            iconRes = R.drawable.ic_menu_arrow_down_24,
            iconContentDescription = "",
            onIconClick = {}
        )
    }
}
