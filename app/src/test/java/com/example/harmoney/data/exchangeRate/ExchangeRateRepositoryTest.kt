package com.example.harmoney.data.exchangeRate

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRate
import com.example.harmoney.domain.models.ExchangeRateResult
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertIs

@Suppress("detekt:LongMethod")
class ExchangeRateRepositoryTest {

    private val baseCurrency = Currency.EUR
    private val clock = Clock.fixed(
        Instant.parse("2026-10-20T12:00:00Z"),
        ZoneOffset.UTC
    )

    private val provider = mockk<ExchangeRateProvider>()
    private val repository = ExchangeRateRepositoryImpl(provider)

    @Test
    fun `returns empty list when sources is empty`() = runTest {
        val result = repository.getRates(sources = emptySet(), target = Currency.EUR)

        assertEquals(
            Resource.Success(emptyList<ExchangeRateResult>()),
            result
        )

        verify { provider wasNot Called }
    }

    @Test
    fun `returns NotRequired when source equals target `() = runTest {
        val result = repository.getRates(sources = setOf(Currency.USD), target = Currency.USD)

        assertEquals(
            Resource.Success(
                listOf(
                    ExchangeRateResult.NotRequired(Currency.USD, Currency.USD)
                )
            ),
            result
        )

        verify { provider wasNot Called }
    }

    @Test
    fun `returns NotRequired when source equals target and baseCurrency`() = runTest {
        val result = repository.getRates(sources = setOf(baseCurrency), target = baseCurrency)

        assertEquals(
            Resource.Success(
                listOf(
                    ExchangeRateResult.NotRequired(baseCurrency, baseCurrency)
                )
            ),
            result
        )

        verify { provider wasNot Called }
    }

    @Test
    fun `returns direct rate when source equals baseCurrency`() =
        runTest {
            // ARRANGE - подготовка входных данных и ответов mock-ов
            // входные данные для репозитория
            val sources = setOf(baseCurrency)
            val target = Currency.USD

            // ответы от provider
            val today = LocalDate.now(clock)

            val rateFromBaseToTarget = BigDecimal("1.20")
            val providerResponse = Resource.Success(
                listOf(
                    ExchangeRate(
                        sourceCurrency = sources.first(),
                        targetCurrency = target,
                        rateDate = today,
                        rateValue = rateFromBaseToTarget
                    )
                )
            )

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = setOf(target),
                )
            } returns providerResponse

            // ACT - вызов реального репозитория
            val result = repository.getRates(sources = sources, target = target)

            // ASSERT - проверка результата
            val expected = Resource.Success(
                listOf(
                    ExchangeRateResult.Available(
                        ExchangeRate(
                            sourceCurrency = sources.first(),
                            targetCurrency = target,
                            rateValue = rateFromBaseToTarget,
                            rateDate = today
                        )
                    )
                )
            )

            assertEquals(expected, result)

            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = setOf(target),
                )
            }
        }

    @Test
    fun `returns inverse rate when target equals  baseCurrency`() =
        runTest {
            // Входные данные для репозитория
            val sources = setOf(Currency.USD)
            val target = baseCurrency

            // ответы от provider
            val today = LocalDate.now(clock)

            val rateFromBaseToSource = BigDecimal("1.25")
            val providerResponse = Resource.Success(
                listOf(
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = sources.first(),
                        rateDate = today,
                        rateValue = rateFromBaseToSource
                    )
                )
            )

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = sources,
                )
            } returns providerResponse

            // Вызов тестируемого репозитория
            val result = repository.getRates(sources = sources, target = target)

            // Оценка результата
            val success = assertIs<Resource.Success<List<ExchangeRateResult>>>(result)
            val available = assertIs<ExchangeRateResult.Available>(success.data.single())
            val actualRate = available.rate

            assertEquals(sources.first(), actualRate.sourceCurrency)
            assertEquals(target, actualRate.targetCurrency)
            assertEquals(today, actualRate.rateDate)
            assertEquals(0, BigDecimal("0.8").compareTo(actualRate.rateValue))

            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = sources,
                )
            }
        }

    @Test
    fun `returns cross rate  when all currencies are  different`() =
        runTest {
            // Входные данные для репозитория
            val sources = setOf(Currency.USD)
            val target = Currency.RUB

            // Выходные данные от provider
            val today = LocalDate.now(clock)

            val rateFromBaseToSource = BigDecimal("2.00")
            val rateFromBaseToTarget = BigDecimal("10.00")

            val providerResponse = Resource.Success(
                listOf(
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = sources.first(),
                        rateDate = today,
                        rateValue = rateFromBaseToSource
                    ),
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = target,
                        rateDate = today,
                        rateValue = rateFromBaseToTarget
                    ),
                )
            )

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = sources + target
                )
            } returns providerResponse

            // Вызов тестируемого репозитрия
            val result = repository.getRates(sources = sources, target = target)

            val success = assertIs<Resource.Success<List<ExchangeRateResult>>>(result)
            val available = assertIs<ExchangeRateResult.Available>(success.data.single())
            val actualRate = available.rate

            assertEquals(sources.first(), actualRate.sourceCurrency)
            assertEquals(target, actualRate.targetCurrency)
            assertEquals(today, actualRate.rateDate)
            assertEquals(0, BigDecimal("5.00").compareTo(actualRate.rateValue))

            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = sources + target
                )
            }
        }

    @Test
    fun `returns NotAvailable when source equals baseCurrency and target is unknown`() =
        runTest {
            // Входные данные для репозитория
            val sources = setOf(baseCurrency)
            val target = Currency.XXX

            // Выходные данные от provider
            val providerResponse = Resource.Success(emptyList<ExchangeRate>())

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = setOf(target)
                )
            } returns providerResponse

            // Обращение к тестируемому репозиторию
            val result = repository.getRates(sources = sources, target = target)

            // Оценка результатов
            val expected = Resource.Success(
                listOf(
                    ExchangeRateResult.NotAvailable(source = sources.first(), target = target)
                )
            )

            assertEquals(expected, result)

            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = setOf(target)
                )
            }
        }

    @Test
    fun `returns NotAvailable when source is unknown and target equals baseCurrency`() =
        runTest {
            // Входные данные для репозитория
            val sources = setOf(Currency.XXX)
            val target = baseCurrency

            // Выходные данные от provider
            val providerResponse = Resource.Success(emptyList<ExchangeRate>())

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = sources
                )
            } returns providerResponse

            // Обращение к тестируемому репозиторию
            val result = repository.getRates(sources = sources, target = target)

            // Оценка результатов
            val expected = Resource.Success(
                listOf(
                    ExchangeRateResult.NotAvailable(source = sources.first(), target = target)
                )
            )

            assertEquals(expected, result)

            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = sources
                )
            }
        }

    @Test
    fun `returns NotAvailable when source doesn't equal baseCurrency and target is unknown`() =
        runTest {
            // Входные данные для репозитория
            val sources = setOf(Currency.USD)
            val target = Currency.XXX

            // Выходные данные от provider
            val today = LocalDate.now(clock)

            val rateFromBaseToSource = BigDecimal("1.20")

            val providerResponse = Resource.Success(
                listOf(
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = sources.first(),
                        rateDate = today,
                        rateValue = rateFromBaseToSource
                    )
                )
            )

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = sources + target
                )
            } returns providerResponse

            // Обращение к тестируемому репозиторию
            val result = repository.getRates(sources = sources, target = target)

            // Оценка результатов
            val expected = Resource.Success(
                listOf(
                    ExchangeRateResult.NotAvailable(source = sources.first(), target = target)
                )
            )

            assertEquals(expected, result)

            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = sources + target
                )
            }
        }

    @Test
    fun `returns NotAvailable when source is unknown and target doesn't equal baseCurrency`() =
        runTest {
            // Входные данные для репозитория
            val sources = setOf(Currency.XXX)
            val target = Currency.USD

            // Выходные данные от provider
            val today = LocalDate.now(clock)

            val rateFromBaseToTarget = BigDecimal("1.20")

            val providerResponse = Resource.Success(
                listOf(
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = target,
                        rateDate = today,
                        rateValue = rateFromBaseToTarget
                    )
                )
            )

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = sources + target
                )
            } returns providerResponse

            // Обращение к тестируемому репозиторию
            val result = repository.getRates(sources = sources, target = target)

            // Оценка результатов
            val expected = Resource.Success(
                listOf(
                    ExchangeRateResult.NotAvailable(source = sources.first(), target = target)
                )
            )

            assertEquals(expected, result)

            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = sources + target
                )
            }
        }

    @Test
    fun `returns direct and cross rates for multiple sources`() =
        runTest {
            // Входные данные для репозитория
            val sources = listOf(baseCurrency, Currency.USD)
            val target = Currency.RUB

            // Выходные данные provider
            val today = LocalDate.now(clock)
            val providerResponse = Resource.Success(
                listOf(
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = Currency.USD,
                        rateDate = today,
                        rateValue = BigDecimal("2.0")
                    ),
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = Currency.RUB,
                        rateDate = today,
                        rateValue = BigDecimal("10.0")
                    ),
                )
            )

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = setOf(Currency.USD, Currency.RUB)
                )
            } returns providerResponse

            // Обращение к тестируемому репозиторию
            val result = repository.getRates(sources = sources.toSet(), target = target)

            val success = assertIs<Resource.Success<List<ExchangeRateResult>>>(result)
            assertEquals(sources.size, success.data.size)
            // Rates должны быть в порядке обхода source
            val actualRate0 = assertIs<ExchangeRateResult.Available>(success.data[0]).rate
            val actualRate1 = assertIs<ExchangeRateResult.Available>(success.data[1]).rate

            assertEquals(sources[0], actualRate0.sourceCurrency)
            assertEquals(target, actualRate0.targetCurrency)
            assertEquals(today, actualRate0.rateDate)
            assertEquals(0, BigDecimal("10.0").compareTo(actualRate0.rateValue))

            assertEquals(sources[1], actualRate1.sourceCurrency)
            assertEquals(target, actualRate1.targetCurrency)
            assertEquals(today, actualRate1.rateDate)
            assertEquals(0, BigDecimal("5.0").compareTo(actualRate1.rateValue))

            // Оценка результатов
            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = setOf(Currency.USD, Currency.RUB)
                )
            }
        }

    @Test
    fun `returns direct rate and NotRequired when target is among sources`() =
        runTest {
            // Входные данные для репозитория
            val sources = listOf(baseCurrency, Currency.USD)
            val target = Currency.USD

            // Выходные данные provider
            val today = LocalDate.now(clock)
            val providerResponse = Resource.Success(
                listOf(
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = Currency.USD,
                        rateDate = today,
                        rateValue = BigDecimal("1.20")
                    ),
                )
            )

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = setOf(Currency.USD),
                )
            } returns providerResponse

            // Обращение к тестируемому репозиторию
            val result = repository.getRates(sources = sources.toSet(), target = target)

            // Оценка результатов
            val expected = Resource.Success(
                listOf(
                    ExchangeRateResult.Available(
                        ExchangeRate(
                            sourceCurrency = sources[0],
                            targetCurrency = target,
                            rateDate = today,
                            rateValue = BigDecimal("1.20")
                        )
                    ),
                    ExchangeRateResult.NotRequired(sources[1], target)
                )
            )

            assertEquals(expected, result)

            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = setOf(Currency.USD),
                )
            }
        }

    @Test
    fun `returns inverse rates and NotRequired when target is baseCurrency`() =
        runTest {
            // Входные данные для репозитория
            val sources = listOf(Currency.USD, baseCurrency, Currency.RUB)
            val target = baseCurrency

            // Выходные данные provider
            val today = LocalDate.now(clock)
            val providerResponse = Resource.Success(
                listOf(
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = Currency.USD,
                        rateDate = today,
                        rateValue = BigDecimal("1.25"),
                    ),
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = Currency.RUB,
                        rateDate = today,
                        rateValue = BigDecimal("2.0"),
                    ),
                )
            )

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency, quoteCurrencies = setOf(Currency.USD, Currency.RUB)
                )
            } returns providerResponse

            // Обращение к тестируемому репозиторию
            val result = repository.getRates(sources = sources.toSet(), target = target)

            // Оценка результатов
            val success = assertIs<Resource.Success<List<ExchangeRateResult>>>(result)
            assertEquals(sources.size, success.data.size)
            // Rates должны быть в порядке обхода sources
            val actualRate0 = assertIs<ExchangeRateResult.Available>(success.data[0]).rate
            val actualRate1 = assertIs<ExchangeRateResult.NotRequired>(success.data[1])
            val actualRate2 = assertIs<ExchangeRateResult.Available>(success.data[2]).rate

            assertEquals(sources[0], actualRate0.sourceCurrency)
            assertEquals(target, actualRate0.targetCurrency)
            assertEquals(today, actualRate0.rateDate)
            assertEquals(
                0, BigDecimal("0.8").compareTo(actualRate0.rateValue)
            )

            assertEquals(sources[1], actualRate1.source)
            assertEquals(target, actualRate1.target)

            assertEquals(sources[2], actualRate2.sourceCurrency)
            assertEquals(target, actualRate2.targetCurrency)
            assertEquals(today, actualRate2.rateDate)
            assertEquals(
                0, BigDecimal("0.5").compareTo(actualRate2.rateValue)
            )

            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency, quoteCurrencies = setOf(Currency.USD, Currency.RUB)
                )
            }
        }

    @Test
    fun `returns Available and NotAvailable for partially available sources`() =
        runTest {
            // Входные данные для репозитория
            val sources = listOf(Currency.USD, Currency.XXX)
            val target = baseCurrency

            // Выходные данные provider
            val today = LocalDate.now(clock)
            val providerResponse = Resource.Success(
                listOf(
                    ExchangeRate(
                        sourceCurrency = baseCurrency,
                        targetCurrency = Currency.USD,
                        rateDate = today,
                        rateValue = BigDecimal("1.25"),
                    )
                )
            )

            coEvery {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = setOf(Currency.USD, Currency.XXX)
                )
            } returns providerResponse

            // Обращение к тестируемому репозиторию
            val result = repository.getRates(sources = sources.toSet(), target = target)

            // Оценка результатов
            val success = assertIs<Resource.Success<List<ExchangeRateResult>>>(result)
            assertEquals(sources.size, success.data.size)
            // Rates должны быть в порядке обхода sources
            val actualRate0 = assertIs<ExchangeRateResult.Available>(success.data[0]).rate
            val actualRate1 = assertIs<ExchangeRateResult.NotAvailable>(success.data[1])

            assertEquals(sources[0], actualRate0.sourceCurrency)
            assertEquals(target, actualRate0.targetCurrency)
            assertEquals(today, actualRate0.rateDate)
            assertEquals(
                0, BigDecimal("0.8")
                    .compareTo(actualRate0.rateValue)
            )

            assertEquals(sources[1], actualRate1.source)
            assertEquals(target, actualRate1.target)

            coVerify(exactly = 1) {
                provider.getAvailableRatesFromBaseToQuote(
                    baseCurrency = baseCurrency,
                    quoteCurrencies = setOf(Currency.USD, Currency.XXX)
                )
            }
        }

    @Test
    fun `uses earliest rate date for cross rate`() = runTest {
        // Входные данные для репозитория
        val sources = listOf(Currency.USD, Currency.JPY)
        val target = Currency.RUB

        // Выходные данные provider
        val today = LocalDate.now(clock)
        val providerResponse = Resource.Success(
            listOf(
                ExchangeRate(
                    sourceCurrency = baseCurrency,
                    targetCurrency = Currency.USD,
                    rateDate = today.minusDays(2),
                    rateValue = BigDecimal("2.0")
                ),
                ExchangeRate(
                    sourceCurrency = baseCurrency,
                    targetCurrency = Currency.JPY,
                    rateDate = today,
                    rateValue = BigDecimal("5.0")
                ),
                ExchangeRate(
                    sourceCurrency = baseCurrency,
                    targetCurrency = Currency.RUB,
                    rateDate = today.minusDays(1),
                    rateValue = BigDecimal("10.0")
                ),
            )
        )

        coEvery {
            provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency,
                quoteCurrencies = setOf(Currency.USD, Currency.RUB, Currency.JPY)
            )
        } returns providerResponse

        // Обращение к тестируемому репозиторию
        val result = repository.getRates(sources = sources.toSet(), target = target)

        // Оценка результатов
        val success = assertIs<Resource.Success<List<ExchangeRateResult>>>(result)
        assertEquals(sources.size, success.data.size)
        val actualRate0 = assertIs<ExchangeRateResult.Available>(success.data[0]).rate
        val actualRate1 = assertIs<ExchangeRateResult.Available>(success.data[1]).rate

        assertEquals(sources[0], actualRate0.sourceCurrency)
        assertEquals(target, actualRate0.targetCurrency)
        assertEquals(today.minusDays(2), actualRate0.rateDate)
        assertEquals(
            0, BigDecimal("5.0").compareTo(actualRate0.rateValue)
        )

        assertEquals(sources[1], actualRate1.sourceCurrency)
        assertEquals(target, actualRate1.targetCurrency)
        assertEquals(today.minusDays(1), actualRate1.rateDate)
        assertEquals(
            0, BigDecimal("2.0").compareTo(actualRate1.rateValue)
        )

        coVerify(exactly = 1) {
            provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency,
                quoteCurrencies = setOf(Currency.USD, Currency.RUB, Currency.JPY)
            )
        }
    }

    @Test
    fun `propagates error from provider`() = runTest {
        // Входные данные для репозитория
        val sources = setOf(Currency.RUB, baseCurrency)
        val target = Currency.USD

        // Выходные данные provider
        val providerResponse = Resource.Error(ExchangeRateFailure.DatabaseError)

        coEvery {
            provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency,
                quoteCurrencies = setOf(Currency.RUB, Currency.USD)
            )
        } returns providerResponse

        // Обращение к тестируемому репозиторию
        val result = repository.getRates(sources = sources, target = target)

        // Оценка результатов
        assertEquals(Resource.Error(ExchangeRateFailure.DatabaseError), result)

        coVerify(exactly = 1) {
            provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency,
                quoteCurrencies = setOf(Currency.RUB, Currency.USD)
            )
        }
    }
}
