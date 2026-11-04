package com.example.harmoney.data.network

import com.example.harmoney.data.currencyList.network.dto.CurrencyDto
import com.example.harmoney.data.exchangeRate.netwok.dto.ExchangeRateDto
import kotlinx.coroutines.CancellationException
import java.io.IOException

class FrankfurterRemoteDataSourceImpl(
    private val frankfurterApi: FrankfurterApi,
) : RemoteDataSource {

    override suspend fun getCurrencies(): NetworkResult<List<CurrencyDto>> {
        return executeRequest { frankfurterApi.getCurrencies() }
    }

    override suspend fun getRate(
        baseIsoCode: String,
        quoteIsoCode: String,
        date: String?
    ): NetworkResult<ExchangeRateDto> {
        return executeRequest {
            frankfurterApi.getRate(
                baseIsoCode = baseIsoCode,
                quoteIsoCode = quoteIsoCode,
                date = date
            )
        }
    }

    override suspend fun getRates(
        baseIsoCode: String,
        quoteIsoCodes: String,
        from: String?,
        to: String?
    ): NetworkResult<List<ExchangeRateDto>> {
        return executeRequest {
            frankfurterApi.getRates(
                baseIsoCode = baseIsoCode,
                quoteIsoCodes = quoteIsoCodes,
                from = from,
                to = to
            )
        }
    }

    @Suppress("detekt:TooGenericExceptionCaught")
    private suspend fun <T> executeRequest(
        request: suspend () -> retrofit2.Response<T>
    ): NetworkResult<T> {

        return try {
            val response = request()

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    NetworkResult.Success(body)
                } else {
                    NetworkResult.UnexpectedError(
                        IllegalStateException(
                            "Successful response has empty body"
                        )
                    )
                }
            } else {
                NetworkResult.HttpError(
                    code = response.code(),
                    message = response.errorBody()?.string(),
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            NetworkResult.ConnectionError(e)
        } catch (e: Exception) {
            NetworkResult.UnexpectedError(e)
        }
    }
}
