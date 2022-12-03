package com.amper.currencyexchanger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.amper.currencyexchanger.R
import com.amper.currencyexchanger.databinding.DialogSelectorBinding

class CurrencySelectorDialog(
    private val list: List<String>,
    private val itemSelectedListener: (Int) -> Unit
): DialogFragment() {

    private lateinit var binding: DialogSelectorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogSelectorBinding.bind(view)
        binding.listItem.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list)
        binding.listItem.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                itemSelectedListener.invoke(position)
                dismiss()
            }
    }

    companion object {
        const val TAG = "CurrencySelectorDialog"
    }
}