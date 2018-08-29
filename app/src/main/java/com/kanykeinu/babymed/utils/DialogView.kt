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

        fun bloodTypeDialog(context: Context, sexInput: WeakReference<EditText>) {
            val genders = arrayOf(context.getString(R.string.blood_type_0_pos), context.getString(R.string.blood_type_0_neg),
                    context.getString(R.string.blood_type_A_pos), context.getString(R.string.blood_type_A_neg),
                    context.getString(R.string.blood_type_B_pos), context.getString(R.string.blood_type_B_neg),
                    context.getString(R.string.blood_type_AB_pos), context.getString(R.string.blood_type_AB_neg))
            val alert = AlertDialog.Builder(context, R.style.DialogTheme)
            alert.setTitle(R.string.select_gender)
            alert.setSingleChoiceItems(genders, -1) { dialog, which ->
                val chosenSex = genders[which].toString()
                sexInput.get()?.setText(chosenSex)
                dialog.dismiss()
            }
            alert.show()
        }

        fun deletingConfirm(context: Context, message : String, onDialogItemSelected: OnDialogItemSelected) {
            val alert = AlertDialog.Builder(context, R.style.DialogTheme)
            alert.setTitle(context.getString(R.string.warning))
            alert.setMessage(message)
            alert.setPositiveButton(context.getString(R.string.yes), object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    onDialogItemSelected.onDialogClicked()
                }

            })
            alert.setNegativeButton(context.getString(R.string.no),object : DialogInterface.OnClickListener{
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