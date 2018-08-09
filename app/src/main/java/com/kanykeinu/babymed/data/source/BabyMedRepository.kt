package com.kanykeinu.babymed.data.source

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
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

    fun saveUserToFirebase(user: User): String? {
        return firebaseHandler.saveUserToFirebase(user)
    }

    fun checkUserFromFirebase(user: User) {
        firebaseHandler.checkUserFromFirebase(user)
    }

    fun saveChildToFirebase(userId: String, child: Child){
        firebaseHandler.saveChildToFirebase(userId,child)
    }

    fun updateChildToFirebase(userId: String,childId: String,child: com.kanykeinu.babymed.data.source.remote.firebase.Child){
        firebaseHandler.updateChildFromFirebase(userId, childId, child)
    }

    fun deleteChildFromFirebase(userId: String, childId: String){
        firebaseHandler.deleteChildFromFirebase(userId,childId)
    }

    fun createUserAccount(email: String, password : String) : Observable<Task<AuthResult>>{
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

}