package com.amper.currencyexchanger.common.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.amper.currencyexchanger.R
import com.amper.currencyexchanger.databinding.DialogLoadingBinding

class LoadingDialog(context: Context): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
    }
}