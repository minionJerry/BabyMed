package com.kanykeinu.babymed.view.singup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.R.id.eTextLogin
import com.kanykeinu.babymed.R.id.eTextPassword
import com.kanykeinu.babymed.data.source.local.sharedpref.SharedPreferencesManager
import com.kanykeinu.babymed.data.source.remote.firebase.FirebaseHandler
import com.kanykeinu.babymed.data.source.remote.firebase.User
import com.kanykeinu.babymed.utils.showErrorToast
import com.kanykeinu.babymed.view.childrenlist.ChildrenActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity : BaseAuthActivity() {
    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    lateinit var userViewModel: UserViewModel
    @Inject
    lateinit var prefs : SharedPreferencesManager
    lateinit var authListener : FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        // inject viewmodel
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)
        //user initially should be null
        userViewModel.signOutUser()
        prefs.saveUserId(null)

        setContentView(R.layout.activity_sign_in)
        btnRegister.setOnClickListener { goToRegistrationScreen() }
        btnAuth.setOnClickListener{ singInUser() }

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user!= null){
                // user is signed in, go to the next page
                prefs.saveUserId(user.uid)
                goToChildrenList()
            }
        }
    }

    private fun registerObservers() {
        userViewModel.initSignInObserver()

        userViewModel.onSignUpSuccess().observe(this, Observer { isSuccessfull ->
            if (isSuccessfull) {
                prefs.saveUserId(FirebaseAuth.getInstance().currentUser!!.uid)
                goToChildrenList()
            }
        })

        userViewModel.onSignUpError().removeObservers(this)
        userViewModel.onSignUpError().observe(this, Observer { error ->

            if (error!=null) {
                showErrorToast(error)
                userViewModel.onSignUpError().postValue(null)
            }
                            })
    }

    private fun goToChildrenList(){
        startActivity(Intent(this, ChildrenActivity::class.java))
    }

    private fun goToRegistrationScreen(){
        startActivity(Intent(this,RegisterUserActivity::class.java))
    }

    private fun singInUser() {
        registerObservers()
        if (!isLoginFieldEmpty() && isPasswordNotEmptyAndValidated()) {
            userViewModel.signIn(eTextLogin.text.toString(),eTextPassword.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        FirebaseHandler().mAuth.addAuthStateListener(authListener)
    }

    override fun onPause() {
        if (authListener!=null)
            FirebaseHandler().mAuth.removeAuthStateListener(authListener)
        super.onPause()
    }

    override fun onDestroy() {
        userViewModel.disposeSignInObserver()
        super.onDestroy()
    }
}
