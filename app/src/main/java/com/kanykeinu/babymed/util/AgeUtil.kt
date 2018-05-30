package com.kanykeinu.babymed.util

import org.joda.time.LocalDate
import org.joda.time.Years
import org.joda.time.format.DateTimeFormat

class AgeUtil() {
    companion object {
        fun getCurrentAge(childBirthDate : String) : Int{
            val birthDate = DateTimeFormat.forPattern("dd MMM yyyy");
            val date : LocalDate = birthDate.parseLocalDate(childBirthDate)
            val now = LocalDate() // test, in real world without args
            val age = Years.yearsBetween(date, now)
            return age.years
        }

        fun getCurrentDate() : String{
            val date : String = LocalDate().toString("dd MMM yyyy")
            return date
        }
    }
}