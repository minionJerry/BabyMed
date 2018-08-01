package com.kanykeinu.babymed.data.source.remote.firebase

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

class FirebaseHandler {
    var database = FirebaseDatabase.getInstance()
    var databaseRef = database.getReference().child("users")
    private var userListener: ValueEventListener? = null
    private var checkedUser : User? = null

    fun saveUserToFirebase(user : User) : String?{
        val userId: String? = databaseRef.push().key
        if (userId != null) {
            databaseRef.child(userId).setValue(user)
        }
        return userId
    }

    fun checkUserFromFirebase(user: User) : User?{
        userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val firebaseUser = dataSnapshot.getValue<User>(User::class.java)
                if (checkIfUserExists(user,firebaseUser)) {
                    checkedUser = user
                    databaseRef.removeEventListener(userListener!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        databaseRef.addValueEventListener(userListener as ValueEventListener)
        return checkedUser
    }

    private fun checkIfUserExists(user: User,firebaseUser: User?) : Boolean {
        if (user.equals(firebaseUser)) {
            return true
        }
        return false
    }

    fun saveChildToFirebase(userId : String,child: Child) : String? {
        val childId = databaseRef.child(userId).child("childList").push().key
        if (childId != null) {
            databaseRef.child(userId).child("childList").child(childId).setValue(child)
        }
        return childId
    }

    fun updateChildFromFirebase(userId: String,childId: String,child: Child){
        databaseRef.child(userId).child("childList").child(childId).setValue(child)
    }

    fun deleteChildFromFirebase(userId: String, childId: String){
        databaseRef.child(userId).child("childList").child(childId).removeValue()
    }

    fun saveIllnessToFirebase(userId : String, childId : String, illness: Illness) : String? {
        val illnessId = databaseRef.child(userId).child("childList").child(childId).child("illnessList").push().key
        if (illnessId != null) {
            databaseRef.child(userId).child("childList").child(childId).child("illnessList").child(illnessId).setValue(illness)
        }
        return illnessId
    }

    fun editIllnessFromFirebase(userId : String, childId : String, illnessId : String, illness: Illness){
        databaseRef.child(userId).child("childList").child(childId).child("illnessList").child(illnessId).setValue(illness)
    }

    fun deleteIllnessFromFirebase(userId : String, childId : String, illnessId : String){
        databaseRef.child(userId).child("childList").child(childId).child("illnessList").child(illnessId).removeValue()
    }



}