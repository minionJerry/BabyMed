package com.kanykeinu.babymed

import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kanykeinu.babymed.adapter.ChildAdapter
import com.kanykeinu.babymed.database.AppDatabase
import com.kanykeinu.babymed.model.Child
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private var database: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = AppDatabase.getInstance(this)
        retrieveChildrenFromDb()
    }

    fun initChildrenList(children: List<Child>){
        val childAdapter = ChildAdapter(this,children)
        val childrenLinearManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerChildren.layoutManager = childrenLinearManager
        recyclerChildren.adapter = childAdapter
    }

    fun openAddChildScreen(){
        btnAddChild.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext,NewChildActivity::class.java))
            }
        })
    }

    fun retrieveChildrenFromDb(){
        database?.childDao()?.getAll()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(Consumer { children ->
                    initChildrenList(children)
                    openAddChildScreen()
                })
    }
}
