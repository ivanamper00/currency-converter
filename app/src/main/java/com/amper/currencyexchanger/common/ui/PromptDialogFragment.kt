package com.amper.currencyexchanger.common.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class PromptDialogFragment(
    private val title: String,
    private val message: String,
    private val negativeText: String? = null,
    private val positiveText: String? = null,
    private val onClick: DialogInterface.OnClickListener
): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton(positiveText ?: "Ok", onClick)
        if(negativeText != null) dialog.setNegativeButton(negativeText, onClick)
        return dialog.create()
    }

    companion object {
        const val TAG = "ErrorDialogFragment"
    }
}