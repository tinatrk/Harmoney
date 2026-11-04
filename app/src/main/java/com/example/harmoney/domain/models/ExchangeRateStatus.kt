package com.example.harmoney.domain.models

import java.math.BigDecimal
import java.time.LocalDate

sealed interface ExchangeRateStatus {
    val source: Currency
    val target: Currency

    data class Fresh(
        override val source: Currency,
        override val target: Currency,
        val rateValue: BigDecimal,
    ) : ExchangeRateStatus

    data class Stale(
        override val source: Currency,
        override val target: Currency,
        val rateValue: BigDecimal,
        val date: LocalDate,
    ) : ExchangeRateStatus

    data class NotRequired(
        override val source: Currency,
        override val target: Currency
    ): ExchangeRateStatus

    data class Unsupported(
        override val source: Currency,
        override val target: Currency,
    ) : ExchangeRateStatus
}
