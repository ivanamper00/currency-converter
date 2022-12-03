package com.amper.currencyexchanger.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amper.currencyexchanger.data.dto.Transaction
import com.amper.currencyexchanger.data.dto.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {

    @Query("SELECT * FROM `UserModel`")
    fun getUsers(): Flow<List<UserModel>>

    @Query("SELECT * FROM `Transaction` WHERE userId = :id")
    fun getTransactions(id: Long): Flow<List<Transaction>>

    @Insert(entity = Transaction::class)
    suspend fun addTransaction(transaction: Transaction)

    @Insert(entity = UserModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UserModel)

}


