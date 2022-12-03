package com.amper.currencyexchanger.data.repository

import com.amper.currencyexchanger.data.dto.Transaction
import com.amper.currencyexchanger.data.dto.UserModel
import com.amper.currencyexchanger.data.local.LocalAppDatabase
import com.amper.currencyexchanger.data.local.LocalDao
import com.amper.currencyexchanger.domain.repository.LocalRepo
import kotlinx.coroutines.flow.Flow

class LocalRepoImp(
    private val dao: LocalDao
): LocalRepo {

    override fun getUsers(): Flow<List<UserModel>> = dao.getUsers()

    override fun getTransactions(id: Long): Flow<List<Transaction>> = dao.getTransactions(id)

    override suspend fun addTransaction(transaction: Transaction) = dao.addTransaction(transaction)

    override suspend fun addUser(user: UserModel) = dao.addUser(user)
}