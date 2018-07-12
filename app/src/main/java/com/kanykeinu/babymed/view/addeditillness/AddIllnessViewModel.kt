package com.kanykeinu.babymed.view.addeditillness

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kanykeinu.babymed.data.source.BabyMedRepository
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddIllnessViewModel @Inject constructor(private val babyMedRepository: BabyMedRepository) : ViewModel() {
    private var addIllnessError: MutableLiveData<String> = MutableLiveData()
    private var addIllnessComplete : MutableLiveData<Boolean> = MutableLiveData()
    lateinit var  disposableObserver : DisposableObserver<Unit>

    fun initDisposableObserver(){
        disposableObserver = object : DisposableObserver<Unit>(){
            override fun onComplete() {
                addIllnessComplete.postValue(true)
            }

            override fun onNext(t: Unit) {
            }

            override fun onError(e: Throwable) {
                addIllnessError.postValue(e.message)
            }
        }
    }

    fun saveIllness(illness : Illness)  {
        babyMedRepository.insertIllnessToBd(illness)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(disposableObserver)
    }

    fun onComplete() : LiveData<Boolean> {
        return addIllnessComplete
    }

    fun onError() : LiveData<String> {
        return addIllnessError
    }

    fun disposeObserver(){
        if (!disposableObserver.isDisposed)
            disposableObserver.dispose()
    }
}