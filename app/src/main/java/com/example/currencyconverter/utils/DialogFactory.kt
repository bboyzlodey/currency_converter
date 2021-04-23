package com.example.currencyconverter.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.currencyconverter.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

//лучше создать не объект а класс наследуемый от AlertDialog.Builder тогда ты сможешь изпользовать его в layout
// и если хочешь вызывать его везде, создай  extension функциию Context.dialog и создавай там объект
object DialogFactory {
    //data class?
    class DialogData(
        val title: String,
        val neutralClicked: () -> Unit,
        val neutralButtonTitle: String? = null,
        val positiveButtonTitle: String? = null,
        val listData: List<String>,
        var selectedItem: Int,
        var itemSelectedListener: ((Int) -> Unit)
    )

    fun showDialog(context: Context, data: DialogData) {
        MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_Dialog)
            .setTitle(data.title)
            .setNeutralButton(data.neutralButtonTitle) { dialog, which -> data.neutralClicked.invoke() }
            .setPositiveButton(data.positiveButtonTitle) { dialog, _ ->
                data.itemSelectedListener.invoke(
                    (dialog as AlertDialog).listView.checkedItemPosition
                )
            }
            .setSingleChoiceItems(
                data.listData.toTypedArray(),
                data.selectedItem,
                null,
            )
            .show()
    }
}