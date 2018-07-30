package com.kanykeinu.babymed.utils

import android.os.Environment

object Constants {
    const val CHILD : String = "CHILD"
    const val ILLNESS : String = "ILLNESS"
    const val REQUEST_CODE_CAMERA : Int = 1010
    const val REQUEST_CODE_GALLERY : Int = 1011
    const val DIRECTORY = "/babymed/Pictures"
    const val RAW_DIRECTORY = "/sdcard/babymed/Pictures"
    const val DATE_FORMAT = "dd MMMM yyyy"
    const val DATABASE_NAME = "BabyMed.db"
    val PHOTO_NAME = "IMG_" + System.currentTimeMillis() + ".jpg"
}