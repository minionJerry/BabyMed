package com.kanykeinu.babymed.view.singup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.view.childrenlist.ChildrenActivity
import kotlinx.android.synthetic.main.activity_register_user.*

class RegisterUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        registerUser()
    }

    private fun registerUser(){
        btnRegister.setOnClickListener { startActivity(Intent(this, ChildrenActivity::class.java)) }
    }
}
