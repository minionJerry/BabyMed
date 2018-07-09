package com.kanykeinu.babymed.view.addeditchild

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kanykeinu.babymed.data.source.BabyMedRepository
import com.kanykeinu.babymed.data.source.local.entity.Child
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddEditChildViewModel @Inject constructor(private val babyMedRepository: BabyMedRepository) : ViewModel() {

    private var addChildError: MutableLiveData<String> = MutableLiveData()
    private var addChildComplete : MutableLiveData<Boolean> = MutableLiveData()
    lateinit var  disposableObserver : DisposableObserver<Unit>

    fun saveChild(child : Child)  {
        disposableObserver = object : DisposableObserver<Unit>(){
            override fun onComplete() {
                addChildComplete.postValue(true)
            }

            override fun onNext(t: Unit) {
            }

            override fun onError(e: Throwable) {
                addChildError.postValue(e.message)
            }
        }

        babyMedRepository.insertChildToDb(child)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(disposableObserver)
    }

    fun onComplete() : LiveData<Boolean> {
        return addChildComplete
    }

    fun onError() : LiveData<String> {
        return addChildError
    }

    fun disposeObserver(){
        if (!disposableObserver.isDisposed)
            disposableObserver.dispose()
    }
}