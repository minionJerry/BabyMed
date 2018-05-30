package com.kanykeinu.babymed

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.widget.Toast

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(getAdapterPosition(), getItemViewType())
    }
    return this
}

fun Context.showToast(text : CharSequence, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this,text,duration).show()
}