package com.kanykeinu.babymed.view.addeditchild

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kanykeinu.babymed.data.source.BabyMedRepository
import com.kanykeinu.babymed.data.source.local.entity.Child
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddEditChildViewModel @Inject constructor(private val babyMedRepository: BabyMedRepository) : ViewModel() {

    private var addChildError: MutableLiveData<String> = MutableLiveData()
    private var addChildComplete : MutableLiveData<Boolean> = MutableLiveData()
    lateinit var  disposableObserver : DisposableObserver<Unit>
    lateinit var disposableUpdateChildObserver: DisposableObserver<Unit>

    fun initDisposableObserver(){
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

        //observer for updating child`s data
        disposableUpdateChildObserver = object : DisposableObserver<Unit>(){
            override fun onComplete() {
                addChildComplete.postValue(true)
            }

            override fun onNext(t: Unit) {
            }

            override fun onError(e: Throwable) {
                addChildError.postValue(e.message)
            }

        }
    }

    fun saveChild(child : Child)  {
        babyMedRepository.insertChildToDb(child)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(disposableObserver)
    }

    fun updateChild(newChild: Child) {
        babyMedRepository.updateChild(newChild)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableUpdateChildObserver)

    }

    fun saveChildToFirebase(userId: String, child: Child){
        babyMedRepository.saveChildToFirebase(userId, child)
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
        if (!disposableUpdateChildObserver.isDisposed)
            disposableUpdateChildObserver.dispose()
    }
}