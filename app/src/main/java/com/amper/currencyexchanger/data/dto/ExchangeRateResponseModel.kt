package com.amper.currencyexchanger.data.dto

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponseModel(
   @SerializedName("base") val base: String? = null,
   @SerializedName("date") val date: String? = null,
   @SerializedName("rates") val rates: HashMap<String, Double>? = null,
)
