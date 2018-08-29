package com.kanykeinu.babymed.data.source

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.UploadTask
import com.kanykeinu.babymed.data.source.local.dao.ChildDao
import com.kanykeinu.babymed.data.source.local.dao.IllnessDao
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import com.kanykeinu.babymed.data.source.remote.firebase.FirebaseHandler
import com.kanykeinu.babymed.data.source.remote.firebase.User
import io.reactivex.Observable
import javax.inject.Inject

class BabyMedRepository @Inject constructor(val childDao : ChildDao, val illnessDao : IllnessDao,val firebaseHandler: FirebaseHandler) {

    fun getChildrenFromDb() : Observable<List<Child>> {
        return childDao.getAll()
                .toObservable()
    }

    fun insertChildToDb(child : Child): Observable<Unit>? {
        return Observable.fromCallable{ childDao.insert(child)}
    }

    fun getIllnessesFromDb(childId : Long) : Observable<List<Illness>> {
        return illnessDao.getByChildId(childId)
                .toObservable()
    }

    fun insertIllnessToBd (illness: Illness) : Observable<Unit>? {
        return Observable.fromCallable{ illnessDao.insert(illness = illness)}
    }

    fun deleteChildFromDb(child: Child): Observable<Unit>? {
        return Observable.fromCallable{ childDao.delete(child)}
    }

    fun getChildById(id : Long) : Observable<Child> {
        return childDao.getById(id).toObservable()
    }

    fun updateChild(newChild: Child) : Observable<Unit> {
        return Observable.fromCallable { childDao.update(newChild)}
    }

    fun updateIllness(illness: Illness) : Observable<Unit>{
        return Observable.fromCallable{ illnessDao.update(illness)}
    }

    fun deleteIllness(illness: Illness) : Observable<Unit>{
        return Observable.fromCallable{illnessDao.delete(illness)}
    }

    fun getIllnessbyId(id: Long) : Observable<Illness>{
        return illnessDao.getById(id).toObservable()
    }

    fun saveChildToFirebase(child: com.kanykeinu.babymed.data.source.remote.firebase.Child) : String? {
        return firebaseHandler.saveChildToFirebase(child)
    }

    fun updateChildToFirebase(childId: String, child: com.kanykeinu.babymed.data.source.remote.firebase.Child){
       return firebaseHandler.updateChildFromFirebase(childId, child)
    }

    fun removeChildFromFirebase(childId: String){
        firebaseHandler.removeChildFromFirebase(childId)
    }

    fun saveIllnessToFirebase(childId : String, illness : com.kanykeinu.babymed.data.source.remote.firebase.Illness) : String?{
        return firebaseHandler.saveIllnessToFirebase(childId, illness)
    }

    fun updateIllnessFromFirebase(childId: String, illnessId : String, illness: com.kanykeinu.babymed.data.source.remote.firebase.Illness) {
        firebaseHandler.updateIllnessFromFirebase(childId,illnessId,illness)
    }

    fun removeIllnessFromFirebase(childId: String, illnessId: String){
        firebaseHandler.removeIllnessFromFirebase(childId, illnessId)
    }

    fun createUserAccount(email: String, password : String) : Observable<Task<AuthResult>>?{
       return firebaseHandler.createAccount(email,password)
    }

    fun signInUser(email: String,password: String) : Observable<Task<AuthResult>>? {
        return firebaseHandler.signIn(email,password)
    }

    fun signOut(){
        firebaseHandler.signOut()
    }

    fun getCurrentUser() : FirebaseUser? {
        return firebaseHandler.getCurrentUser()
    }

    fun clearChildTable() : Observable<Unit> {
        return Observable.fromCallable {childDao.clearChildTable()}
    }

    fun clearIllnessTable() : Observable<Unit> {
        return Observable.fromCallable {illnessDao.clearIllnessTable()}
    }

    fun retrieveUserChildren(userId : String, valueEventListener: ValueEventListener){
        return firebaseHandler.retrieveUserChildren(userId,valueEventListener)
    }

    fun getChildrenSortByName() : Observable<List<Child>>{
        return childDao.getChildrenSortByName().toObservable()
    }

    fun getChildrenSortByBirthdateAsc() : Observable<List<Child>>{
        return childDao.getChildrenSortByBirthdateAsc().toObservable()
    }

    fun getChildrenSortByBirthdateDesc() : Observable<List<Child>>{
        return childDao.getChildrenSortByBirthdateDesc().toObservable()
    }

    fun saveImageToFirebase(photoUri : Uri) : Observable<UploadTask> {
        return firebaseHandler.saveImageToFirebaseStorage(photoUri)
    }

}