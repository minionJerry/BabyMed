package com.kanykeinu.babymed.view.illnessdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kanykeinu.babymed.data.source.BabyMedRepository
import com.kanykeinu.babymed.data.source.local.entity.Illness
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class IllnessDetailViewModel @Inject constructor(private val babyMedRepository: BabyMedRepository) : ViewModel() {

    lateinit var disposableObserverForDeleting: DisposableObserver<Unit>
    lateinit var disposableObserverForGettingIllness : DisposableObserver<Illness>
    private var illnesseError: MutableLiveData<String> = MutableLiveData()
    private var illnessDeletingComplete : MutableLiveData<Boolean> = MutableLiveData()
    private var illnessGettingIllness : MutableLiveData<Illness> = MutableLiveData()


    fun initDisposableObserver(){

        disposableObserverForDeleting = object : DisposableObserver<Unit>(){
            override fun onComplete() {
                illnessDeletingComplete.postValue(true)
            }

            override fun onNext(t: Unit) {
            }

            override fun onError(e: Throwable) {
                illnesseError.postValue(e.message)
            }

        }

        disposableObserverForGettingIllness = object : DisposableObserver<Illness>(){
            override fun onComplete() {
            }

            override fun onNext(t: Illness) {
                illnessGettingIllness.postValue(t)
            }

            override fun onError(e: Throwable) {
                illnesseError.postValue(e.message)
            }

        }
    }


    fun deleteIllness(illness: Illness){
        babyMedRepository.deleteIllness(illness)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(disposableObserverForDeleting)
    }

    fun getIllnessById(id : Long){
        babyMedRepository.getIllnessbyId(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserverForGettingIllness)
    }

    fun disposeObservers(){
        if (!disposableObserverForDeleting.isDisposed)
            disposableObserverForDeleting.dispose()
    }



    fun onSuccessDeleting() : LiveData<Boolean>{
        return illnessDeletingComplete
    }

    fun onError(): LiveData<String> {
        return illnesseError
    }

    fun onCompleteGettingIllness() : LiveData<Illness>{
        return illnessGettingIllness
    }
}