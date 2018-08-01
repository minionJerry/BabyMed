package com.kanykeinu.babymed.data.source.remote.firebase

import android.os.Parcelable
import com.kanykeinu.babymed.data.source.local.entity.Child
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User( var name : String,  var password : String,  var childList : List<Child>?) : Parcelable {

}
