package com.amper.currencyexchanger.di

import com.amper.currencyexchanger.data.local.LocalAppDatabase
import com.amper.currencyexchanger.data.repository.LocalRepoImp
import com.amper.currencyexchanger.domain.repository.LocalRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesLocalRepo(
        localAppDatabase: LocalAppDatabase
    ): LocalRepo{
        return LocalRepoImp(localAppDatabase.localDao)
    }

}