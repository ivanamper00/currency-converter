package com.amper.currencyexchanger.domain.interactor

import com.amper.currencyexchanger.domain.repository.LocalRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTransactions @Inject constructor(
    private val localRepo: LocalRepo
) {
    operator fun invoke(id: Long) = localRepo.getTransactions(id)
}