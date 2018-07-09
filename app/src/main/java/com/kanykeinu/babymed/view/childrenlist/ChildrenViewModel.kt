package com.kanykeinu.babymed.view.childrenlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kanykeinu.babymed.data.source.BabyMedRepository
import com.kanykeinu.babymed.data.source.local.entity.Child
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChildrenViewModel @Inject constructor(private val babyMedRepository: BabyMedRepository) : ViewModel() {
    private var childrenResult: MutableLiveData<List<Child>> = MutableLiveData()
    private var childrenError: MutableLiveData<String> = MutableLiveData()
    private var childrenLoader: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var disposableObserver: DisposableObserver<List<Child>>

    fun onSuccess() : LiveData<List<Child>> {
        return childrenResult
    }

    fun onError() : LiveData<String>{
        return childrenError
    }

    fun onLoader() : LiveData<Boolean>{
        return childrenLoader
    }

    fun getChildrenList(){
        disposableObserver = object : DisposableObserver<List<Child>>(){
            override fun onComplete() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onNext(t: List<Child>) {
                childrenResult.postValue(t)
                childrenLoader.postValue(false)
            }

            override fun onError(e: Throwable) {
                childrenError.postValue(e.message)
                childrenLoader.postValue(false)
            }

        }

    babyMedRepository.getChildrenFromDb()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400,TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun disposeObserver(){
        if (!disposableObserver.isDisposed)
            disposableObserver.dispose()
    }
}