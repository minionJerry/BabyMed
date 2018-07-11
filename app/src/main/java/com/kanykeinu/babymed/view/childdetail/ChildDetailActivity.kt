package com.kanykeinu.babymed.view.childdetail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import com.kanykeinu.babymed.utils.showToast
import com.kanykeinu.babymed.view.addeditchild.NewChildActivity
import com.kanykeinu.babymed.view.childrenlist.ChildrenViewModel
import com.kanykeinu.babymed.view.childrenlist.ChildrenViewModelFactory
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_medical_file.*
import kotlinx.android.synthetic.main.activity_new_child.*
import javax.inject.Inject

class ChildDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var childDetailViewModelFactory: ChildDetailViewModelFactory
    lateinit var childDetailViewModel: ChildDetailViewModel
    var child : Child? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectChildrenDetailViewModel()
        setContentView(R.layout.activity_medical_file)
        initActionBar()
        child = intent.getParcelableExtra<Child>(Constants.CHILD)
        initFields()
        retrieveIllnessesFromDb()
        fabAddIllness.setOnClickListener { openAddIllnessScreen() }
    }

    private fun injectChildrenDetailViewModel(){
        AndroidInjection.inject(this)
        childDetailViewModel = ViewModelProviders.of(this,childDetailViewModelFactory).get(ChildDetailViewModel::class.java)
        childDetailViewModel.initDisposableObserver()

        childDetailViewModel.onSuccess()
                .observe(this, Observer<List<Illness>> { children->
                    if (children != null)
                        initChildIllnesses(children)
                })

        childDetailViewModel.onError()
                .observe(this, Observer { error->
                    if (error!=null)
                        showToast(error)
                })

        childDetailViewModel.onLoading()
                .observe(this, Observer { isLoading ->
                    if (isLoading == false)
                        progressBarIllnesses.visibility = View.GONE
                })
    }

    private fun initActionBar(){
        setSupportActionBar(childToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    fun initFields(){
        tvName.text = child?.name
        tvAge.text = Child.getCurrentAge(child!!.birthDate).toString()
        tvBloodType.text = if (child?.bloodType != null) child!!.bloodType.toString() else ""
        tvGender.text = child?.gender
        tvWeight.text = if (child?.weight != null) child!!.weight.toString() else ""
        Glide.with(this)
                .load(child?.photoUri)
                .into(childAvatar);

    }

    fun retrieveIllnessesFromDb(){
        childDetailViewModel.getChildIllnesses(child?.id?.toInt()!!)
    }

    fun openAddIllnessScreen(){
        startActivity(Intent(this, NewIllnessActivity::class.java).putExtra(CHILD,child))
    }

    fun initChildIllnesses(illnesses : List<Illness>) {
        val illnessAdapter = IllnessAdapter(this, illnesses, object : OnAgeSet {
            override fun getChildAge(id: Long): Int {
                return Child.getCurrentAge(Integer.parseInt(child?.birthDate).toString())
            }

        })
        val illnessLinearManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerIllnesses.layoutManager = illnessLinearManager
        recyclerIllnesses.adapter = illnessAdapter
    }

    override fun onDestroy() {
        childDetailViewModel.disposeObserver()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_children_list,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.delete -> { showToast("Child will be deleted")}
            R.id.edit -> {showToast("Child will be editted")}
            android.R.id.home -> onBackPressed ()
        }

        return super.onOptionsItemSelected(item)
    }
}