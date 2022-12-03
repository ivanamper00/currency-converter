package com.amper.currencyexchanger.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.amper.currencyexchanger.R
import com.amper.currencyexchanger.common.toDecimalFormat
import com.amper.currencyexchanger.data.dto.Transaction
import com.amper.currencyexchanger.databinding.ListBalancesBinding
import com.amper.currencyexchanger.databinding.ListTransactionsBinding
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(private var list: List<Transaction>): Adapter<TransactionAdapter.Holder>() {

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = ListTransactionsBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_transactions, parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.binding){
            val current = list[position]
            val date = Date(current.dateMilis ?: 0)
            transactionDate.text = SimpleDateFormat("MM/dd/yyyy", Locale.ROOT).format(date)
            fromAmount.text = root.context.getString(R.string.minus_amount,
                root.context.getString(R.string.two_concat, current.fromAmount?.toDecimalFormat(), current.fromCurrency)
            )
            toAmount.text =  root.context.getString(R.string.plus_amount,
                root.context.getString(R.string.two_concat, current.toAmount?.toDecimalFormat(), current.toCurrency)
            )
            Log.d("onBindViewHolder", "onBindViewHolder: ${current.commission?.toInt()}")
            commissionRate.text = root.context.getString(
                R.string.commission,
                current.commission?.toDecimalFormat("##0.00"),
                current.fromCurrency)
        }
    }

    fun setData(list: List<Transaction>){
        this.list = list
        notifyItemRangeChanged(0, list.size)
    }

    override fun getItemCount(): Int = list.size
}