package com.example.harmoney.domain.settings.period.impl

import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.models.StatisticsPeriodType
import java.time.LocalDate
import java.time.YearMonth

class StatisticsPeriodCalculator {
    fun createPeriods(firstDayMonth: Int): List<StatisticsPeriod> {
        val now = YearMonth.now()
        val prev = now.minusMonths(1)
        val next = now.plusMonths(1)

        return StatisticsPeriodType.entries.map { periodType ->
            when (periodType) {
                StatisticsPeriodType.CURRENT_MONTH -> {
                    getPeriod(
                        firstDayMonth = firstDayMonth,
                        lastDayMonth = now.lengthOfMonth(),
                        now = now,
                        next = next,
                        periodType = periodType
                    )
                }

                StatisticsPeriodType.LAST_MONTH -> {
                    getPeriod(
                        firstDayMonth = firstDayMonth,
                        lastDayMonth = prev.lengthOfMonth(),
                        now = prev,
                        next = now,
                        periodType = periodType
                    )
                }
            }
        }
    }

    private fun getPeriod(
        firstDayMonth: Int,
        lastDayMonth: Int,
        now: YearMonth,
        next: YearMonth,
        periodType: StatisticsPeriodType
    ): StatisticsPeriod {
        val firstDate = LocalDate.of(now.year, now.monthValue, firstDayMonth)
        val lastDate = if (firstDayMonth == DEFAULT_FIRST_DAY_MONTH) {
            LocalDate.of(now.year, now.monthValue, lastDayMonth)
        } else {
            LocalDate.of(next.year, next.monthValue, firstDayMonth - 1)
        }
        return StatisticsPeriod(
            type = periodType, firstDay = firstDate, lastDay = lastDate
        )
    }

    companion object {
        private const val DEFAULT_FIRST_DAY_MONTH = 1
    }
}
