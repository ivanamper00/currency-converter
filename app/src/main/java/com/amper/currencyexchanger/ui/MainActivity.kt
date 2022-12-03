package com.amper.currencyexchanger.ui

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amper.currencyexchanger.R
import com.amper.currencyexchanger.common.*
import com.amper.currencyexchanger.common.ui.PromptDialogFragment
import com.amper.currencyexchanger.common.ui.LoadingDialog
import com.amper.currencyexchanger.data.dto.Transaction
import com.amper.currencyexchanger.databinding.ActivityMainBinding
import com.amper.currencyexchanger.ui.adapter.BalancesAdapter
import com.amper.currencyexchanger.ui.adapter.TransactionAdapter
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel : CurrencyViewModel

    private val loading by lazy { LoadingDialog(this) }

    private var exchangeRates = HashMap<String, Double>()

    private var currencyList = mutableListOf<String>()

    private var userCurrencyBalance: List<Pair<String,Double>>? = null

    private lateinit var balanceAdapter : BalancesAdapter

    private lateinit var transactionAdapter : TransactionAdapter

    private var transactionsList: List<Transaction>? = emptyList()

    private var baseCurrency = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[CurrencyViewModel::class.java]
        setContentView(binding.root)

        viewModel.uiEvent.observe(this){ event ->
            when(event){
                is UiEvent.Error -> showErrorMessage(event.error, true)
                is UiEvent.Loading -> showLoading(event.isLoading)
                else -> {}
            }
        }

        viewModel.uiState.observe(this){ state ->
            when(state){
                is CurrencyState.ExchangeResponse -> {
                    Log.d(TAG, Gson().toJson(state.data))
                    exchangeRates = state.data.rates ?: HashMap()
                    baseCurrency = state.data.base ?: ""
                    currencyList = exchangeRates.map { it.key }.sortedByDescending { it == baseCurrency }.toMutableList()
                    with(binding){
                        sellContainer.suffixText = state.data.base
                        receivedContainer.suffixText = state.data.base
                    }
                    viewModel.updateCurrencyList(currencyList, state.data.base ?: "")
                    updateUserBalances(viewModel.getUserCurrencies()?.filterValues { it > 0.00 }?.toList())
                }
                is UserState.UserLoaded -> {
                    updateUserBalances(state.data.currencies?.filterValues { it > 0.00 }?.toList())
                }
                is UserState.TransactionSaved -> {
                    showSuccessMessage(state.transaction)
                }
                is UserState.AllTransactions -> {
                    state.transactions.let {
                        transactionsList = it.sortedByDescending { trans -> trans.dateMilis }
                        transactionAdapter.setData(transactionsList ?: emptyList())
                    }
                }
                is UserState.TransactionFailed -> showErrorMessage(state.details, false)
            }
        }



        viewModel.getUser()
        viewModel.getTransactions()
        if(isNetworkConnected()) viewModel.getRates()
        else {
            noInternetPrompt(getString(R.string.no_internet_error_message))
        }

        setupViews()
    }

    private fun updateUserBalances(toList: List<Pair<String, Double>>?) {
        toList?.let {
            Log.d(TAG, "updateUserBalances: ${Gson().toJson(toList)}")
            userCurrencyBalance = it
            balanceAdapter.setData(userCurrencyBalance ?: emptyList())
        }
    }

    private fun showErrorMessage(message: String?, hasExit: Boolean) {
        PromptDialogFragment(
            getString(R.string.error_label),
            message ?: "",
            if(hasExit) getString(R.string.exit_label) else null
        ) { dialog, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> dialog.dismiss()
                else -> finish()
            }
        }.show(supportFragmentManager, PromptDialogFragment.TAG)
    }

    private fun noInternetPrompt(message: String?) {
        PromptDialogFragment(
            getString(R.string.no_internet),
            message ?: "",
            getString(R.string.exit_label),
            getString(R.string.retry)
        ) { dialog, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    dialog.dismiss()
                    recreate()
                }
                else -> finish()
            }
        }.show(supportFragmentManager, PromptDialogFragment.TAG)
    }

    fun showSuccessMessage(
        transaction: Transaction
    ){
        PromptDialogFragment(
            getString(R.string.success_message),
            getString(
                R.string.success_description,
                transaction.fromAmount.toString(),
                transaction.fromCurrency ?: "",
                transaction.toAmount.toString(),
                transaction.toCurrency ?: "",
                transaction.commission.toString(),
                transaction.fromCurrency.toString()
            )
        ){ dialog, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.getTransactions()
                    dialog.dismiss()
                }
            }
        }.show(supportFragmentManager, PromptDialogFragment.TAG)
    }

    private fun setupViews() {
        with(binding){
            sellText.addTextChangedListener(CurrencyTextWatcher())
            sellText.addDefaultValue("0")
            receivedText.addDefaultValue("0")
            sellText.textChanges()
                .debounce(300)
                .onEach {
                    receivedText.setText(
                        convertData(
                            it.toString(),
                            binding.sellContainer.suffixText.toString(),
                            binding.receivedContainer.suffixText.toString(),
                            baseCurrency
                        )
                    )
                }
                .launchIn(lifecycleScope)


            receivedContainer.setEndIconOnClickListener {
                openSelector(receivedContainer, currencyList)
            }

            sellContainer.setEndIconOnClickListener {
                openSelector(sellContainer, userCurrencyBalance?.map { it.first } ?: emptyList())
            }

            balanceAdapter = BalancesAdapter(userCurrencyBalance ?: emptyList())
            with(balanceRecycler){
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = balanceAdapter
            }

            transactionAdapter = TransactionAdapter(transactionsList ?: emptyList())
            with(transactionsRecycler){
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = transactionAdapter
            }

            submitButton.setOnClickListener {
                viewModel.addTransaction(
                    Transaction(
                        fromCurrency = sellContainer.suffixText.toString(),
                        fromAmount = sellText.text.toString().toDouble(),
                        toCurrency = receivedContainer.suffixText.toString(),
                        toAmount = cleanAmount(receivedText.text.toString()).toDouble(),
                        dateMilis = Date().time,
                        hasCommission = getCommisionRule(),
                    )
                )
            }
        }
    }

    private fun getCommisionRule(): Boolean {
        return (transactionsList?.size ?: 0) >= 5
    }

    private fun convertData(amountToSell: String?, sellCurrency: String, receivedCur: String, baseCurrency: String): String {
        if(sellCurrency != baseCurrency && receivedCur != baseCurrency)  return "0"
        if(amountToSell.isNullOrEmpty() || amountToSell == "0") return "0"
        val sellAmount = amountToSell.toDouble()
        val currencyToReceived = if(baseCurrency == sellCurrency) receivedCur else sellCurrency
        val exchangeMultiplier = exchangeRates[currencyToReceived] ?: 0.0
        val toReceivedAmount = if(sellCurrency == baseCurrency) sellAmount * exchangeMultiplier
        else sellAmount / exchangeMultiplier
        return getString(R.string.plus_amount, toReceivedAmount.toDecimalFormat())
    }

    fun openSelector(container: TextInputLayout, list: List<String>){
        CurrencySelectorDialog(list) {
            container.suffixText = list[it]
            binding.receivedText.setText(
                convertData(
                    binding.sellText.text.toString(),
                    binding.sellContainer.suffixText.toString(),
                    binding.receivedContainer.suffixText.toString(),
                    baseCurrency
                )
            )

        }.show(supportFragmentManager, CurrencySelectorDialog.TAG)
    }

    private fun cleanAmount(amount: String): String{
        return  if(amount.contains("+")) amount.replace("+", "") else amount
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading) loading.show()
        else loading.dismiss()
    }

}