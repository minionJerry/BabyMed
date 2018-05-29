package com.kanykeinu.babymed.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "illness",
        foreignKeys = arrayOf(ForeignKey(
        entity = Child::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("childId"),
        onDelete = ForeignKey.CASCADE)))
data class Illness(
        @PrimaryKey(autoGenerate = true) val id : Long,
        val name : String,
        val symptoms : String,
        val treatment : String?,
        val treatmentPhotoUri : String?,
        val childId : Long)
