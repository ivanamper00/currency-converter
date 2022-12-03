package com.amper.currencyexchanger.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey val transactionId: Long? = null,
    var userId: Long? = null,
    val fromAmount: Double? = null,
    val fromCurrency: String? = null,
    val toAmount: Double? = null,
    val toCurrency: String? = null,
    val dateMilis: Long? = null,
    var commission: Double? = null,
    val hasCommission: Boolean? = false
)
