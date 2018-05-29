package com.kanykeinu.babymed.util

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import com.kanykeinu.babymed.R

class DialogUtil {

    companion object {
        fun genderDialog(context: Context, sexInput: EditText) {
            val genders = arrayOf(context.getString(R.string.male), context.getString(R.string.female))
            val alert = AlertDialog.Builder(context, R.style.DialogTheme)
            alert.setTitle(R.string.select_gender)
            alert.setSingleChoiceItems(genders, -1) { dialog, which ->
                val chosenSex = genders[which].toString()
                sexInput.setText(chosenSex)
                dialog.dismiss()
            }
            alert.show()
        }
    }
}