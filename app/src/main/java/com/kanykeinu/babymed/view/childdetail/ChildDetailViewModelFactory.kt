package com.kanykeinu.babymed.view.childdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kanykeinu.babymed.view.addeditchild.AddEditChildViewModel
import javax.inject.Inject

class ChildDetailViewModelFactory  @Inject constructor(private val childDetailViewModel: ChildDetailViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChildDetailViewModel::class.java)){
            return childDetailViewModel as T
        }
        throw IllegalStateException("Unknown class name")
    }
}