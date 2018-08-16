package com.kanykeinu.babymed.data.source.remote.firebase

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.*

class FirebaseHandler {
    private var database = FirebaseDatabase.getInstance()
    private var databaseRef = database.getReference().child("children")
    private var mAuth = FirebaseAuth.getInstance()

    fun saveChildToFirebase(child : Child) : String?{
        val childId: String? = databaseRef.push().key
        if (childId != null) {
            databaseRef.child(childId).setValue(child)
        }
        return childId
    }

    fun updateChildFromFirebase(childId: String,child: Child){
        databaseRef.child(childId).setValue(child)
    }

    fun removeChildFromFirebase(childId: String){
        databaseRef.child(childId).removeValue()
    }

    fun saveIllnessToFirebase(childId : String, illness: Illness) : String? {
        val illnessId = databaseRef.child(childId).child("illnessList").push().key
        if (illnessId != null) {
            databaseRef.child(childId).child("illnessList").child(illnessId).setValue(illness)
        }
        return illnessId
    }

    fun updateIllnessFromFirebase(childId : String, illnessId : String, illness: Illness){
        databaseRef.child(childId).child("illnessList").child(illnessId).setValue(illness)
    }

    fun removeIllnessFromFirebase(childId : String, illnessId : String){
        databaseRef.child(childId).child("illnessList").child(illnessId).removeValue()
    }

    fun createAccount(email : String, password : String) : Observable<Task<AuthResult>>? {
        Log.e("Account"," Email & Password --> " + email + " : " + password)
        return io.reactivex.Observable.fromCallable { mAuth.createUserWithEmailAndPassword(email,password)}
    }

    fun getCurrentUser() : FirebaseUser?{
        return mAuth.currentUser
    }

    fun signIn(email: String,password: String)  : Observable<Task<AuthResult>>? {
        return io.reactivex.Observable.fromCallable { mAuth.signInWithEmailAndPassword(email, password)}
    }

    fun signOut() {
        mAuth.signOut()
    }
}
