package com.amper.currencyexchanger.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @PrimaryKey val id: Long? = null,
    var baseQuantity: Double? = null,
    var baseCurrency: String? = null,
    var currencies: HashMap<String, Double>? = null
)