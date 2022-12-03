package com.amper.currencyexchanger.domain.repository

import com.amper.currencyexchanger.data.dto.Transaction
import com.amper.currencyexchanger.data.dto.UserModel
import kotlinx.coroutines.flow.Flow

interface LocalRepo {
    fun getUsers(): Flow<List<UserModel>>

    fun getTransactions(id: Long): Flow<List<Transaction>>

    suspend fun addTransaction(transaction: Transaction)

    suspend fun addUser(user: UserModel)
}