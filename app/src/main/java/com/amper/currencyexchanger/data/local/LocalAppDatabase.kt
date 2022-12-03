package com.amper.currencyexchanger.data.local

import android.provider.ContactsContract.Data
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.amper.currencyexchanger.data.dto.Transaction
import com.amper.currencyexchanger.data.dto.UserModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(
    entities = [
        UserModel::class,
        Transaction::class
    ],
    version = 1
)
@TypeConverters(DataConverters::class)
abstract class LocalAppDatabase: RoomDatabase() {
    abstract val localDao: LocalDao

    companion object {
        const val DATABASE_NAME = "local_app_db"
    }
}

class DataConverters {

    @TypeConverter
    fun fromCurrenciesMap(map: HashMap<String, Double>) : String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun fromStringCurrencies(curStr: String): HashMap<String, Double> {
        val type = object : TypeToken<HashMap<String, Double>>() {}.type
        return Gson().fromJson(curStr, type)
    }
}