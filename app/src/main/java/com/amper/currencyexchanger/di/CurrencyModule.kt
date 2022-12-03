package com.amper.currencyexchanger.di

import com.amper.currencyexchanger.data.remote.CurrencyService
import com.amper.currencyexchanger.data.repository.CurrencyRepoImp
import com.amper.currencyexchanger.domain.repository.CurrencyRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(ActivityRetainedComponent::class)
object CurrencyModule {

    @Provides
    @ActivityRetainedScoped
    fun providesCurrencyService(retrofit: Retrofit): CurrencyService {
        return retrofit.create()
    }

    @Provides
    @ActivityRetainedScoped
    fun providesCurrencyRepo(service: CurrencyService): CurrencyRepo {
        return CurrencyRepoImp(service)
    }
}