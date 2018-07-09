package com.kanykeinu.babymed.view.addeditchild

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kanykeinu.babymed.view.childrenlist.ChildrenViewModel
import javax.inject.Inject

class AddEditChildViewModelFactory @Inject constructor(private val addEditChildViewModel : AddEditChildViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditChildViewModel::class.java)){
            return addEditChildViewModel as T
        }
        throw IllegalStateException("Unknown class name")
    }
}