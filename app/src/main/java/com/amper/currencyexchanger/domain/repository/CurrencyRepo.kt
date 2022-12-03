package com.amper.currencyexchanger.domain.repository

import com.amper.currencyexchanger.common.ResponseEvent
import com.amper.currencyexchanger.data.dto.ExchangeRateResponseModel
import kotlinx.coroutines.flow.Flow

interface CurrencyRepo {

    suspend fun getExchangeRates() : Flow<ResponseEvent<ExchangeRateResponseModel>>
}