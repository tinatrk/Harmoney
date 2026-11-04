package com.example.harmoney.presentation.converters

import com.example.harmoney.R
import com.example.harmoney.core.util.StringProvider
import com.example.harmoney.domain.models.ExchangeRateStatus

class ExchangeRateFormatterImpl(
    private val stringProvider: StringProvider,
    private val dateFormatter: DateFormatter,
    private val numbersFormatter: NumbersFormatter,
) : ExchangeRateFormatter {
    override fun map(rates: Set<ExchangeRateStatus>): String? {
        if (rates.isEmpty()) return null

        val fresh = mutableListOf<String>()
        val stale = mutableListOf<String>()
        val unsupported = mutableListOf<String>()

        rates.forEach { status ->
            when (status) {
                is ExchangeRateStatus.Fresh -> fresh += formatFresh(status)
                is ExchangeRateStatus.Stale -> stale += formatStale(status)
                is ExchangeRateStatus.Unsupported -> unsupported += formatUnsupported(status)
                is ExchangeRateStatus.NotRequired -> Unit
            }
        }

        val lines = buildList {
            if (fresh.isNotEmpty()) {
                add(stringProvider.getString(R.string.title_exchange_rate_fresh_status))
                add(fresh.joinToString(EXCHANGE_RATE_LIST_ITEMS_SEPARATOR))
                add(EMPTY_STRING) // чтобы в конце блока был SEPARATOR
            }

            if (stale.isNotEmpty()) {
                add(stringProvider.getString(R.string.title_exchange_rate_stale_status))
                add(stale.joinToString(EXCHANGE_RATE_LIST_ITEMS_SEPARATOR))
                add(EMPTY_STRING) // чтобы в конце блока был SEPARATOR
            }

            if (unsupported.isNotEmpty()) {
                add(stringProvider.getString(R.string.title_exchange_rate_unsupported_status))
                add(unsupported.joinToString(EXCHANGE_RATE_LIST_ITEMS_SEPARATOR))
                add(EMPTY_STRING) // чтобы в конце блока был SEPARATOR
            }
        }.dropLast(1)

        return lines.takeIf { it.isNotEmpty() }?.joinToString(EXCHANGE_RATE_LINE_SEPARATOR)
    }

    private fun formatFresh(rate: ExchangeRateStatus.Fresh): String = stringProvider.getString(
        R.string.exchange_rate_fresh_value,
        rate.source.symbol,
        numbersFormatter.numberToString(rate.rateValue, EXCHANGE_RATE_SCALE),
        rate.target.symbol
    )

    private fun formatStale(rate: ExchangeRateStatus.Stale): String = stringProvider.getString(
        R.string.exchange_rate_stale_value,
        rate.source.symbol,
        numbersFormatter.numberToString(rate.rateValue, EXCHANGE_RATE_SCALE),
        rate.target.symbol,
        dateFormatter.formatDate(rate.date)
    )

    private fun formatUnsupported(rate: ExchangeRateStatus.Unsupported): String =
        stringProvider.getString(
            R.string.exchange_rate_unsupported_value,
            rate.source.symbol,
            rate.target.symbol
        )

    companion object {
        private const val EXCHANGE_RATE_LIST_ITEMS_SEPARATOR = "\n"
        private const val EXCHANGE_RATE_LINE_SEPARATOR = "\n"
        private const val EMPTY_STRING = ""
        private const val EXCHANGE_RATE_SCALE = 3
    }
}
