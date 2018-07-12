package com.kanykeinu.babymed.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.EditText
import com.kanykeinu.babymed.R
import java.lang.ref.WeakReference

class DialogView {

    companion object {
        fun genderDialog(context: Context, sexInput: WeakReference<EditText>) {
            val genders = arrayOf(context.getString(R.string.male), context.getString(R.string.female))
            val alert = AlertDialog.Builder(context, R.style.DialogTheme)
            alert.setTitle(R.string.select_gender)
            alert.setSingleChoiceItems(genders, -1) { dialog, which ->
                val chosenSex = genders[which].toString()
                sexInput.get()?.setText(chosenSex)
                dialog.dismiss()
            }
            alert.show()
        }

        fun deletingConfirm(context: Context, onDialogItemSelected: OnDialogItemSelected) {
            val alert = AlertDialog.Builder(context, R.style.DialogTheme)
            alert.setTitle("Предупреждение!")
            alert.setMessage("Вы уверены, что хотите безвозвратно удалить данные об этом ребенке?")
            alert.setPositiveButton("Да", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    onDialogItemSelected.onDialogClicked()
                }

            })
            alert.setNegativeButton("Нет",object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }

            })
            alert.show()
        }
    }
}

interface OnDialogItemSelected{
    fun onDialogClicked();
}