package com.example.harmoney.presentation.converters

import com.example.harmoney.R
import com.example.harmoney.core.util.StringProvider
import com.example.harmoney.domain.models.ConvertedMoney
import com.example.harmoney.domain.models.ConvertedTotal

class ConvertedTotalFormatterImpl(
    private val numbersFormatter: NumbersFormatter,
    private val stringProvider: StringProvider,
) : ConvertedTotalFormatter {
    override fun getOriginalsStr(total: ConvertedTotal): String {
        return buildString {
            append(
                total.includedAmounts.joinToString(MONEY_LIST_SEPARATOR) {
                    getConvertedMoneyStr(it)
                }
            )

            if (total.excludedAmounts.isNotEmpty()) {
                if (isNotEmpty()) append(BLOCK_SEPARATOR)

                append(stringProvider.getString(R.string.title_excluded_amounts))
                append(TITLE_SEPARATOR)

                append(
                    total.excludedAmounts.joinToString(MONEY_LIST_SEPARATOR) {
                        numbersFormatter.moneyToStringWithCurrency(it)
                    }
                )
            }
        }
    }

    private fun getConvertedMoneyStr(money: ConvertedMoney): String {
        val originalStr = numbersFormatter
            .moneyToStringWithCurrency(money.original)
        val convertedStr = numbersFormatter
            .moneyToStringWithCurrency(money.converted)
        return buildString {
            append(originalStr)
            append(CONVERTED_MONEY_SEPARATOR)
            append(convertedStr)
        }
    }

    companion object {
        private const val CONVERTED_MONEY_SEPARATOR = " = "
        private const val MONEY_LIST_SEPARATOR = "\n"
        private const val BLOCK_SEPARATOR = "\n\n"
        private const val TITLE_SEPARATOR = " "
    }
}
