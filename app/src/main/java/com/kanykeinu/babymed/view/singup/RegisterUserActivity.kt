package com.kanykeinu.babymed.view.singup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.data.source.BabyMedRepository
import com.kanykeinu.babymed.data.source.local.sharedpref.SharedPreferencesManager
import com.kanykeinu.babymed.data.source.remote.firebase.User
import com.kanykeinu.babymed.view.childrenlist.ChildrenActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_new_illness.*
import kotlinx.android.synthetic.main.activity_register_user.*
import javax.inject.Inject

class RegisterUserActivity : AppCompatActivity(), View.OnFocusChangeListener {
    @Inject
    lateinit var babyMedRepository : BabyMedRepository

    @Inject
    lateinit var prefs : SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_register_user)
        eTextLogin.setOnFocusChangeListener(this)
        eTextPassword.setOnFocusChangeListener(this)
        eTextConfirmPassword.setOnFocusChangeListener(this)
        registerUser()

    }

    private fun registerUser() {
        btnRegister.setOnClickListener {
            if (isLoginFieldEmpty() && isPasswordEmpty() && isConfirmPasswordEmpty()) {
                val userId = babyMedRepository.saveUserToFirebase(User(eTextLogin.text.toString(), eTextPassword.text.toString(), null))
                if (userId != null) {
                    prefs.saveUserId(userId)
                }
                startActivity(Intent(this, ChildrenActivity::class.java))
            }
        }
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        when (p0?.id) {
            R.id.eTextLogin -> {
                isLoginFieldEmpty()
            }
            R.id.eTextPassword -> {
                isPasswordEmpty()
            }
            R.id.eTextConfirmPassword -> {
                isConfirmPasswordEmpty()
            }
        }

    }

    private fun isLoginFieldEmpty() : Boolean{
        if ((eTextLogin.text.toString().equals(""))) {
            eTextLogin.error = "Введите логин"
            return false
        }
        return true
    }

    private fun isPasswordEmpty() : Boolean {
        if (eTextPassword.text.toString().equals("")) {
            eTextPassword.error = "Введите пароль"
            return false
        }
        return true
    }

    private fun isConfirmPasswordEmpty() : Boolean {
        if (eTextConfirmPassword.text.toString().equals("")) {
            eTextConfirmPassword.error = "Подтвердите пароль"
            return false
        }
        else if (!eTextConfirmPassword.text.toString().equals(eTextPassword.text.toString())) {
            eTextConfirmPassword.error = "Пароли не совпадают"
            return false
        }
        return true
    }
}
