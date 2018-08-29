package com.kanykeinu.babymed.view.addeditchild

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.UploadTask
import com.kanykeinu.babymed.data.source.BabyMedRepository
import com.kanykeinu.babymed.data.source.local.entity.Child
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddEditChildViewModel @Inject constructor(private val babyMedRepository: BabyMedRepository) : ViewModel() {

    lateinit var  childSavingObserver : DisposableObserver<Unit>
    lateinit var childUpdatingObserver: DisposableObserver<Unit>
    lateinit var childRemovingObserver: DisposableObserver<Any>
    lateinit var childDisplayingObserver: DisposableObserver<Child>
    lateinit var childrenDeletingObserver : DisposableObserver<Unit>
    lateinit var saveAvatarObserver: DisposableObserver<UploadTask>
    private var addChildError: MutableLiveData<String> = MutableLiveData()
    private var addChildComplete : MutableLiveData<Boolean> = MutableLiveData()
    private var childDisplayingResult : MutableLiveData<Child> = MutableLiveData()
    private var childDisplayingError : MutableLiveData<String> = MutableLiveData()
    private var childRemovingResult : MutableLiveData<Boolean> = MutableLiveData()
    private var childRemovingError : MutableLiveData<String> = MutableLiveData()
    private var childrenDeleteResult : MutableLiveData<Boolean> = MutableLiveData()
    private var childrenDeleteError : MutableLiveData<String> = MutableLiveData()
    private val onAvatarSavingSuccess: MutableLiveData<Uri> = MutableLiveData()
    private val onAvatarSavingError : MutableLiveData<String> = MutableLiveData()

    fun initChildSavingObserver(){
        childSavingObserver = object : DisposableObserver<Unit>(){
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

    fun initChildUpdatingObserver(){
        childUpdatingObserver = object : DisposableObserver<Unit>(){
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

    fun initChildRemovingObserver(){
        childRemovingObserver = object : DisposableObserver<Any>(){
            override fun onComplete() {
                childRemovingResult.postValue(true)
            }

            override fun onNext(t: Any) {

            }

            override fun onError(e: Throwable) {
                childRemovingError.postValue(e.localizedMessage)
            }
        }
    }

    fun initChildDisplayingObserver(){
        childDisplayingObserver = object : DisposableObserver<Child>(){
            override fun onComplete() {
            }

            override fun onNext(t: Child) {
                childDisplayingResult.postValue(t)
            }

            override fun onError(e: Throwable) {
                childDisplayingError.postValue(e.message)
            }
        }
    }

    fun initChildrenDeletingObserver(){
        childrenDeletingObserver = object : DisposableObserver<Unit>() {
            override fun onComplete() {
                childrenDeleteResult.postValue(true)
            }

            override fun onNext(t: Unit) {
            }

            override fun onError(e: Throwable) {
                childrenDeleteError.postValue(e.localizedMessage)
            }
        }
    }

    fun initSaveChildAvatarObserver(){
        saveAvatarObserver = object : DisposableObserver<UploadTask>(){
            override fun onComplete() {
            }

            override fun onNext(t: UploadTask) {
                t.addOnSuccessListener { taskSnapshot ->
                   taskSnapshot.storage.downloadUrl.addOnCompleteListener { task: Task<Uri> -> onAvatarSavingSuccess.postValue(task.result) }}
                t.addOnFailureListener { exception -> onAvatarSavingError.postValue(exception.localizedMessage) }
            }

            override fun onError(e: Throwable) {
                onAvatarSavingError.postValue(e.localizedMessage)
            }
        }
    }

    fun saveChild(child : Child)  {
        childSavingObserver = object : DisposableObserver<Unit>(){
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
                ?.subscribe(childSavingObserver)
    }

    fun updateChild(newChild: Child) {
        babyMedRepository.updateChild(newChild)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(childUpdatingObserver)

    }

    fun deleteChildData(child: Child){
        babyMedRepository.deleteChildFromDb(child)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(childRemovingObserver)
    }

    fun getChildData(id : Long){
        babyMedRepository.getChildById(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(childDisplayingObserver)
    }

    fun saveChildToFirebase(child: com.kanykeinu.babymed.data.source.remote.firebase.Child) : String? {
       return babyMedRepository.saveChildToFirebase(child)
    }

    fun updateChildFromFirebase(childId : String, child: com.kanykeinu.babymed.data.source.remote.firebase.Child){
        babyMedRepository.updateChildToFirebase(childId,child)
    }

    fun removeChildFromFirebase(childId: String){
        babyMedRepository.removeChildFromFirebase(childId = childId)
    }

    fun saveChildAvatarToFirebase(uri : Uri) {
       babyMedRepository.saveImageToFirebase(uri)
               .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(saveAvatarObserver)
    }

    fun getAvatarPath() : MutableLiveData<Uri>{
        return onAvatarSavingSuccess
    }

    fun getAvatarSavingError() : MutableLiveData<String>{
        return onAvatarSavingError
    }

    fun clearChildrenTable(){
        babyMedRepository.clearChildTable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(childrenDeletingObserver)
    }

    fun disposeChildSavingObserver(){
        if (!childSavingObserver.isDisposed)
            childSavingObserver.dispose()

    }

    fun disposeChildUpdatingObserver(){
        if (!childUpdatingObserver.isDisposed)
            childUpdatingObserver.dispose()
    }

    fun disposeChildRemovingObserver(){
        if (!childRemovingObserver.isDisposed)
            childRemovingObserver.dispose()
    }

    fun disposeChildDisplayingObserver(){
        if (!childDisplayingObserver.isDisposed)
            childDisplayingObserver.dispose()
    }

    fun disposeChildrenDeletingObserver(){
        if (!childrenDeletingObserver.isDisposed)
            childrenDeletingObserver.dispose()
    }

    fun disposeSaveAvatarObserver(){
        if (!saveAvatarObserver.isDisposed)
            saveAvatarObserver.dispose()
    }

    fun onCompleteSaveUpdate() : LiveData<Boolean> {
        return addChildComplete
    }

    fun onSaveUpdateError() : LiveData<String> {
        return addChildError
    }

    fun onCompleteDisplayingChild() : LiveData<Child>{
        return childDisplayingResult
    }

    fun onDisplayingChildError() : LiveData<String>{
        return childDisplayingError
    }

    fun onCompleteRemovingChild() : LiveData<Boolean>{
        return childRemovingResult
    }

    fun onRemovingChildError() : LiveData<String>{
        return childRemovingError
    }

    fun onChildrenDeletingComplete() : LiveData<Boolean>{
        return childrenDeleteResult
    }

    fun onChildrenDeletingError() : LiveData<String>{
        return childrenDeleteError
    }

}
