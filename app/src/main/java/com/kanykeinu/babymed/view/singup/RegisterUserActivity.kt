package com.kanykeinu.babymed.view.singup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.R.id.*
import com.kanykeinu.babymed.utils.showErrorToast
import com.kanykeinu.babymed.utils.showSuccessToast
import com.kanykeinu.babymed.view.childrenlist.ChildrenActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_register_user.*
import javax.inject.Inject

class RegisterUserActivity : BaseAuthActivity(), View.OnFocusChangeListener {


    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inject userViewModel
        AndroidInjection.inject(this)
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)

        setContentView(R.layout.activity_register_user)
        eTextLogin.setOnFocusChangeListener(this)
        eTextPassword.setOnFocusChangeListener(this)
        eTextConfirmPassword.setOnFocusChangeListener(this)
        btnRegister.setOnClickListener {
            injectRegisterViewModel()
            signUpUser()
        }
    }

    private fun injectRegisterViewModel() {
        userViewModel.initSignUpObserver()

        userViewModel.onSignUpSuccess().observe(this, Observer { isSuccessfull ->
            if (isSuccessfull) {
                updateUserProfile()
                goToSignInScreen()
            }
        })

        userViewModel.onSignUpError().observe(this, Observer { error ->
            if (error!=null) {
                showErrorToast(error)
                userViewModel.onSignUpError().postValue(null)
            } })
    }

    private fun updateUserProfile(){
        userViewModel.getCurrentUser()?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(eTextUsername.text.toString()).build())
                ?.addOnCompleteListener { task ->  if (task.isSuccessful) showSuccessToast("Profile is updated")}
    }

    private fun goToSignInScreen(){
        startActivity(Intent(this,SignInActivity::class.java))
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        when (p0?.id) {
            R.id.eTextConfirmPassword -> {
                isConfirmPasswordEmpty()
            }
        }
    }

    private fun isConfirmPasswordEmpty() : Boolean {
        if (TextUtils.isEmpty(eTextConfirmPassword.text.toString())) {
            eTextConfirmPassword.error = getString(R.string.confirm_password)
            return true
        }
        else if (!eTextConfirmPassword.text.toString().equals(eTextPassword.text.toString())) {
            eTextConfirmPassword.error = getString(R.string.password_does_not_match)
            return true
        }
        return false
    }

   private fun signUpUser() {
       if (!isLoginFieldEmpty() && isPasswordNotEmptyAndValidated() && !isConfirmPasswordEmpty()) {
           userViewModel.signUp(eTextLogin.text.toString(), eTextPassword.text.toString())
       }
   }

    override fun onDestroy() {
        userViewModel.disposeSignUpObserver()
        super.onDestroy()
    }
}
