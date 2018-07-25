package com.kanykeinu.babymed.utils


import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import es.dmoral.toasty.Toasty

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(getAdapterPosition(), getItemViewType())
    }
    return this
}

fun Context.showSuccessToast(text : CharSequence, duration: Int = Toast.LENGTH_LONG){
    Toasty.success(this, text, duration, true).show()
}

fun Context.showErrorToast(text : CharSequence, duration: Int = Toast.LENGTH_LONG){
    Toasty.error(this, text, duration, true).show()
}

fun Context.showInfoToast(text : CharSequence, duration: Int = Toast.LENGTH_LONG){
    Toasty.info(this, text, duration, true).show()
}

fun Context.showWarningToast(text : CharSequence, duration: Int = Toast.LENGTH_LONG){
    Toasty.warning(this, text, duration, true).show()
}