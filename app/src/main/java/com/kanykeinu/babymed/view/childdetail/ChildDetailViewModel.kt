package com.kanykeinu.babymed.view.childdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kanykeinu.babymed.data.source.BabyMedRepository
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChildDetailViewModel @Inject constructor(private val babyMedRepository: BabyMedRepository) : ViewModel() {
    lateinit var  disposableObserver : DisposableObserver<List<Illness>>
    private var childrenIlnessesResult: MutableLiveData<List<Illness>> = MutableLiveData()
    private var childrenIllnessesError: MutableLiveData<String> = MutableLiveData()
    private var childrenIllnessesLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun onSuccess() : LiveData<List<Illness>>{
        return childrenIlnessesResult
    }

    fun onError() : LiveData<String>{
        return childrenIllnessesError
    }

    fun onLoading() : LiveData<Boolean>{
        return childrenIllnessesLoader
    }

    fun initDisposableObserver(){
        disposableObserver = object : DisposableObserver<List<Illness>>(){
            override fun onComplete() {
            }

            override fun onNext(t: List<Illness>) {
                childrenIlnessesResult.postValue(t)
                childrenIllnessesLoader.postValue(false)
            }

            override fun onError(e: Throwable) {
                childrenIllnessesError.postValue(e.message)
            }
        }
    }

    fun getChildIllnesses(childId : Int){
        babyMedRepository.getIllnessesFromDb(childId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(disposableObserver)
    }

    fun disposeObserver(){
        if (!disposableObserver.isDisposed)
            disposableObserver.dispose()
    }
}