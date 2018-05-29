package com.kanykeinu.babymed

import android.arch.persistence.room.Room
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kanykeinu.babymed.Constants.CHILD
import com.kanykeinu.babymed.adapter.IllnessAdapter
import com.kanykeinu.babymed.database.AppDatabase
import com.kanykeinu.babymed.model.Child
import com.kanykeinu.babymed.model.Illness
import com.kanykeinu.babymed.util.AgeUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_medical_file.*

class MedicalFileActivity : AppCompatActivity() {

    var child : Child? = null

    private var database: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_file)
        child = intent.getParcelableExtra<Child>(Constants.CHILD)
        database = AppDatabase.getInstance(this)
        initFields()
        retrieveIllnessesFromDb()
        btnAddIllness.setOnClickListener({v -> openAddIllnessScreen()})
    }

    fun initFields(){
        tvName.text = child?.name
        tvAge.text = AgeUtil.getCurrentAge(child!!.birthDate).toString()
        tvBloodType.text = if (child?.bloodType != null) child!!.bloodType.toString() else ""
        tvGender.text = child?.gender
        tvWeight.text = if (child?.weight != null) child!!.weight.toString() else ""
        Glide.with(this)
                .load(child?.photoUri)
                .into(imgChildPhoto);

    }

    fun iniChildIllnesses(illnesses : List<Illness>) {
        val illnessAdapter = IllnessAdapter(this, illnesses)
        val illnessLinearManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerIllnesses.layoutManager = illnessLinearManager
        recyclerIllnesses.adapter = illnessAdapter
    }


    fun retrieveIllnessesFromDb(){
        val illnessDao = database?.illnessDao()
        illnessDao?.getAll()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe ({ it ->
                    iniChildIllnesses(it)
//                    openAddChildScreen()
                })
    }

    fun openAddIllnessScreen(){
        startActivity(Intent(this,NewIllnessActivity::class.java).putExtra(CHILD,child))
    }
}