package com.kanykeinu.babymed.view.addeditillness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kanykeinu.babymed.view.addeditchild.AddEditChildViewModel
import javax.inject.Inject

class AddIllnessViewModelFactory @Inject constructor(private val addIllnessViewModel: AddIllnessViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddIllnessViewModel::class.java)){
            return addIllnessViewModel as T
        }
        throw IllegalStateException("Unknown class name")
    }
}