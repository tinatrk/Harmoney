package com.example.harmoney.core.uilibrary.others

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.harmoney.presentation.models.StatisticPeriod
import com.example.harmoney.ui.theme.HarmTheme

object HarmOther {
    @Composable
    fun HarmStatisticPeriodList(
        data: String,
        periods: List<StatisticPeriod>,
        selectedPeriodId: Long,
        onPeriodClick: (Long) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            periods.forEach { period ->
                Text(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clickable { onPeriodClick(period.id) },
                    text = stringResource(period.textRes),
                    style = if (period.id == selectedPeriodId) {
                        HarmTheme.typography.titleSmallSemiBold
                    } else {
                        HarmTheme.typography.titleSmall
                    },
                    color = if (period.id == selectedPeriodId) {
                        HarmTheme.colors.primary
                    } else {
                        HarmTheme.colors.onSurfaceContainer
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = data,
                style = HarmTheme.typography.titleSmall,
                color = HarmTheme.colors.onSurface
            )
        }
    }
}
