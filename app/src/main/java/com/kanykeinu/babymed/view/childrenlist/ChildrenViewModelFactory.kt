package com.kanykeinu.babymed.view.childrenlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ChildrenViewModelFactory @Inject constructor(private val childrenViewModel: ChildrenViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChildrenViewModel::class.java)){
            return childrenViewModel as T
        }
        throw IllegalStateException("Unknown class name")
    }
}