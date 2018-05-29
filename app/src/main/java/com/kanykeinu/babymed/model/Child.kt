package com.kanykeinu.babymed.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "child")
data class Child(
        @PrimaryKey(autoGenerate = true) val id : Long,
         val name : String,
         val birthDate : String,
         val gender : String?,
         val weight : Int?,
         val photoUri : String?,
         val bloodType : Int?) : Parcelable