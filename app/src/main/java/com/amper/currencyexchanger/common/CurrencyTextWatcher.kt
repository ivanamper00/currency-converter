package com.amper.currencyexchanger.common

import android.text.Editable
import android.text.TextWatcher

class CurrencyTextWatcher : TextWatcher {

    // simple mutex
    private var isCursorRunning = false

    override fun afterTextChanged(s: Editable?) {
        if (isCursorRunning) {
            return
        }
        isCursorRunning = true

        s?.let {
            val split = it.toString().split(".")
            val onlyDigits = removeMask(split[0])
            val decimal = try {
                ".${applyDecimalMask(split[1])}"
            } catch (e: Exception) {
                ""
            }
            it.clear()
            it.append(onlyDigits + decimal)
        }

        isCursorRunning = false
    }

    private fun applyDecimalMask(value: String): String {
        val number = Regex("[a-zA-Z]").replace(value, "")
        return Regex("^[0-9]{3}").replace(number, "")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    private fun removeMask(value: String): String {
        // extract all the digits from the string
        return try {
            Regex("\\D+").replace(value, "").toInt().toString()
        }catch (e: Exception){
            Regex("\\D+").replace(value, "")
        }
    }

}