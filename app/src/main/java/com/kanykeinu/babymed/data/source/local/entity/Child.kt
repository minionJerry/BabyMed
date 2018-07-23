package com.kanykeinu.babymed.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kanykeinu.babymed.utils.Constants.DATE_FORMAT
import kotlinx.android.parcel.Parcelize
import org.joda.time.LocalDate
import org.joda.time.Years
import org.joda.time.format.DateTimeFormat

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
         val bloodType : Int?) : Parcelable{

  companion object {

      fun getCurrentAge(childBirthDate: String): Int {
          val birthDate = DateTimeFormat.forPattern(DATE_FORMAT)
          val date: LocalDate = birthDate.parseLocalDate(childBirthDate)
          val now = LocalDate() // test, in real world without args
          val age = Years.yearsBetween(date, now)
          return age.years
      }

      fun getCurrentDate(): String {
          val date: String = LocalDate().toString(DATE_FORMAT)
          return date
      }

      fun getIllnessAge(childBirthDate: String, illnessDateString : String) : Int{
          val dateFormat = DateTimeFormat.forPattern(DATE_FORMAT)
          val birthDate = dateFormat.parseLocalDate(childBirthDate)
          val illnessDate = dateFormat.parseLocalDate(illnessDateString)
          val illnessAge = Years.yearsBetween(birthDate,illnessDate)
          return illnessAge.years
      }
  }
}