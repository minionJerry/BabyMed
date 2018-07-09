package com.kanykeinu.babymed.view.childdetail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.utils.Constants.CHILD
import com.kanykeinu.babymed.view.childdetail.IllnessAdapter.OnAgeSet
import com.kanykeinu.babymed.view.addeditillness.NewIllnessActivity
import com.kanykeinu.babymed.data.source.local.BabyMedDatabase
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import com.kanykeinu.babymed.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_medical_file.*

class ChildDetailActivity : AppCompatActivity() {

    var child : Child? = null

    private var database: BabyMedDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_file)
        child = intent.getParcelableExtra<Child>(Constants.CHILD)
        initFields()
        retrieveIllnessesFromDb()
        btnAddIllness.setOnClickListener({ openAddIllnessScreen()})
    }

    fun initFields(){
        tvName.text = child?.name
        tvAge.text = Child.getCurrentAge(child!!.birthDate).toString()
        tvBloodType.text = if (child?.bloodType != null) child!!.bloodType.toString() else ""
        tvGender.text = child?.gender
        tvWeight.text = if (child?.weight != null) child!!.weight.toString() else ""
        Glide.with(this)
                .load(child?.photoUri)
                .into(imgChildPhoto);

    }

    fun initChildIllnesses(illnesses : List<Illness>) {
        val illnessAdapter = IllnessAdapter(this, illnesses, object : OnAgeSet {
            override fun getChildAge(id: Long): Int {
                var birthDate = database?.childDao()?.getBirthDateByChildId(id)
                return Child.getCurrentAge(birthDate!!)
            }

        })
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
                    initChildIllnesses(it)
                })
    }

    fun openAddIllnessScreen(){
        startActivity(Intent(this, NewIllnessActivity::class.java).putExtra(CHILD,child))
    }
}