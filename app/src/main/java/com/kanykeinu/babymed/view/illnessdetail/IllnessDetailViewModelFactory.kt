package com.kanykeinu.babymed.view.illnessdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class IllnessDetailViewModelFactory @Inject constructor(private val illnessDetailViewModel: IllnessDetailViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IllnessDetailViewModel::class.java)){
            return illnessDetailViewModel as T
        }
        throw IllegalStateException("Unknown class name")
    }
}