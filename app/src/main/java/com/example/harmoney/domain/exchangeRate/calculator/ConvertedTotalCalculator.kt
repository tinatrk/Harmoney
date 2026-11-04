package com.example.harmoney.domain.exchangeRate.calculator

import com.example.harmoney.domain.models.ConvertedMoney
import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRateResult
import com.example.harmoney.domain.models.Money
import com.example.harmoney.domain.models.MoneyAmount

class ConvertedTotalCalculator {
    fun calculate(
        moneyList: List<Money>,
        rates: Set<ExchangeRateResult>,
        targetCurrency: Currency
    ): ConvertedTotal {

        val includedMoney: MutableList<ConvertedMoney> = mutableListOf()
        val excludedMoney: MutableList<Money> = mutableListOf()

        moneyList.forEach { money ->
            val rate =
                rates.find {
                    it.source.id == money.currency.id && it.target.id == targetCurrency.id
                }
            when (rate) {
                null -> excludedMoney.add(money)
                is ExchangeRateResult.NotAvailable -> excludedMoney.add(money)
                is ExchangeRateResult.NotRequired -> includedMoney
                    .add(ConvertedMoney(original = money, converted = money))

                is ExchangeRateResult.Available -> {
                    val converted =
                        money.amount.minorUnits.toBigDecimal().multiply(rate.rate.rateValue)
                    includedMoney.add(
                        ConvertedMoney(
                            original = money,
                            converted = Money(
                                amount = MoneyAmount(converted.toLong()),
                                currency = rate.target
                            )
                        )
                    )
                }
            }
        }

        val totalMinorUnits = includedMoney.sumOf { it.converted.amount.minorUnits }

        return ConvertedTotal(
            total = Money(amount = MoneyAmount(totalMinorUnits), currency = targetCurrency),
            includedAmounts = includedMoney,
            excludedAmounts = excludedMoney,
        )
    }
}
