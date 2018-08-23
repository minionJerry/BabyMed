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
    lateinit var getIllnessObserver: DisposableObserver<Illness>
    lateinit var removingIllnessObserver: DisposableObserver<Unit>

    private var addIllnessError : MutableLiveData<String> = MutableLiveData()
    private var addIllnessComplete : MutableLiveData<Boolean> = MutableLiveData()
    private var illnessUpdatingComplete : MutableLiveData<Boolean> = MutableLiveData()
    private var illnessListResult : MutableLiveData<List<Illness>> = MutableLiveData()
    private var illnessListError : MutableLiveData<String> = MutableLiveData()
    private val illnessRemovingResult : MutableLiveData<Boolean> = MutableLiveData()
    private val illnessRemovingError : MutableLiveData<String> = MutableLiveData()
    private val getIllnessResult : MutableLiveData<Illness> = MutableLiveData()
    private val getIllnessError : MutableLiveData<String> = MutableLiveData()

    fun initSaveChildObserver(){
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

    fun initRemovingIllnessObserver(){
        removingIllnessObserver = object : DisposableObserver<Unit>() {
            override fun onNext(t: Unit) {
            }

            override fun onComplete() {
                illnessRemovingResult.postValue(true)
            }

            override fun onError(e: Throwable) {
                illnessRemovingError.postValue(e.message)
            }
        }
    }

    fun initGettingIllnessObserver(){
        getIllnessObserver = object : DisposableObserver<Illness>(){
            override fun onComplete() {
            }

            override fun onNext(t: Illness) {
                getIllnessResult.postValue(t)
            }

            override fun onError(e: Throwable) {
                getIllnessError.postValue(e.message)
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

    fun removeIllness(illness: Illness){
        babyMedRepository.deleteIllness(illness)
                .subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(removingIllnessObserver)
    }

    fun removeIllnessFromFirebase(childId: String, illnessId : String){
        babyMedRepository.removeIllnessFromFirebase(childId,illnessId)
    }

    fun getIllness(id : Long){
        babyMedRepository.getIllnessbyId(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getIllnessObserver)
    }

    fun onSaveChildComplete() : LiveData<Boolean> {
        return addIllnessComplete
    }

    fun onSuccessUpdating(): LiveData<Boolean> {
        return illnessUpdatingComplete
    }

    fun onSaveChildError() : LiveData<String> {
        return addIllnessError
    }

    fun onGetIllnessesComplete() : LiveData<List<Illness>>{
        return illnessListResult
    }

    fun onGetIllnessesError() : LiveData<String>{
        return illnessListError
    }

    fun onRemoveIllnessResult() : LiveData<Boolean>{
        return illnessRemovingResult
    }

    fun onRemoveIllnessError() : LiveData<String>{
        return illnessRemovingError
    }

    fun onGetIllnessByIdComplete() : LiveData<Illness>{
        return getIllnessResult
    }

    fun onGetIllnessError() : LiveData<String>{
        return getIllnessError
    }

    fun clearIllnessesTable(){
        babyMedRepository.clearIllnessTable()
    }


    fun disposeSaveChildObserver(){
        if (!disposableObserver.isDisposed)
            disposableObserver.dispose()

        if (!disposableUpdatingObserver.isDisposed)
            disposableUpdatingObserver.dispose()
    }

    fun disposeIllnessListObserver(){
        if (!illnessListObserver.isDisposed)
            illnessListObserver.dispose()
    }

    fun disposeGetIllnessObserver(){
        if (!getIllnessObserver.isDisposed)
            getIllnessObserver.dispose()
    }

    fun disposeRemovingIllnessObserver(){
        if (!removingIllnessObserver.isDisposed)
            removingIllnessObserver.dispose()
    }


}