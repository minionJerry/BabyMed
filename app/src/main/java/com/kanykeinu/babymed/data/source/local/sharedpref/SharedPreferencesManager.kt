package com.kanykeinu.babymed.data.source.local.sharedpref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Contacts

class SharedPreferencesManager(context: Context) {

    val USER_ID: String = "USER_ID"
    private var sharedpreferences: SharedPreferences? = null
    private var editor : SharedPreferences.Editor? = null

    init {
        this.sharedpreferences = context.getSharedPreferences(Contacts.SettingsColumns.KEY, Context.MODE_PRIVATE)
        editor = sharedpreferences?.edit()
    }

    fun saveUserId(userId: String?) {
        editor?.putString(USER_ID, userId)
        editor?.apply()
    }

    fun getUserId() : String? {
        return sharedpreferences?.getString(USER_ID,null)
    }
}