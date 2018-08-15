package com.kanykeinu.babymed.view.singup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.kanykeinu.babymed.data.source.BabyMedRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserViewModel @Inject constructor(private val babyMedRepository: BabyMedRepository) : ViewModel() {

    lateinit var disposableObserverForSignUp: DisposableObserver<Task<AuthResult>>
    lateinit var disposableObserverForSignIn: DisposableObserver<Task<AuthResult>>
    private var signUpError: MutableLiveData<String> = MutableLiveData()
    private var signInError: MutableLiveData<String> = MutableLiveData()
    private var signUpComplete : MutableLiveData<Boolean> = MutableLiveData()
    private var signInComplete : MutableLiveData<Boolean> = MutableLiveData()

    fun initSignUpObserver(){
        disposableObserverForSignUp = object : DisposableObserver<Task<AuthResult>>(){
            override fun onNext(t: Task<AuthResult>) {
                signUpComplete.postValue(t.isSuccessful)
                t.addOnFailureListener {  signUpError.postValue(t.exception?.localizedMessage) }
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
                Log.e("UserViewModel", "sign in error --> " + e.localizedMessage)
                signUpError.postValue(e.localizedMessage)
            }
        }
    }

    fun initSignInObserver(){
        disposableObserverForSignIn = object : DisposableObserver<Task<AuthResult>>(){
            override fun onComplete() {
            }

            override fun onNext(t: Task<AuthResult>) {
                signInComplete.postValue(t.isSuccessful)
                t.addOnFailureListener { signInError.postValue(t.exception?.localizedMessage) }
            }

            override fun onError(e: Throwable) {
                signInError.postValue(e.localizedMessage)
            }
        }
    }

    fun signUp(email : String, password : String){
      babyMedRepository.createUserAccount(email,password)
              ?.subscribeOn(Schedulers.newThread())
              ?.observeOn(AndroidSchedulers.mainThread())
              ?.subscribe(disposableObserverForSignUp)
    }

    fun signIn(email: String,password: String)  {
        babyMedRepository.signInUser(email,password)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(disposableObserverForSignIn)
    }


    fun onSignUpSuccess() : MutableLiveData<Boolean>{
        return signUpComplete
    }

    fun onSignInSuccess() : MutableLiveData<Boolean>{
        return signInComplete
    }

    fun onSignUpError() : MutableLiveData<String>{
        return signUpError
    }

    fun onSignInError() : MutableLiveData<String>{
        return signInError
    }

    fun getCurrentUser() : FirebaseUser? {
        return babyMedRepository.getCurrentUser()
    }

    fun signOutUser(){
        return babyMedRepository.signOut()
    }

    fun disposeSignUpObserver(){
        if (!disposableObserverForSignUp.isDisposed)
            disposableObserverForSignUp.dispose()
    }

    fun disposeSignInObserver(){
        if (!disposableObserverForSignIn.isDisposed)
            disposableObserverForSignIn.dispose()
    }
}