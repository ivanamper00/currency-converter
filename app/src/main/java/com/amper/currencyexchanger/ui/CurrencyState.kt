package com.amper.currencyexchanger.ui

import com.amper.currencyexchanger.common.UiState
import com.amper.currencyexchanger.data.dto.ExchangeRateResponseModel

class CurrencyState: UiState() {
    data class ExchangeResponse(val data: ExchangeRateResponseModel): UiState()
}