package com.example.harmoney.data.exchangeRate

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.api.repository.ExchangeRateRepository
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRate
import com.example.harmoney.domain.models.ExchangeRateResult
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class ExchangeRateRepositoryImpl(
    private val exchangeRateProvider: ExchangeRateProvider,
) : ExchangeRateRepository {

    private val baseCurrency = Currency.EUR

    override suspend fun getRates(
        sources: Set<Currency>,
        target: Currency,
    ): Resource<List<ExchangeRateResult>, ExchangeRateFailure> {

        if (sources.isEmpty()) return Resource.Success(emptyList())

        val currenciesToFetch = if (sources.all { it.id == target.id }) {
            emptySet()
        } else {
            (sources + target).filterTo(mutableSetOf()) {
                it.id != baseCurrency.id
            }
        }

        val resourceRates = if (currenciesToFetch.isEmpty()) {
            Resource.Success(emptyList())
        } else {
            exchangeRateProvider.getAvailableRatesFromBaseToQuote(
                baseCurrency, currenciesToFetch
            )
        }

        return when (resourceRates) {
            is Resource.Error -> Resource.Error(resourceRates.error)
            is Resource.Success -> {
                val ratesFromBaseToAll = resourceRates.data
                val rates = sources.map { source ->
                    when {
                        // это условие идет строго первым, иначе некорректно обработается случай
                        // source.id = target.id = baseCurrency.id -> rate not required
                        source.id == target.id -> {
                            ExchangeRateResult.NotRequired(source, target)
                        }

                        source.id == baseCurrency.id -> {
                            getBaseToTargetRate(target, ratesFromBaseToAll)
                        }

                        target.id == baseCurrency.id -> {
                            getSourceToBaseRate(source, ratesFromBaseToAll)
                        }

                        else -> {
                            getCrossRate(source, target, ratesFromBaseToAll)
                        }
                    }
                }
                Resource.Success(rates)
            }
        }
    }

    /** Обязательное условие `source` == `baseCurrency` && `target` != `baseCurrency`
     *
     * В противном случае будет возвращенно некорректное значение для случая
     * `source` == `baseCurrency` && `target` == `baseCurrency`
     * (вернется NotAvailable вместо NotRequired)
     */
    private fun getBaseToTargetRate(
        target: Currency,
        ratesFromBaseToAll: List<ExchangeRate>
    ): ExchangeRateResult {

        require(target.id != baseCurrency.id) {
            "Target currency must differ from base currency"
        }

        val fromBaseToTarget = getRateFromBaseToCurrency(currency = target, ratesFromBaseToAll)
            ?: return ExchangeRateResult.NotAvailable(baseCurrency, target)

        return ExchangeRateResult.Available(
            ExchangeRate(
                sourceCurrency = baseCurrency,
                targetCurrency = target,
                rateValue = fromBaseToTarget.rateValue,
                rateDate = fromBaseToTarget.rateDate,
            )
        )
    }

    /** Обязательное условие  `source` != `baseCurrency` && `target` == `baseCurrency`
     *
     * В противном случае будет возвращенно некорректное значение для случая
     * `source` == `baseCurrency` && `target` == `baseCurrency`
     *  (вернется NotAvailable вместо NotRequired)
     */
    private fun getSourceToBaseRate(
        source: Currency,
        ratesFromBaseToAll: List<ExchangeRate>
    ): ExchangeRateResult {
        require(source.id != baseCurrency.id) {
            "Source currency must differ from base currency"
        }

        val fromBaseToSource = getRateFromBaseToCurrency(currency = source, ratesFromBaseToAll)
            ?: return ExchangeRateResult.NotAvailable(source, baseCurrency)

        return ExchangeRateResult.Available(
            ExchangeRate(
                sourceCurrency = source,
                targetCurrency = baseCurrency,
                rateValue = BigDecimal.ONE.divide(
                    fromBaseToSource.rateValue,
                    MathContext(16, RoundingMode.HALF_EVEN)
                ),
                rateDate = fromBaseToSource.rateDate,
            )
        )
    }

    /** Обязательное условие `source` != `target` && `source` != `baseCurrency` &&
     * `target` != `baseCurrency`
     *
     * В противном случае некорректные значения будут возвращены для особых ситуаций:
     *
     * `source` = `target`,
     *
     * `source` = `baseCurrency`,
     *
     * `target` = `baseCurrency`,
     *
     * `source` = `target` = `baseCurrency`
     */
    private fun getCrossRate(
        source: Currency,
        target: Currency,
        ratesFromBaseToAll: List<ExchangeRate>,
    ): ExchangeRateResult {
        require(
            source.id != target.id &&
                    source.id != baseCurrency.id &&
                    target.id != baseCurrency.id
        ) {
            "Incorrect input data for getting cross rate"
        }

        val fromBaseToSource = getRateFromBaseToCurrency(currency = source, ratesFromBaseToAll)
        val fromBaseToTarget = getRateFromBaseToCurrency(currency = target, ratesFromBaseToAll)

        if (fromBaseToSource == null || fromBaseToTarget == null) {
            return ExchangeRateResult.NotAvailable(source, target)
        }

        return ExchangeRateResult.Available(
            ExchangeRate(
                sourceCurrency = fromBaseToSource.targetCurrency,
                targetCurrency = fromBaseToTarget.targetCurrency,
                rateValue = fromBaseToTarget.rateValue.divide(
                    fromBaseToSource.rateValue,
                    MathContext(16, RoundingMode.HALF_EVEN)
                ),
                rateDate = minOf(fromBaseToSource.rateDate, fromBaseToTarget.rateDate)
            )
        )
    }

    private fun getRateFromBaseToCurrency(
        currency: Currency,
        ratesFromBaseToAll: List<ExchangeRate>
    ): ExchangeRate? {
        return ratesFromBaseToAll.find { it.targetCurrency.id == currency.id }
    }
}
