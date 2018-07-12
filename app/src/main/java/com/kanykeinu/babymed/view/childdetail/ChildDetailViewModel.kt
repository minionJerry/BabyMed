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
    lateinit var disposableObserver : DisposableObserver<List<Illness>>
    lateinit var disposableObserverChild : DisposableObserver<Child>
    lateinit var disposableObserverForDeleting : DisposableObserver<Any>
    private var childrenIlnessesResult: MutableLiveData<List<Illness>> = MutableLiveData()
    private var childrenIllnessesError: MutableLiveData<String> = MutableLiveData()
    private var childrenDeletingComplete : MutableLiveData<Boolean> = MutableLiveData()
    private var childrenIllnessesLoader: MutableLiveData<Boolean> = MutableLiveData()
    private var childResult : MutableLiveData<Child> = MutableLiveData()

    fun onSuccess() : LiveData<List<Illness>>{
        return childrenIlnessesResult
    }

    fun onError() : LiveData<String>{
        return childrenIllnessesError
    }

    fun onLoading() : LiveData<Boolean>{
        return childrenIllnessesLoader
    }

    fun onCompleteDeleting() : LiveData<Boolean>{
        return childrenDeletingComplete
    }

    fun onGetChildData() : LiveData<Child>{
        return childResult
    }

    fun initDisposableObserver(){
        //getting list of child`s illnesses
        disposableObserver = object : DisposableObserver<List<Illness>>(){
            override fun onNext(t: List<Illness>) {
                childrenIlnessesResult.postValue(t)
                childrenIllnessesLoader.postValue(false)
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
                childrenIllnessesError.postValue(e.message)
            }
        }

        // delete child
        disposableObserverForDeleting = object : DisposableObserver<Any>(){
            override fun onComplete() {
                childrenDeletingComplete.postValue(true)
                childrenIllnessesLoader.postValue(false)
            }

            override fun onNext(t: Any) {

            }

            override fun onError(e: Throwable) {
                childrenIllnessesError.postValue(e.message)
            }
        }

        //get child new data
        disposableObserverChild = object : DisposableObserver<Child>(){
            override fun onComplete() {
            }

            override fun onNext(t: Child) {
                childResult.postValue(t)
//                childrenIllnessesLoader.postValue(false)
            }

            override fun onError(e: Throwable) {
//                childrenIllnessesError.postValue(e.message)
//                childrenIllnessesLoader.postValue(false)
            }
        }
    }

    fun getChildIllnesses(childId : Long){
        babyMedRepository.getIllnessesFromDb(childId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(disposableObserver)
    }

    fun deleteChildData(child: Child){
        babyMedRepository.deleteChildFromDb(child)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(disposableObserverForDeleting)
    }

    fun getChildData(id : Long){
        babyMedRepository.getChildById(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserverChild)
    }

    fun disposeObserver(){
        if (!disposableObserver.isDisposed)
            disposableObserver.dispose()
        if (!disposableObserverForDeleting.isDisposed)
            disposableObserverForDeleting.dispose()

    }

    fun disposeChildObserver(){
        if (!disposableObserverChild.isDisposed)
            disposableObserverChild.dispose()
    }
}