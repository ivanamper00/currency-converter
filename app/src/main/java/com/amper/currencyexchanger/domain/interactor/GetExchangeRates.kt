package com.amper.currencyexchanger.domain.interactor

import com.amper.currencyexchanger.domain.repository.CurrencyRepo
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class GetExchangeRates @Inject constructor(
    private val currencyRepo: CurrencyRepo
) {
    suspend operator fun invoke() = currencyRepo.getExchangeRates()
}