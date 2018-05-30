package com.kanykeinu.babymed.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

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
        @ColumnInfo(name = "child_id")
        val childId : Long)
