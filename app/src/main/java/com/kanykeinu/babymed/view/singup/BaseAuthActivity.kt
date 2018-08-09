package com.kanykeinu.babymed.view.singup

import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.utils.showErrorToast
import kotlinx.android.synthetic.main.activity_register_user.*

abstract class BaseAuthActivity : AppCompatActivity(), View.OnFocusChangeListener {

    override fun onFocusChange(p0: View?, p1: Boolean) {
        when (p0?.id) {
            R.id.eTextLogin -> {
                isLoginFieldEmpty()
            }
            R.id.eTextPassword -> {
                isPasswordNotEmptyAndValidated()
            }
        }
    }

    protected fun isLoginFieldEmpty() : Boolean{
        if (TextUtils.isEmpty(eTextLogin.text.toString())) {
            eTextLogin.error = getString(R.string.enter_login)
            return true
        }
        return false
    }

    protected fun isPasswordNotEmptyAndValidated() : Boolean {
        if (TextUtils.isEmpty(eTextPassword.text.toString())) {
            eTextPassword.error = getString(R.string.enter_password)
            return false
        } else if (TextUtils.getTrimmedLength(eTextPassword.text.toString()) < 6) {
            eTextPassword.error = getString(R.string.password_length_validating)
            return false
        }
        return true
    }
}