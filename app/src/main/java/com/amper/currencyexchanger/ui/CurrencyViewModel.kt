package com.amper.currencyexchanger.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amper.currencyexchanger.common.ResponseEvent
import com.amper.currencyexchanger.common.UiEvent
import com.amper.currencyexchanger.common.UiState
import com.amper.currencyexchanger.data.dto.Transaction
import com.amper.currencyexchanger.data.dto.UserModel
import com.amper.currencyexchanger.domain.interactor.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getExchangeRates: GetExchangeRates,
    private val addUserInteractor: AddUser,
    private val addTransactionInteractor: AddTransaction,
    private val getUserInteractor: GetUser,
    private val getTransactionsInteractor: GetTransactions
): ViewModel() {

    private val _uiEvent = MutableLiveData<UiEvent>()
    val uiEvent: LiveData<UiEvent>
    get() = _uiEvent

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState>
        get() = _uiState

    private var defaultUser = UserModel(
        id = 1,
        baseQuantity = 1000.00,
        currencies = HashMap()
    )

    private var commissionPercent = 0.007

    fun getRates(){
        viewModelScope.launch {
            getExchangeRates.invoke()
                .onStart { _uiEvent.value = UiEvent.Loading(true) }
                .collectLatest {
                    when(it){
                        is ResponseEvent.Error -> _uiEvent.value = UiEvent.Error(it.error)
                        is ResponseEvent.Success -> _uiState.value = CurrencyState.ExchangeResponse(it.data)
                    }
                    _uiEvent.value = UiEvent.Loading(false)
                }
        }
    }

    fun getUser(){
        viewModelScope.launch {
            getUserInteractor.invoke()
                .onStart { _uiEvent.value = UiEvent.Loading(true) }
                .collectLatest {
                    if(it.isEmpty()){
                        _uiState.value = UserState.UserLoaded(defaultUser, true)
                        addUser()
                    }else {
                        defaultUser = it.first()
                        _uiState.value = UserState.UserLoaded(defaultUser, false)
                    }
                    _uiEvent.value = UiEvent.Loading(false)
                }
        }
    }

    fun getTransactions() {
        viewModelScope.launch {
            getTransactionsInteractor(defaultUser.id ?: 0)
                .onStart { _uiEvent.value = UiEvent.Loading(true) }
                .collectLatest {
                    _uiState.value = UserState.AllTransactions(it)
                    _uiEvent.value = UiEvent.Loading(false)
                }
        }
    }

    fun addTransaction(transaction: Transaction){
        transaction.userId = defaultUser.id
        viewModelScope.launch {
            if(calculateTransaction(transaction)){
                addTransactionInteractor(transaction)
                _uiState.value = UserState.TransactionSaved(transaction)
            }
        }
    }

    private fun calculateTransaction(transaction: Transaction) : Boolean {
        if(transaction.fromCurrency != defaultUser.baseCurrency && transaction.toCurrency != defaultUser.baseCurrency) {
            _uiState.value = UserState.TransactionFailed("Cannot proceed, Invalid exchange!")
            return false
        }

        if((transaction.fromAmount ?: 0.00) <= 0.00) {
            _uiState.value = UserState.TransactionFailed("Sell amount cannot be 0")
            return false
        }

        if(transaction.toCurrency == transaction.fromCurrency){
            _uiState.value = UserState.TransactionFailed("Cannot exchange same currency")
            return false
        }

        with(defaultUser){
            // less from
            val currentFrom = currencies?.get(transaction.fromCurrency)
            val commission = if(transaction.hasCommission == true) (currentFrom ?: 0.00) * commissionPercent else 0.00
            val newFrom = (currentFrom ?: 0.00) - (transaction.fromAmount ?: 0.00) - commission
            if(newFrom < 0) {
                _uiState.value = UserState.TransactionFailed("Insufficient amount of ${transaction.fromCurrency}")
                return false
            }
            currencies?.put(transaction.fromCurrency ?: "", newFrom)

            // add to
            val currentTo = currencies?.get(transaction.toCurrency)
            val newTo = (currentTo ?: 0.00) + (transaction.toAmount ?: 0.00)
            currencies?.put(transaction.toCurrency ?: "", newTo)

            transaction.commission = commission
        }
        addUser()
        return true
    }


    fun addUser(){
        viewModelScope.launch {
            addUserInteractor(defaultUser)
        }
    }

    fun updateCurrencyList(currencyList: MutableList<String>, base: String) {
        if(defaultUser.currencies.isNullOrEmpty()){
            defaultUser.baseCurrency = base
            currencyList.forEach {
                defaultUser.currencies?.put(it, if(it == base) 1000.00 else 0.00)
            }
            addUser()
        }
    }

    fun getUserCurrencies() = defaultUser.currencies

}