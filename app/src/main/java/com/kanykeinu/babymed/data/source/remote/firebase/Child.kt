package com.kanykeinu.babymed.data.source.remote.firebase

import android.os.Parcelable
import android.os.Parcel
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Child(
        val name: String,
        val birthDate: String,
        val gender: String?,
        val weight: Int?,
        val photoUri: String?,
        val bloodType: Int?,
        val userId : String?,
        val illnessList: ArrayList<Illness>?) : Parcelable{

    constructor() : this("","","",0,"",0,"",null)


    @Exclude
    fun toMap(): Map<String, Any?> {
        val result = HashMap<String,Any?>()
        result.put("name", name)
        result.put("birthDate", birthDate)
        result.put("gender", gender)
        result.put("weight", weight)
        result.put("photoUri", photoUri)
        result.put("bloodType", bloodType)
        result.put("illnessList", illnessList)

        return result
    }
}