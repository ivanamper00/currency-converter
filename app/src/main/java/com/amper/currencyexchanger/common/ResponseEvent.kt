package com.amper.currencyexchanger.common

import java.lang.Exception

sealed class ResponseEvent<out DataType> {
    data class Success<DataType>(val data: DataType): ResponseEvent<DataType>()
    data class Error(val error: String): ResponseEvent<Nothing>()
}
