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
    private var sorttedChildrenResult : MutableLiveData<List<Child>> = MutableLiveData()
    private var sorttedChildrenError : MutableLiveData<String> = MutableLiveData()

    lateinit var disposableObserver: DisposableObserver<List<Child>>
    lateinit var sorttedChildrenObserver : DisposableObserver<List<Child>>

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

    fun getChildrenSorttedByName(){
        sorttedChildrenObserver = object : DisposableObserver<List<Child>>(){
            override fun onComplete() {
            }

            override fun onNext(t: List<Child>) {
                sorttedChildrenResult.postValue(t)
            }

            override fun onError(e: Throwable) {
                sorttedChildrenError.postValue(e.localizedMessage)
            }
        }

        babyMedRepository.getChildrenSortByName()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sorttedChildrenObserver)
    }

    fun getChildrenSorttedByBirthdateAsc(){
        sorttedChildrenObserver = object : DisposableObserver<List<Child>>()
        {
            override fun onComplete() {
            }

            override fun onNext(t: List<Child>) {
                sorttedChildrenResult.postValue(t)
            }

            override fun onError(e: Throwable) {
                sorttedChildrenError.postValue(e.localizedMessage)
            }
        }

        babyMedRepository.getChildrenSortByBirthdateAsc()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sorttedChildrenObserver)
    }

    fun getChildrenSorttedByBirthdateDesc(){
        sorttedChildrenObserver = object : DisposableObserver<List<Child>>(){
            override fun onComplete() {
            }

            override fun onNext(t: List<Child>) {
                sorttedChildrenResult.postValue(t)
            }

            override fun onError(e: Throwable) {
                sorttedChildrenError.postValue(e.localizedMessage)
            }
        }

        babyMedRepository.getChildrenSortByBirthdateDesc()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sorttedChildrenObserver)
    }

    fun onSorttedChildrenResult() : LiveData<List<Child>>{
        return sorttedChildrenResult
    }

    fun onSorttedChildrenError() : LiveData<String>{
        return sorttedChildrenError
    }

    fun disposeObserver(){
        if (!disposableObserver.isDisposed)
            disposableObserver.dispose()
    }

    fun disposeSorttedChildrenObserver(){
        if (!sorttedChildrenObserver.isDisposed)
            sorttedChildrenObserver.dispose()
    }
}