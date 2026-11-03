package com.example.harmoney.domain.converter

import com.example.harmoney.domain.models.ExchangeRateResult
import com.example.harmoney.domain.models.ExchangeRateStatus
import java.time.LocalDate

class ExchangeRateResultConverterImpl : ExchangeRateResultConverter {
    override fun map(result: ExchangeRateResult): ExchangeRateStatus {
        return when (result) {
            is ExchangeRateResult.NotRequired -> ExchangeRateStatus.NotRequired(
                source = result.source,
                target = result.target,
            )

            is ExchangeRateResult.NotAvailable -> ExchangeRateStatus.Unsupported(
                source = result.source,
                target = result.target,
            )

            is ExchangeRateResult.Available -> {
                val now = LocalDate.now().toEpochDay()

                if (result.rate.rateDate.toEpochDay() < now) {
                    ExchangeRateStatus.Stale(
                        source = result.source,
                        target = result.target,
                        rateValue = result.rate.rateValue,
                        date = result.rate.rateDate,
                    )
                } else {
                    ExchangeRateStatus.Fresh(
                        source = result.source,
                        target = result.target,
                        rateValue = result.rate.rateValue,
                    )
                }
            }
        }
    }

    override fun map(results: Set<ExchangeRateResult>): Set<ExchangeRateStatus> {
        return results.map { map(it) }.toSet()
    }
}
