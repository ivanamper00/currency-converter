package com.amper.currencyexchanger.domain.interactor

import com.amper.currencyexchanger.data.dto.UserModel
import com.amper.currencyexchanger.domain.repository.CurrencyRepo
import com.amper.currencyexchanger.domain.repository.LocalRepo
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddUser @Inject constructor(
    private val localRepo: LocalRepo
) {
    suspend operator fun invoke(user: UserModel) = localRepo.addUser(user)
}