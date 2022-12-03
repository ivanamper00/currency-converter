package com.amper.currencyexchanger.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.amper.currencyexchanger.R
import com.amper.currencyexchanger.common.toDecimalFormat
import com.amper.currencyexchanger.databinding.ListBalancesBinding

class BalancesAdapter(private var list: List<Pair<String,Double>>): Adapter<BalancesAdapter.Holder>() {

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = ListBalancesBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_balances, parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.binding){
            val current = list[position]
            balanceText.text = root.context.getString(R.string.two_concat, current.second.toDecimalFormat(), current.first)
        }
    }

    fun setData(list: List<Pair<String,Double>>){
        this.list = list
        notifyItemRangeChanged(0, list.size)
    }

    override fun getItemCount(): Int = list.size
}