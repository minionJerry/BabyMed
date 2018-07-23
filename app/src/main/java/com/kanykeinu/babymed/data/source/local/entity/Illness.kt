package com.kanykeinu.babymed.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "illness",
        foreignKeys = arrayOf(ForeignKey(
        entity = Child::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("child_id"),
        onDelete = ForeignKey.CASCADE)))
data class Illness(
        @PrimaryKey(autoGenerate = true)
        val id : Long,
        val name : String,
        val symptoms : String,
        val treatment : String,
        @ColumnInfo(name = "treatment_photo_uri")
        val treatmentPhotoUri : String?,
        val date : String,
        @ColumnInfo(name = "illness_weight")
        val illnessWeight : Int,
        @ColumnInfo(name = "child_id")
        val childId : Long) : Parcelable
