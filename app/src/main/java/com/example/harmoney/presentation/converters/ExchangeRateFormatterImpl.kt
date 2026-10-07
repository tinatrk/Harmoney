package com.example.harmoney.presentation.converters

import com.example.harmoney.R
import com.example.harmoney.core.util.StringProvider
import com.example.harmoney.domain.models.ExchangeRateStatus

class ExchangeRateFormatterImpl(
    private val stringProvider: StringProvider,
    private val dateFormatter: DateFormatter,
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

        return buildList {
            if (fresh.isNotEmpty()) {
                add(
                    stringProvider.getString(
                        R.string.exchange_rate_fresh_status,
                        fresh.joinToString(EXCHANGE_RATE_LIST_SEPARATOR)
                    )
                )
            }

            if (stale.isNotEmpty()) {
                add(
                    stringProvider.getString(
                        R.string.exchange_rate_stale_status,
                        stale.joinToString(EXCHANGE_RATE_LIST_SEPARATOR)
                    )
                )
            }

            if (unsupported.isNotEmpty()) {
                add(
                    stringProvider.getString(
                        R.string.exchange_rate_unsupported_status,
                        unsupported.joinToString(EXCHANGE_RATE_LIST_SEPARATOR)
                    )
                )
            }
        }.joinToString(EXCHANGE_RATE_BLOCK_SEPARATOR)
    }

    private fun formatFresh(rate: ExchangeRateStatus.Fresh): String = stringProvider.getString(
        R.string.exchange_rate_fresh_value,
        rate.source.symbol,
        rate.rate.toString(),
        rate.target.symbol
    )

    private fun formatStale(rate: ExchangeRateStatus.Stale): String = stringProvider.getString(
        R.string.exchange_rate_stale_value,
        rate.source.symbol,
        rate.rate.toString(),
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
        private const val EXCHANGE_RATE_LIST_SEPARATOR = ", "
        private const val EXCHANGE_RATE_BLOCK_SEPARATOR = "\n"
    }
}
