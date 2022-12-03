package com.amper.currencyexchanger.data.repository

import com.amper.currencyexchanger.common.ResponseEvent
import com.amper.currencyexchanger.data.dto.ExchangeRateResponseModel
import com.amper.currencyexchanger.data.remote.CurrencyService
import com.amper.currencyexchanger.domain.repository.CurrencyRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CurrencyRepoImp(
    private val service: CurrencyService
): CurrencyRepo {

    override suspend fun getExchangeRates(): Flow<ResponseEvent<ExchangeRateResponseModel>> = callbackFlow {
        val result = service.getExchangeRates()
        if(result.isSuccessful){
            trySend(ResponseEvent.Success(result.body()!!))
        }else trySend(ResponseEvent.Error("${result.message()} : ${result.code()}"))
        awaitClose()
    }
}