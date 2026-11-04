package com.example.harmoney.data.exchangeRate

import androidx.sqlite.SQLiteException
import com.example.harmoney.core.util.Resource
import com.example.harmoney.data.currencyList.network.SupportedCurrenciesDataSource
import com.example.harmoney.data.currencyList.network.SupportedCurrenciesResult
import com.example.harmoney.data.exchangeRate.db.converter.ExchangeRateDBConverter
import com.example.harmoney.data.exchangeRate.db.converter.ExchangeRateDBConverterImpl
import com.example.harmoney.data.exchangeRate.db.dao.ExchangeRateDao
import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateEntity
import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateSyncEntity
import com.example.harmoney.data.exchangeRate.netwok.converter.ExchangeRateDtoConverter
import com.example.harmoney.data.exchangeRate.netwok.converter.ExchangeRateDtoConverterImpl
import com.example.harmoney.data.exchangeRate.netwok.dto.ExchangeRateDto
import com.example.harmoney.data.network.NetworkResult
import com.example.harmoney.data.network.RemoteDataSource
import com.example.harmoney.domain.currencyList.models.CurrencyListFailure
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRate
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.test.assertEquals

@Suppress("detekt:LongMethod", "detekt:LargeClass")
class ExchangeRateProviderTest {
    private val baseCurrency = Currency.EUR

    private val remoteDataSource = mockk<RemoteDataSource>()
    private val exchangeRateDao = mockk<ExchangeRateDao>()
    private val supportedCurrenciesDataSource = mockk<SupportedCurrenciesDataSource>()
    private val clock = Clock.fixed(
        Instant.parse("2026-10-20T12:00:00Z"),
        ZoneOffset.UTC
    )
    private val dbConverter: ExchangeRateDBConverter = ExchangeRateDBConverterImpl()
    private val dtoConverter: ExchangeRateDtoConverter = ExchangeRateDtoConverterImpl()

    private val provider = ExchangeRateProviderImpl(
        remoteDataSource = remoteDataSource,
        exchangeRateDao = exchangeRateDao,
        exchangeRateDBConverter = dbConverter,
        exchangeRateDtoConverter = dtoConverter,
        supportedCurrenciesDataSource = supportedCurrenciesDataSource,
        clock = clock
    )

    // region Получение курсов из кеша
    @Test
    fun `returns empty list when input currencies set is empty`() = runTest {
        // Входные данные для provider
        val currencies = emptySet<Currency>()

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = Resource.Success(emptyList<ExchangeRate>())

        assertEquals(expected, result)

        verify { exchangeRateDao wasNot Called }
        verify { remoteDataSource wasNot Called }
        verify { supportedCurrenciesDataSource wasNot Called }
    }

    @Test
    fun `returns cached rates when all rates are available for today`() = runTest {
        // Входные данные для provider
        val currencies = setOf(Currency.USD, Currency.RUB)

        // Выходные данные БД
        val today = LocalDate.now(clock)

        val exchangeRateEntities = listOf(
            rateEntity(Currency.RUB, today, "90.0"),
            rateEntity(Currency.USD, today, "1.20"),
        )

        coEveryGetRatesByDate(currencies, today, exchangeRateEntities)

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = setOf(
            rate(Currency.USD, today, "1.20"),
            rate(Currency.RUB, today, "90.0")
        )

        assertSuccessRates(expected, result)

        coVerifyGetRatesByDate(1, currencies, today)

        coVerifyGetSyncedCurrencyIds(0)
        coVerifyInsertRates(0)
        coVerifyMarkAsSynced(0)
        coVerifyGetRatesClosestToDate(0)

        verify { remoteDataSource wasNot Called }
        verify { supportedCurrenciesDataSource wasNot Called }
    }

    @Test
    fun `returns latest cached rates when missing rates were already synced today`() = runTest {
        // Входные данные для provider
        val currencies = setOf(Currency.USD, Currency.RUB)

        // Выходные данные БД
        val today = LocalDate.now(clock)

        val rateEntitiesByDate = listOf(rateEntity(Currency.USD, today, "1.20"))

        val syncedIds = listOf(Currency.RUB.id)

        val rateEntitiesClosestToDate = listOf(
            rateEntity(Currency.USD, today, "1.20"),
            // cвежего курса нет и уже пытались обновляться сегодня, значит в БД только старый
            rateEntity(Currency.RUB, today.minusDays(1), "91.0"),
        )

        coEveryGetRatesByDate(currencies, today, rateEntitiesByDate)

        coEveryGetSyncedCurrencyIds(setOf(Currency.RUB), today, syncedIds)

        coEveryGetClosestToDate(currencies, today, rateEntitiesClosestToDate)

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = setOf(
            rate(Currency.USD, today, "1.20"),
            rate(Currency.RUB, today.minusDays(1), "91.0"),
        )

        assertSuccessRates(expected, result)

        coVerifyGetRatesByDate(1, currencies, today)

        coVerifyGetSyncedCurrencyIds(1, setOf(Currency.RUB), today)

        coVerifyGetRatesClosestToDate(1, currencies, today)

        coVerifyMarkAsSynced(0)
        coVerifyInsertRates(0)

        verify { remoteDataSource wasNot Called }
        verify { supportedCurrenciesDataSource wasNot Called }
    }
    // endregion

    // region Поддерживаемые валюты
    @Test
    fun `fetches rates only for supported currencies`() = runTest {
        // Входные данные для provider
        val currencies = setOf(Currency.USD, Currency.XXX, Currency.RUB)

        // Выходные данные БД
        val today = LocalDate.now(clock)

        val supportedCurrencies =
            SupportedCurrenciesResult.Available(
                currencies = listOf(Currency.EUR, Currency.RUB, Currency.USD), isStale = false
            )

        val remoteRates = NetworkResult.Success(
            listOf(
                rateDto(Currency.RUB, today, "90.0"),
                rateDto(Currency.USD, today, "1.20"),
            )
        )
        val rateEntitiesClosestToDate = listOf(
            rateEntity(Currency.USD, today, "1.20"),
            rateEntity(Currency.RUB, today, "90.0"),
        )

        coEveryGetRatesByDate(currencies, today, emptyList())

        coEveryGetSyncedCurrencyIds(currencies, today, emptyList())

        coEveryGetSupportedCurrencies(supportedCurrencies)

        coEveryMarkAsSynced(listOf(syncEntity(Currency.XXX, today)))

        coEveryGetRemoteRates(
            "USD,RUB",// порядок по обходу currencies
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
            today,
            remoteRates
        )

        coEveryInsertRates(
            // порядок, как в ответе api (обычно по алфавиту isoCode, но в тесте зависит от
            // нашего порядка в переменной remoteRates)
            listOf(
                rateEntity(Currency.RUB, today, "90.0"),
                rateEntity(Currency.USD, today, "1.20"),
            )
        )

        coEveryMarkAsSynced(// порядок по обходу currencies
            listOf(
                syncEntity(Currency.USD, today),
                syncEntity(Currency.RUB, today)
            )
        )

        coEveryGetClosestToDate(currencies, today, rateEntitiesClosestToDate)

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = setOf(
            rate(Currency.USD, today, "1.20"),
            rate(Currency.RUB, today, "90.0"),
        )

        assertSuccessRates(expected, result)
        val success = result as Resource.Success

        coVerifyGetRatesByDate(2, currencies, today)

        coVerifyGetSyncedCurrencyIds(2, currencies, today)

        coVerifyGetSupportedCurrencies(1)

        coVerifyMarkAsSynced(1, listOf(syncEntity(Currency.XXX, today)))

        coVerifyGetRemoteRates(
            1, "USD,RUB",// порядок по обходу currencies
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE), today
        )

        coVerifyInsertRates(// порядок, как в ответе api
            1,
            listOf(
                rateEntity(Currency.RUB, today, "90.0"),
                rateEntity(Currency.USD, today, "1.20"),
            )
        )

        coVerifyMarkAsSynced(// порядок по обходу currencies
            1, listOf(
                syncEntity(Currency.USD, today),
                syncEntity(Currency.RUB, today),
            )
        )

        coVerifyGetRatesClosestToDate(1, currencies, today)
    }

    @Test
    fun `marks unsupported currencies as synced when currency list is fresh`() = runTest {
        // входные данные для provider
        val currencies = setOf(Currency.XXX, Currency.XUA)

        // Выходные данные БД
        val today = LocalDate.now(clock)

        val supportedCurrencies = SupportedCurrenciesResult.Available(
            currencies = listOf(Currency.USD, Currency.RUB, Currency.EUR), isStale = false
        )

        coEveryGetRatesByDate(currencies, today, emptyList())

        coEveryGetSyncedCurrencyIdsMany(
            currencies, today, listOf(
                emptyList(),// перед mutex (первый запуск)
                emptyList(),// внутри mutex (первый запуск)
                listOf(Currency.XXX.id, Currency.XUA.id) // перед mutex (второй запуск)
            )
        )

        coEveryGetSupportedCurrencies(supportedCurrencies)

        coEveryMarkAsSynced(// порядок обхода currencies
            listOf(
                syncEntity(Currency.XXX, today),
                syncEntity(Currency.XUA, today),
            )
        )

        coEveryGetClosestToDate(currencies, today, emptyList())

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = Resource.Success(emptyList<ExchangeRate>())

        assertEquals(expected, result)

        coVerifyGetRatesByDate(2, currencies, today)

        coVerifyGetSyncedCurrencyIds(2, currencies, today)

        coVerifyGetSupportedCurrencies(1)

        coVerifyMarkAsSynced(// порядок обхода currencies
            1, listOf(
                syncEntity(Currency.XXX, today),
                syncEntity(Currency.XUA, today),
            )
        )

        coVerifyGetRatesClosestToDate(1, currencies, today)

        coVerifyInsertRates(0)

        verify { remoteDataSource wasNot Called }


        // Запускаем во второй раз, чтобы убедиться, что произошла маркировка
        val resultAfterMarking = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        assertEquals(expected, resultAfterMarking)

        coVerifyGetRatesByDate(3, currencies, today)// 2 ранее, 1 в этот запуск

        coVerifyGetSyncedCurrencyIds(3, currencies, today)//2 ранее, 1 в этот запуск

        coVerifyGetSupportedCurrencies(1)// 1 в прошлый запуск

        coVerifyMarkAsSynced(1)// 1 в прошлый запуск

        coVerifyGetRatesClosestToDate(2, currencies, today)//1 ранее, 1 в этот запуск

        coVerifyInsertRates(0)

        verify { remoteDataSource wasNot Called }
    }

    @Test
    fun `does not mark unsupported currencies as synced when currency list is stale`() =
        runTest {
            // Входные данные для provider
            val currencies = setOf(Currency.XXX, Currency.XUA)

            // Выходные данные БД
            val today = LocalDate.now(clock)

            val supportedCurrencies = SupportedCurrenciesResult.Available(
                currencies = listOf(Currency.USD, Currency.RUB, Currency.EUR), isStale = true
            )

            coEveryGetRatesByDate(currencies, today, emptyList())

            coEveryGetSyncedCurrencyIds(currencies, today, emptyList())

            coEveryGetSupportedCurrencies(supportedCurrencies)

            coEveryGetClosestToDate(currencies, today, emptyList())

            // Обращение к тестируемому provider
            val result = provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency, quoteCurrencies = currencies
            )

            // Оценка результата
            val expected = Resource.Success(emptyList<ExchangeRate>())

            assertEquals(expected, result)

            coVerifyGetRatesByDate(2, currencies, today)

            coVerifyGetSyncedCurrencyIds(2, currencies, today)

            coVerifyGetSupportedCurrencies(1)

            coVerifyMarkAsSynced(0)

            coVerifyGetRatesClosestToDate(1, currencies, today)

            coVerifyInsertRates(0)

            verify { remoteDataSource wasNot Called }
        }

    @Test
    fun `not fetch rates when supported currencies list is unavailable (returns cache instead)`() =
        runTest {
            // Входные данные для provider
            val currencies = setOf(Currency.USD, Currency.XXX)

            // Выходные данные БД
            val today = LocalDate.now(clock)

            val supportedCurrencies =
                SupportedCurrenciesResult.Unavailable(CurrencyListFailure.NetworkError)
            val rateEntitiesClosestToDate = listOf(
                // т.к. не нашли в сегодняшнем кеше и не смогли обновиться, то возвращаем старый кеш
                rateEntity(Currency.USD, today.minusDays(1), "1.21")
            )

            coEveryGetRatesByDate(currencies, today, emptyList())

            coEveryGetSyncedCurrencyIds(currencies, today, emptyList())

            coEveryGetSupportedCurrencies(supportedCurrencies)

            coEveryGetClosestToDate(currencies, today, rateEntitiesClosestToDate)

            // Обращение к тестируемому provider
            val result = provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency, quoteCurrencies = currencies
            )

            // Оценка результата
            val expected = Resource.Success(
                listOf(
                    rate(Currency.USD, today.minusDays(1), "1.21")
                )
            )

            assertEquals(expected, result)

            coVerifyGetRatesByDate(2, currencies, today)

            coVerifyGetSyncedCurrencyIds(2, currencies, today)

            coVerifyGetSupportedCurrencies(1)

            coVerifyGetRatesClosestToDate(1, currencies, today)

            coVerifyMarkAsSynced(0)

            coVerifyInsertRates(0)

            verify { remoteDataSource wasNot Called }
        }
    // endregion

    // region Получение курсов из сети
    @Test
    fun `fetches missing rates when only some rates are cached for today`() = runTest {
        // Входные данные для provider
        val currencies = setOf(Currency.USD, Currency.RUB)

        // Выходные данные БД и api
        val today = LocalDate.now(clock)

        val rateEntitiesByDate = listOf(rateEntity(Currency.USD, today, "1.20"))

        val supportedCurrencies = SupportedCurrenciesResult.Available(
            listOf(Currency.EUR, Currency.RUB, Currency.USD), isStale = false
        )

        val remoteRates = NetworkResult.Success(
            listOf(rateDto(Currency.RUB, today, "90.0"))
        )

        val ratesEntitiesClosestToDate = listOf(
            rateEntity(Currency.RUB, today, "90.0"),
            rateEntity(Currency.USD, today, "1.20"),
        )

        coEveryGetRatesByDate(currencies, today, rateEntitiesByDate)

        coEveryGetSyncedCurrencyIds(setOf(Currency.RUB), today, emptyList())

        coEveryGetSupportedCurrencies(supportedCurrencies)

        coEveryGetRemoteRates(
            "RUB",
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
            today,
            remoteRates
        )

        coEveryInsertRates(listOf(rateEntity(Currency.RUB, today, "90.0")))

        coEveryMarkAsSynced(listOf(syncEntity(Currency.RUB, today)))

        coEveryGetClosestToDate(currencies, today, ratesEntitiesClosestToDate)

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = setOf(
            rate(Currency.USD, today, "1.20"),
            rate(Currency.RUB, today, "90.0"),
        )

        assertSuccessRates(expected, result)

        coVerifyGetRatesByDate(2, currencies, today)

        coVerifyGetSyncedCurrencyIds(2, setOf(Currency.RUB), today)

        coVerifyGetSupportedCurrencies(1)

        coVerifyGetRemoteRates(
            1,
            "RUB",
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
            today
        )

        coVerifyInsertRates(
            1,
            listOf(rateEntity(Currency.RUB, today, "90.0"))
        )

        coVerifyMarkAsSynced(1, listOf(syncEntity(Currency.RUB, today)))

        coVerifyGetRatesClosestToDate(1, currencies, today)
    }

    @Test
    fun `cached fetches rates and marks currencies as synced when api response is successful`() =
        runTest {
            // Входные данные для provider
            val currencies = setOf(Currency.USD, Currency.RUB)

            // Выходные данные БД и api
            val today = LocalDate.now(clock)

            val rateEntitiesByDate = listOf(rateEntity(Currency.USD, today, "1.20"))

            val rateEntitiesByDate2 = listOf(
                rateEntity(Currency.USD, today, "1.20"),
                rateEntity(Currency.RUB, today, "90.0"),
            )

            val supportedCurrencies = SupportedCurrenciesResult.Available(
                listOf(Currency.EUR, Currency.RUB, Currency.USD), isStale = false
            )

            val remoteRates = NetworkResult.Success(
                listOf(rateDto(Currency.RUB, today, "90.0"))
            )

            val ratesEntitiesClosestToDate = listOf(
                rateEntity(Currency.RUB, today, "90.0"),
                rateEntity(Currency.USD, today, "1.20"),
            )

            coEveryGetRatesByDateMany(
                currencies, today, listOf(
                    rateEntitiesByDate, // до mutex (первый запрос)
                    rateEntitiesByDate, // внутри mutex (первый запрос)
                    rateEntitiesByDate2, // до mutex (второй запрос)
                )
            )

            coEveryGetSyncedCurrencyIds(
                setOf(Currency.RUB),
                today, emptyList()
            )

            coEveryGetSupportedCurrencies(supportedCurrencies)

            coEveryGetRemoteRates(
                "RUB",
                today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
                today,
                remoteRates
            )

            coEveryInsertRates(listOf(rateEntity(Currency.RUB, today, "90.0")))

            coEveryMarkAsSynced(listOf(syncEntity(Currency.RUB, today)))

            coEveryGetClosestToDate(currencies, today, ratesEntitiesClosestToDate)

            // Обращение к тестируемому provider
            val result = provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency, quoteCurrencies = currencies
            )

            // Оценка результата
            val expected = setOf(
                rate(Currency.USD, today, "1.20"),
                rate(Currency.RUB, today, "90.0"),
            )

            assertSuccessRates(expected, result)

            coVerifyGetRatesByDate(2, currencies, today)

            coVerifyGetSyncedCurrencyIds(2, setOf(Currency.RUB), today)

            coVerifyGetSupportedCurrencies(1)

            coVerifyGetRemoteRates(
                1, "RUB",
                today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE), today
            )

            coVerifyInsertRates(
                1,
                listOf(rateEntity(Currency.RUB, today, "90.0"))
            )

            coVerifyMarkAsSynced(1, listOf(syncEntity(Currency.RUB, today)))

            coVerifyGetRatesClosestToDate(1, currencies, today)

            // Обращаемся во второй раз, чтобы убедиться, что synced rate получим из кеша
            val resultAfterCachingAndMarking = provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency, quoteCurrencies = currencies
            )
            assertSuccessRates(expected, resultAfterCachingAndMarking)

            coVerifyGetRatesByDate(3, currencies, today)//2 ранее, 1 в этот запуск

            coVerifyGetSyncedCurrencyIds(2)// 2 в прошлый раз

            coVerifyGetSupportedCurrencies(1)// 1 в прошлый раз

            coVerifyGetRemoteRates(1)// 1 в прошлый раз

            coVerifyInsertRates(1)// 1 в прошлый раз

            coVerifyMarkAsSynced(1)// 1 в прошлый раз

            coVerifyGetRatesClosestToDate(1)// 1 в прошлый раз
        }

    @Test
    fun `saves only latest rate for each currency`() = runTest {
        // Входные данные для provider
        val currencies = setOf(Currency.RUB)

        // Выходные данные БД и api
        val today = LocalDate.now(clock)

        val supportedCurrencies = SupportedCurrenciesResult.Available(
            listOf(Currency.EUR, Currency.RUB, Currency.USD), isStale = false
        )

        val remoteRates = NetworkResult.Success(
            // специально перемешали, чтобы убедиться, что метод выбирает самый свежий курс
            listOf(
                rateDto(Currency.RUB, today.minusDays(5), "95.0"),
                rateDto(Currency.RUB, today.minusDays(4), "94.0"),
                rateDto(Currency.RUB, today.minusDays(6), "96.0"),
                rateDto(Currency.RUB, today.minusDays(3), "93.0"),
                rateDto(Currency.RUB, today, "90.0"),
                rateDto(Currency.RUB, today.minusDays(2), "92.0"),
                rateDto(Currency.RUB, today.minusDays(1), "91.0"),
            )
        )

        val ratesEntitiesClosestToDate = listOf(
            rateEntity(Currency.RUB, today, "90.0"),
        )

        coEveryGetRatesByDate(currencies, today, emptyList())

        coEveryGetSyncedCurrencyIds(currencies, today, emptyList())

        coEveryGetSupportedCurrencies(supportedCurrencies)

        coEveryGetRemoteRates(
            "RUB",
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
            today,
            remoteRates
        )

        coEveryInsertRates(listOf(rateEntity(Currency.RUB, today, "90.0")))

        coEveryMarkAsSynced(listOf(syncEntity(Currency.RUB, today)))

        coEveryGetClosestToDate(currencies, today, ratesEntitiesClosestToDate)

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = Resource.Success(
            listOf(rate(Currency.RUB, today, "90.0"))
        )

        assertEquals(expected, result)

        coVerifyGetRatesByDate(2, currencies, today)

        coVerifyGetSyncedCurrencyIds(2, currencies, today)

        coVerifyGetSupportedCurrencies(1)

        coVerifyGetRemoteRates(
            1, "RUB",
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE), today
        )

        coVerifyInsertRates(
            1,
            listOf(rateEntity(Currency.RUB, today, "90.0"))
        )

        coVerifyMarkAsSynced(1, listOf(syncEntity(Currency.RUB, today)))

        coVerifyGetRatesClosestToDate(1, currencies, today)
    }

    @Test
    fun `marks synced currency by today even if the freshest fetched currency is not today`() =
        runTest {
            // Входные данные для provider
            val currencies = setOf(Currency.RUB)

            // Выходные данные БД и api
            val today = LocalDate.now(clock)

            val syncedIds2 = listOf(Currency.RUB.id)

            val supportedCurrencies = SupportedCurrenciesResult.Available(
                listOf(Currency.EUR, Currency.RUB, Currency.USD), isStale = false
            )

            val remoteRates = NetworkResult.Success(
                listOf(
                    rateDto(Currency.RUB, today.minusDays(1), "91.0"),
                )
            )

            val ratesEntitiesClosestToDate = listOf(
                rateEntity(Currency.RUB, today.minusDays(1), "91.0"),
            )

            coEveryGetRatesByDate(currencies, today, emptyList())

            coEveryGetSyncedCurrencyIdsMany(
                currencies, today, listOf(
                    emptyList(),// до mutex (первый запуск)
                    emptyList(),// внутри mutex (первый запуск)
                    syncedIds2 // до mutex (второй запуск)
                )
            )

            coEveryGetSupportedCurrencies(supportedCurrencies)

            coEveryGetRemoteRates(
                "RUB",
                today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
                today,
                remoteRates
            )

            coEveryInsertRates(
                listOf(
                    rateEntity(Currency.RUB, today.minusDays(1), "91.0")
                )
            )

            coEveryMarkAsSynced(listOf(syncEntity(Currency.RUB, today)))

            coEveryGetClosestToDate(currencies, today, ratesEntitiesClosestToDate)

            // Обращение к тестируемому provider
            val result = provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency, quoteCurrencies = currencies
            )

            // Оценка результата
            val expected = Resource.Success(
                listOf(
                    rate(Currency.RUB, today.minusDays(1), "91.0"),
                )
            )

            assertEquals(expected, result)

            coVerifyGetRatesByDate(2, currencies, today)

            coVerifyGetSyncedCurrencyIds(2, currencies, today)

            coVerifyGetSupportedCurrencies(1)

            coVerifyGetRemoteRates(
                1, "RUB",
                today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE), today
            )

            coVerifyInsertRates(
                1, listOf(
                    rateEntity(Currency.RUB, today.minusDays(1), "91.0")
                )
            )

            coVerifyMarkAsSynced(1, listOf(syncEntity(Currency.RUB, today)))

            coVerifyGetRatesClosestToDate(1, currencies, today)

            // Обращаемся к provider во второй раз, чтобы убедиться, что курс был synced
            // сегодняшним днем
            val resultAfterMarking = provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency, quoteCurrencies = currencies
            )

            assertEquals(expected, resultAfterMarking)

            coVerifyGetRatesByDate(3, currencies, today)//2 ранее, 1 в этот запуск

            coVerifyGetSyncedCurrencyIds(3, currencies, today)//2 ранее, 1 в этот раз

            coVerifyGetSupportedCurrencies(1)// 1 в прошлый раз

            coVerifyGetRemoteRates(
                1, "RUB",
                today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE), today
            )// 1 в прошлый раз

            coVerifyInsertRates(
                1, listOf(
                    rateEntity(Currency.RUB, today.minusDays(1), "91.0")
                )
            )// 1 в прошлый раз

            // 1 в прошлый раз
            coVerifyMarkAsSynced(1, listOf(syncEntity(Currency.RUB, today)))

            coVerifyGetRatesClosestToDate(2, currencies, today)//1 ранее, 1 в этот раз
        }

    @Test
    fun `marks currencies as synced when api returns empty rates`() = runTest {
        // Входные данные для provider
        val currencies = setOf(Currency.USD, Currency.RUB)

        // Выходные данные БД
        val today = LocalDate.now(clock)

        val supportedCurrencies = SupportedCurrenciesResult.Available(
            listOf(Currency.EUR, Currency.RUB, Currency.USD), isStale = false
        )
        val remoteRates = NetworkResult.Success(emptyList<ExchangeRateDto>())
        val rateEntitiesClosesToDate = listOf(
            /// свежего курса в кеше нет, а api ничего не вернул, поэтому в кеше старый курс
            rateEntity(Currency.USD, today.minusDays(1), "1.21"),
            rateEntity(Currency.RUB, today.minusDays(1), "91.0"),
        )

        coEveryGetRatesByDate(currencies, today, emptyList())

        coEveryGetSyncedCurrencyIdsMany(
            currencies, today, listOf(
                emptyList(), // проверка перед Mutex
                emptyList(), // проверка в Mutex перед api
                listOf(Currency.USD.id, Currency.RUB.id) // проверка перед Mutex при втором вызове
            )
        )

        coEveryGetSupportedCurrencies(supportedCurrencies)

        coEveryGetRemoteRates(
            "USD,RUB",// порядок по обходу currencies
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
            today,
            remoteRates
        )

        coEveryMarkAsSynced(
            listOf(
                // порядок по обходу currencies
                syncEntity(Currency.USD, today),
                syncEntity(Currency.RUB, today),
            )
        )

        coEveryGetClosestToDate(currencies, today, rateEntitiesClosesToDate)

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = setOf(
            rate(Currency.USD, today.minusDays(1), "1.21"),
            rate(Currency.RUB, today.minusDays(1), "91.0"),
        )

        assertSuccessRates(expected, result)

        coVerifyGetRatesByDate(2, currencies, today)

        coVerifyGetSyncedCurrencyIds(2, currencies, today)

        coVerifyGetSupportedCurrencies(1)

        coVerifyGetRemoteRates(
            1, "USD,RUB",// порядок по обходу currencies
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE), today
        )

        coVerifyInsertRates(0)

        coVerifyMarkAsSynced(// порядок по обходу currencies
            1, listOf(
                syncEntity(Currency.USD, today),
                syncEntity(Currency.RUB, today),
            )
        )

        coVerifyGetRatesClosestToDate(1, currencies, today)

        // Cнова обращаемся к provider (должны выйти до Mutex, т.к. уже обновлялись в прошлом
        // запуске)
        val resultAfterEmptyRemoteRates = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        assertSuccessRates(expected, resultAfterEmptyRemoteRates)

        coVerifyGetRatesByDate(3, currencies, today)// 2 ранее, 1 сейчас

        coVerifyGetSyncedCurrencyIds(3, currencies, today)// 2 ранее, 1 сейчас

        coVerifyGetRatesClosestToDate(2, currencies, today)// 1 ранее, 1 сейчас

        coVerifyGetSupportedCurrencies(1)// 1 в прошлый раз
        coVerifyGetRemoteRates(1)// 1 в прошлый раз
        coVerifyInsertRates(0)
        coVerifyMarkAsSynced(1)// 1 в прошлый раз
    }

    @Test
    fun `returns cached rates when API request fails`() = runTest {
        // Входные данные для provider
        val currencies = setOf(Currency.USD, Currency.RUB)

        // Выходные данные БД
        val today = LocalDate.now(clock)

        val supportedCurrencies = SupportedCurrenciesResult.Available(
            listOf(Currency.USD, Currency.RUB, Currency.EUR), isStale = false
        )
        val remoteResponse = NetworkResult.HttpError(code = 400)
        val rateEntitiesClosestToDate = listOf(
            // в свежем кеше нет, а api вернул ошибку, поэтому возвращаем старый кеш
            rateEntity(Currency.USD, today.minusDays(1), "1.21"),
            rateEntity(Currency.RUB, today.minusDays(1), "91.0"),
        )

        coEveryGetRatesByDate(currencies, today, emptyList())

        coEveryGetSyncedCurrencyIds(currencies, today, emptyList())

        coEveryGetSupportedCurrencies(supportedCurrencies)

        coEveryGetRemoteRates(
            "USD,RUB",// порядок по обходу currencies
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
            today,
            remoteResponse
        )

        coEveryGetClosestToDate(currencies, today, rateEntitiesClosestToDate)

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = setOf(
            rate(Currency.USD, today.minusDays(1), "1.21"),
            rate(Currency.RUB, today.minusDays(1), "91.0"),
        )

        assertSuccessRates(expected, result)

        coVerifyGetRatesByDate(2, currencies, today)

        coVerifyGetSyncedCurrencyIds(2, currencies, today)

        coVerifyGetSupportedCurrencies(1)

        coVerifyGetRemoteRates(
            1, "USD,RUB",// порядок по обходу currencies
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE), today
        )

        coVerifyGetRatesClosestToDate(1, currencies, today)

        coVerifyInsertRates(0)

        coVerifyMarkAsSynced(0)
    }
    // endregion

    // region Ошибки БД и конкурентные запросы
    @Test
    fun `returns DatabaseError when database read fails`() = runTest {
        // Входные данные для provider
        val currencies = setOf(Currency.USD, Currency.RUB)

        // Выходные данные БД
        val today = LocalDate.now(clock)

        coEvery {
            exchangeRateDao.getRatesByDate(
                baseCurrencyId = baseCurrency.id,
                // в порядке обхода currencies
                quoteCurrencyIds = listOf(Currency.USD.id, Currency.RUB.id),
                dateEpochDay = today.toEpochDay()
            )
        } throws SQLiteException("Database read failed")

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = Resource.Error(ExchangeRateFailure.DatabaseError)

        assertEquals(expected, result)

        coVerifyGetRatesByDate(1, currencies, today)

        coVerifyGetSyncedCurrencyIds(0)
        coVerifyMarkAsSynced(0)
        coVerifyInsertRates(0)
        coVerifyGetRatesClosestToDate(0)

        verify { remoteDataSource wasNot Called }
        verify { supportedCurrenciesDataSource wasNot Called }
    }

    @Test
    fun `returns DatabaseError when database insert fails`() = runTest {
        // Входные данные для provider
        val currencies = setOf(Currency.USD)

        // Выходные данные БД
        val today = LocalDate.now(clock)

        val rateEntitiesByDate = emptyList<ExchangeRateEntity>()
        val syncedIds = emptyList<Long>()
        val supportedCurrencies =
            SupportedCurrenciesResult.Available(
                currencies = listOf(Currency.EUR, Currency.RUB, Currency.USD), isStale = false
            )
        val remoteRates = NetworkResult.Success(
            listOf(rateDto(Currency.USD, today, "1.20"))
        )

        coEveryGetRatesByDate(currencies, today, rateEntitiesByDate)

        coEveryGetSyncedCurrencyIds(currencies, today, syncedIds)

        coEveryGetSupportedCurrencies(supportedCurrencies)

        coEveryGetRemoteRates(
            "USD",
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
            today,
            remoteRates
        )

        coEvery {
            exchangeRateDao.insertRates(
                listOf(rateEntity(Currency.USD, today, "1.20"))
            )
        } throws SQLiteException("Database insert failed")

        // Обращение к тестируемому provider
        val result = provider.getAvailableRatesFromBaseToQuote(
            baseCurrency = baseCurrency, quoteCurrencies = currencies
        )

        // Оценка результата
        val expected = Resource.Error(ExchangeRateFailure.DatabaseError)

        assertEquals(expected, result)

        coVerifyGetRatesByDate(2, currencies, today)

        coVerifyGetSyncedCurrencyIds(2, currencies, today)

        coVerifyGetSupportedCurrencies(1)

        coVerifyGetRemoteRates(
            1, "USD",
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE), today
        )

        coVerifyInsertRates(
            1, listOf(rateEntity(Currency.USD, today, "1.20"))
        )

        coVerifyMarkAsSynced(0)

        coVerifyGetRatesClosestToDate(0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `does not fetch rates twice when cache is updated while waiting for mutex`() = runTest {
        // Входные данные для provider
        val currencies = setOf(Currency.RUB)

        // Выходные данные БД
        val today = LocalDate.now(clock)

        // кеш после того, как первый запрос обновит его по api
        val rateEntitiesByDate2 = listOf(rateEntity(Currency.RUB, today, "90.0"))

        val rateEntitiesClosestToDate = listOf(rateEntity(Currency.RUB, today, "90.0"))

        val supportedCurrencies = SupportedCurrenciesResult.Available(
            listOf(Currency.EUR, Currency.RUB, Currency.USD), isStale = false
        )

        val remoteRates = NetworkResult.Success(
            listOf(rateDto(Currency.RUB, today, "90.0"))
        )

        // Сигнал о том, что первая корутина достигла getRates(). После него можно запускать вторую
        // корутину.
        val apiStarted = CompletableDeferred<Unit>()
        // Сигнал о том, что первой корутине можно продолжить выполнение. Отправляем его после того,
        // как запустили вторую корутину и осуществили необходимые проверки.
        val releaseApi = CompletableDeferred<Unit>()

        coEveryGetRatesByDateMany(
            currencies, today, listOf(
                emptyList(), // до mutex (первая корутина)
                emptyList(), // внутри mutex (первая корутина)
                emptyList(), // до mutex (вторая корутина)
                rateEntitiesByDate2, // внутри mutex (вторая корутина после того, как первая
                // освободила mutex)
            )
        )

        coEveryGetSyncedCurrencyIds(currencies, today, emptyList())

        coEveryGetSupportedCurrencies(supportedCurrencies)

        coEvery {
            remoteDataSource.getRates(
                baseIsoCode = baseCurrency.isoCode,
                quoteIsoCodes = "RUB",
                from = today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE)
                    .toString(),
                to = today.toString()
            )
        } coAnswers {
            // отправляем сигнал о том, что первая корутина дошла до getRates()
            apiStarted.complete(Unit)

            // удерживаем mutex (ждем, сигнала о том, что первой корутине можно продолжить
            // выполнение)
            releaseApi.await()

            remoteRates
        }

        coEveryInsertRates(listOf(rateEntity(Currency.RUB, today, "90.0")))

        coEveryMarkAsSynced(listOf(syncEntity(Currency.RUB, today)))

        coEveryGetClosestToDate(currencies, today, rateEntitiesClosestToDate)

        // Обращение к тестируемому provider
        // Первая корутина
        val firstRequest = async {
            provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency, quoteCurrencies = currencies
            )
        }

        // Ждем, сигнал от первой корутины о том, что она дошла до getRates()
        apiStarted.await()

        // Пока mutex занят, запускаем вторую корутину
        val secondRequest = async {
            provider.getAvailableRatesFromBaseToQuote(
                baseCurrency = baseCurrency, quoteCurrencies = currencies
            )
        }

        // просим планировщик запустить корутину (т.е. теперь корутина будет гарантированно
        // запущена)
        runCurrent()

        // Проверяем, что вторая корутина не обратилась к api, пока он занят первой корутиной
        coVerifyGetRemoteRates(1)

        // отправляем сигнал о том, что первая корутина может продолжить выполнение
        releaseApi.complete(Unit)

        // ждем завершение обеих корутин
        val firstResult = firstRequest.await()
        val secondResult = secondRequest.await()

        // Оцениваем результаты обеих корутин
        val expected = Resource.Success(
            listOf(rate(Currency.RUB, today, "90.0"))
        )

        assertEquals(expected, firstResult)
        assertEquals(expected, secondResult)

        //по 2 вызова для каждой корутины
        coVerifyGetRatesByDate(4, currencies, today)

        // 2 вызова в первой корутине, 1 - во второй
        coVerifyGetSyncedCurrencyIds(3, currencies, today)

        coVerifyGetSupportedCurrencies(1)// 1 вызов в первой корутине

        // 1 вызов в первой корутине
        coVerifyGetRemoteRates(
            1, "RUB",
            today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE), today
        )

        coVerifyInsertRates(// 1 вызов в первой корутине
            1, listOf(rateEntity(Currency.RUB, today, "90.0"))
        )

        // 1 вызов в первой корутине
        coVerifyMarkAsSynced(1, listOf(syncEntity(Currency.RUB, today)))

        // по 1 вызову в каждой корутине
        coVerifyGetRatesClosestToDate(2, currencies, today)
    }
    // endregion

    // region helpers
    private fun rateEntity(
        currency: Currency,
        date: LocalDate,
        rate: String
    ) = ExchangeRateEntity(
        baseCurrencyId = baseCurrency.id,
        quoteCurrencyId = currency.id,
        dateEpochDay = date.toEpochDay(),
        rateFromBaseToQuote = BigDecimal(rate)
    )

    private fun rateDto(
        currency: Currency,
        date: LocalDate,
        rate: String
    ) = ExchangeRateDto(
        baseIsoCode = baseCurrency.isoCode,
        quoteIsoCode = currency.isoCode,
        date = date.toString(),
        rate = BigDecimal(rate)
    )

    private fun syncEntity(
        currency: Currency,
        date: LocalDate
    ) = ExchangeRateSyncEntity(
        baseCurrencyId = baseCurrency.id,
        quoteCurrencyId = currency.id,
        dateEpochDay = date.toEpochDay(),
    )

    private fun rate(
        currency: Currency,
        date: LocalDate,
        rate: String
    ) = ExchangeRate(
        sourceCurrency = baseCurrency,
        targetCurrency = currency,
        rateDate = date,
        rateValue = BigDecimal(rate)
    )


    private fun coEveryGetRatesByDate(
        currencies: Set<Currency>,
        date: LocalDate,
        rates: List<ExchangeRateEntity>
    ) {
        coEvery {
            exchangeRateDao.getRatesByDate(
                baseCurrencyId = baseCurrency.id,
                quoteCurrencyIds = currencies.map { it.id },
                dateEpochDay = date.toEpochDay()
            )
        } returns rates
    }

    private fun coEveryGetRatesByDateMany(
        currencies: Set<Currency>,
        date: LocalDate,
        ratesMany: List<List<ExchangeRateEntity>>
    ) {
        coEvery {
            exchangeRateDao.getRatesByDate(
                baseCurrencyId = baseCurrency.id,
                quoteCurrencyIds = currencies.map { it.id },
                dateEpochDay = date.toEpochDay()
            )
        } returnsMany ratesMany
    }

    private fun coEveryGetSyncedCurrencyIds(
        currencies: Set<Currency>,
        date: LocalDate,
        ids: List<Long>
    ) {
        coEvery {
            exchangeRateDao.getSyncedCurrencyIds(
                baseCurrencyId = baseCurrency.id,
                quoteCurrencyIds = currencies.map { it.id },
                dateEpochDay = date.toEpochDay()
            )
        } returns ids
    }

    private fun coEveryGetSyncedCurrencyIdsMany(
        currencies: Set<Currency>,
        date: LocalDate,
        idsMany: List<List<Long>>
    ) {
        coEvery {
            exchangeRateDao.getSyncedCurrencyIds(
                baseCurrencyId = baseCurrency.id,
                quoteCurrencyIds = currencies.map { it.id },
                dateEpochDay = date.toEpochDay()
            )
        } returnsMany idsMany
    }

    private fun coEveryGetSupportedCurrencies(
        supportedCurrencies: SupportedCurrenciesResult
    ) {
        coEvery {
            supportedCurrenciesDataSource.getCurrencies()
        } returns supportedCurrencies
    }

    private fun coEveryGetRemoteRates(
        quoteIsoCodes: String? = null,
        from: LocalDate? = null,
        to: LocalDate? = null,
        remoteRates: NetworkResult<List<ExchangeRateDto>>
    ) {
        coEvery {
            remoteDataSource.getRates(
                baseIsoCode = baseCurrency.isoCode,
                quoteIsoCodes = quoteIsoCodes ?: any(),
                from = from?.toString() ?: any(),
                to = to?.toString() ?: any()
            )
        } returns remoteRates
    }

    private fun coEveryInsertRates(
        rates: List<ExchangeRateEntity>? = null,
    ) {
        coEvery {
            exchangeRateDao.insertRates(rates ?: any())
        } returns Unit
    }

    private fun coEveryMarkAsSynced(
        entities: List<ExchangeRateSyncEntity>? = null
    ) {
        coEvery {
            exchangeRateDao.markAsSynced(entities ?: any())
        } returns Unit
    }

    private fun coEveryGetClosestToDate(
        currencies: Set<Currency>,
        date: LocalDate,
        rates: List<ExchangeRateEntity>
    ) {
        coEvery {
            exchangeRateDao.getRatesClosestToDate(
                baseCurrencyId = baseCurrency.id,
                quoteCurrencyIds = currencies.map { it.id },
                dateEpochDay = date.toEpochDay()
            )
        } returns rates
    }

    private fun coVerifyGetRatesByDate(
        exactly: Int,
        currencies: Set<Currency>? = null,
        date: LocalDate? = null
    ) {
        coVerify(exactly = exactly) {
            exchangeRateDao.getRatesByDate(
                baseCurrencyId = baseCurrency.id,
                quoteCurrencyIds = currencies?.map { it.id } ?: any(),
                dateEpochDay = date?.toEpochDay() ?: any()
            )
        }
    }

    private fun coVerifyGetSyncedCurrencyIds(
        exactly: Int,
        currencies: Set<Currency>? = null,
        date: LocalDate? = null
    ) {
        coVerify(exactly = exactly) {
            exchangeRateDao.getSyncedCurrencyIds(
                baseCurrencyId = baseCurrency.id,
                quoteCurrencyIds = currencies?.map { it.id } ?: any(),
                dateEpochDay = date?.toEpochDay() ?: any()
            )
        }
    }

    private fun coVerifyGetSupportedCurrencies(
        exactly: Int,
    ) {
        coVerify(exactly = exactly) {
            supportedCurrenciesDataSource.getCurrencies()
        }
    }

    private fun coVerifyGetRemoteRates(
        exactly: Int,
        quoteIsoCodes: String? = null,
        from: LocalDate? = null,
        to: LocalDate? = null
    ) {
        coVerify(exactly = exactly) {
            remoteDataSource.getRates(
                baseIsoCode = baseCurrency.isoCode,
                quoteIsoCodes = quoteIsoCodes ?: any(),
                from = from?.toString() ?: any(),
                to = to?.toString() ?: any()
            )
        }
    }

    private fun coVerifyInsertRates(
        exactly: Int,
        rates: List<ExchangeRateEntity>? = null,
    ) {
        coVerify(exactly = exactly) {
            exchangeRateDao.insertRates(rates ?: any())
        }
    }

    private fun coVerifyMarkAsSynced(
        exactly: Int,
        entities: List<ExchangeRateSyncEntity>? = null,
    ) {
        coVerify(exactly = exactly) {
            exchangeRateDao.markAsSynced(entities ?: any())
        }
    }

    private fun coVerifyGetRatesClosestToDate(
        exactly: Int,
        currencies: Set<Currency>? = null,
        date: LocalDate? = null
    ) {
        coVerify(exactly = exactly) {
            exchangeRateDao.getRatesClosestToDate(
                baseCurrencyId = baseCurrency.id,
                quoteCurrencyIds = currencies?.map { it.id } ?: any(),
                dateEpochDay = date?.toEpochDay() ?: any()
            )
        }
    }

    private fun assertSuccessRates(
        expected: Set<ExchangeRate>,
        result: Resource<List<ExchangeRate>, ExchangeRateFailure>,
    ) {
        // Проверяем содержимое списка, а не его порядок
        val success = result as Resource.Success
        assertEquals(expected, success.data.toSet())
        assertEquals(expected.size, success.data.size)
    }
    // endregion

    companion object {
        private const val AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE = 6L
    }
}
