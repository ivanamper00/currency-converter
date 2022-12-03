package com.amper.currencyexchanger.data.remote

import com.amper.currencyexchanger.data.dto.ExchangeRateResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyService {

    @GET("currency-exchange-rates")
    suspend fun getExchangeRates() : Response<ExchangeRateResponseModel>

}