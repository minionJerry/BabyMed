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
    lateinit var disposableObserver : DisposableObserver<Unit>
    lateinit var disposableUpdatingObserver : DisposableObserver<Unit>
    lateinit var illnessListObserver: DisposableObserver<List<Illness>>

    private var addIllnessError: MutableLiveData<String> = MutableLiveData()
    private var addIllnessComplete : MutableLiveData<Boolean> = MutableLiveData()
    private var illnessUpdatingComplete : MutableLiveData<Boolean> = MutableLiveData()
    private var illnessListResult : MutableLiveData<List<Illness>> = MutableLiveData()
    private var illnessListError : MutableLiveData<String> = MutableLiveData()

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

        disposableUpdatingObserver = object  : DisposableObserver<Unit>() {
            override fun onComplete() {
                illnessUpdatingComplete.postValue(true)
            }

            override fun onNext(t: Unit) {
            }

            override fun onError(e: Throwable) {
                addIllnessError.postValue(e.message)
            }
        }
    }

    fun initGettingIllnessListObserver(){
        //getting list of child`s illnesses
        illnessListObserver = object : DisposableObserver<List<Illness>>(){
            override fun onNext(t: List<Illness>) {
                illnessListResult.postValue(t)
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
                illnessListError.postValue(e.message)
            }
        }
    }

    fun saveIllness(illness : Illness)  {
        babyMedRepository.insertIllnessToBd(illness)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(disposableObserver)
    }

    fun saveIllnessToFirebase(childId : String, illness: com.kanykeinu.babymed.data.source.remote.firebase.Illness) : String? {
       return babyMedRepository.saveIllnessToFirebase(childId,illness)
    }

    fun updateIllness(illness: Illness){
        babyMedRepository.updateIllness(illness)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(disposableUpdatingObserver)
    }

    fun updateIllnessFromFirebase(childId: String, illnessId : String, illness: com.kanykeinu.babymed.data.source.remote.firebase.Illness){
        babyMedRepository.updateIllnessFromFirebase(childId,illnessId, illness)
    }

    fun getChildIllnesses(childId : Long){
        babyMedRepository.getIllnessesFromDb(childId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(illnessListObserver)
    }

    fun onComplete() : LiveData<Boolean> {
        return addIllnessComplete
    }

    fun onSuccessUpdating(): LiveData<Boolean> {
        return illnessUpdatingComplete
    }

    fun onError() : LiveData<String> {
        return addIllnessError
    }

    fun onGetIllnessesComplete() : LiveData<List<Illness>>{
        return illnessListResult
    }

    fun onGetIllnessesError() : LiveData<String>{
        return illnessListError
    }

    fun disposeObserver(){
        if (!disposableObserver.isDisposed)
            disposableObserver.dispose()

        if (!disposableUpdatingObserver.isDisposed)
            disposableUpdatingObserver.dispose()
    }

    fun disposeIllnessListObserver(){
        if (illnessListObserver.isDisposed)
            illnessListObserver.dispose()
    }
}