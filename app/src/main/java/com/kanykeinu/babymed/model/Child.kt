package com.kanykeinu.babymed.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "child",indices = arrayOf(Index(value = ["name","birth_date"],
        unique = true)))
data class Child(
         @PrimaryKey(autoGenerate = true)
         val id : Long,
         val name : String,
         @ColumnInfo(name = "birth_date")
         val birthDate : String,
         val gender : String?,
         val weight : Int?,
         @ColumnInfo(name = "photo_uri")
         val photoUri : String?,
         @ColumnInfo(name = "blood_type")
         val bloodType : Int?) : Parcelable