package com.kanykeinu.babymed.view.singup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class UserViewModelFactory @Inject constructor(private val userViewModel: UserViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)){
            return userViewModel as T
        }
        throw IllegalStateException("Unknown class name")
    }
}