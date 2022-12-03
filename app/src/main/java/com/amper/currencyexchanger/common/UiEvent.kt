package com.amper.currencyexchanger.common

sealed class UiEvent {
    object Success: UiEvent()
    data class Error(val error: String): UiEvent()
    data class Loading(val isLoading: Boolean): UiEvent()
}