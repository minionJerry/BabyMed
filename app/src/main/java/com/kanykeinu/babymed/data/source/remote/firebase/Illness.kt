package com.kanykeinu.babymed.data.source.remote.firebase

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Illness(
        val name : String,
        val symptoms : String,
        val treatment : String,
        val treatmentPhotoUri : String?,
        val date : String,
        val illnessWeight : Int) : Parcelable{
}