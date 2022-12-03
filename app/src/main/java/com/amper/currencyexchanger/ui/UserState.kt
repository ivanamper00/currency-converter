package com.amper.currencyexchanger.ui

import com.amper.currencyexchanger.common.UiState
import com.amper.currencyexchanger.data.dto.Transaction
import com.amper.currencyexchanger.data.dto.UserModel

class UserState: UiState() {
    data class UserLoaded(val data: UserModel,val isNew: Boolean): UiState()
    data class AllTransactions(val transactions: List<Transaction>): UiState()
    data class TransactionSaved(val transaction: Transaction): UiState()
    data class TransactionFailed(val details: String): UiState()
}