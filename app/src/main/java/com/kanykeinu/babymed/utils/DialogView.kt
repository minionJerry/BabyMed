package com.kanykeinu.babymed.utils

import android.app.AlertDialog
import android.content.Context
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
    }
}