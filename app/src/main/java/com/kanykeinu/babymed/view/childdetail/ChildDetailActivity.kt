package com.kanykeinu.babymed.view.childdetail

import android.content.Intent
import android.net.Uri
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
import com.kanykeinu.babymed.R.string.child
import com.kanykeinu.babymed.utils.Constants.CHILD
import com.kanykeinu.babymed.view.addeditillness.NewIllnessActivity
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import com.kanykeinu.babymed.utils.*
import com.kanykeinu.babymed.utils.Constants.ILLNESS
import com.kanykeinu.babymed.view.addeditchild.AddEditChildViewModel
import com.kanykeinu.babymed.view.addeditchild.AddEditChildViewModelFactory
import com.kanykeinu.babymed.view.addeditchild.NewChildActivity
import com.kanykeinu.babymed.view.addeditillness.AddIllnessViewModel
import com.kanykeinu.babymed.view.addeditillness.AddIllnessViewModelFactory
import com.kanykeinu.babymed.view.childrenlist.OnSortChildrenClick
import com.kanykeinu.babymed.view.illnessdetail.IllnessDetailActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_medical_file.*
import kotlinx.android.synthetic.main.children_list_item.view.*
import javax.inject.Inject

class ChildDetailActivity : AppCompatActivity(){
    @Inject
    lateinit var addEditChildViewModelFactory : AddEditChildViewModelFactory

    lateinit var addEditChildViewModel: AddEditChildViewModel
    @Inject
    lateinit var addIllnesViewModelFactory: AddIllnessViewModelFactory
    lateinit var addIllnessViewModel : AddIllnessViewModel
    private var child : Child? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectChildrenDetailViewModel()
        setContentView(R.layout.activity_medical_file)
        initActionBar()
        child = intent.getParcelableExtra<Child>(Constants.CHILD)
        addEditChildViewModel.getChildData(child!!.id)
        retrieveIllnessesFromDb()
        fabAddIllness.setOnClickListener { openAddIllnessScreen() }
    }

    private fun injectChildrenDetailViewModel(){
        AndroidInjection.inject(this)
        addEditChildViewModel = ViewModelProviders.of(this,addEditChildViewModelFactory).get(AddEditChildViewModel::class.java)
        addEditChildViewModel.initChildDisplayingObserver()
        addEditChildViewModel.initChildRemovingObserver()

        addEditChildViewModel.onCompleteDisplayingChild().observe(this, Observer { child ->
            initFields(child)
            this.child = child})
        addEditChildViewModel.onDisplayingChildError().observe(this, Observer { error -> showErrorToast(error) })

        addEditChildViewModel.onCompleteRemovingChild().observe(this, Observer { isSuccessfull -> if (isSuccessfull)  showSuccessToast(getString(R.string.child_is_deleted)) })
        addEditChildViewModel.onRemovingChildError().observe(this, Observer { error -> showErrorToast(error) })

        addIllnessViewModel = ViewModelProviders.of(this,addIllnesViewModelFactory).get(AddIllnessViewModel::class.java)
        addIllnessViewModel.initGettingIllnessListObserver()

        addIllnessViewModel.onGetIllnessesComplete().observe(this, Observer { illnesses -> if (illnesses.isNotEmpty()) { initChildIllnesses(illnesses) } })
        addIllnessViewModel.onGetIllnessesError().observe(this, Observer { error -> showErrorToast(error) })
    }

    private fun initChildIllnesses(illnesses : List<Illness>) {
        val illnessAdapter = IllnessAdapter(this, illnesses, object : IllnessAdapter.OnAgeAndWeightSet {
            override fun getChildAge(id: Long): Child {
                return child!!
            }

        }, object  : IllnessAdapter.OnIllnessClick{
            override fun onIllnessClick(illness: Illness) {
                startActivity(Intent(baseContext,IllnessDetailActivity::class.java).putExtra(ILLNESS,illness).putExtra(CHILD,child))
            }
        })
        val illnessLinearManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerIllnesses.layoutManager = illnessLinearManager
        recyclerIllnesses.adapter = illnessAdapter
    }

    private fun initActionBar(){
        setSupportActionBar(childToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun initFields(child: Child){
        tvName.text = child.name
        tvAge.text = Child.getCurrentAge(child.birthDate).toString()
        tvBloodType.text = if (child.bloodType != null) child.bloodType.toString() else ""
        tvGender.text = child.gender
        tvWeight.text = if (child.weight != null) child.weight.toString() else ""
        Glide.with(this)
                .load(child.photoUri)
                .into(childAvatar)
    }

    private fun retrieveIllnessesFromDb(){
        addIllnessViewModel.getChildIllnesses(child!!.id)
    }

    private fun openAddIllnessScreen(){
        startActivity(Intent(this, NewIllnessActivity::class.java).putExtra(CHILD,child))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_children_list,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.delete -> {
                DialogView.deletingConfirm(this, getString(R.string.deleting_child_data_confirm),object : OnDialogItemSelected{
                    override fun onDialogClicked() {
                        deleteChild()
                        finish()
                    }
                })
               }
            R.id.edit -> {
                openEditChildDataScreen()
            }
            android.R.id.home -> onBackPressed ()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteChild(){
        addEditChildViewModel.deleteChildData(child!!)
        addEditChildViewModel.removeChildFromFirebase(child!!.firebaseId!!)
    }

    private fun openEditChildDataScreen(){
        startActivity(Intent(this,NewChildActivity::class.java).putExtra(CHILD,child))
    }


    override fun onDestroy() {
        addEditChildViewModel.disposeChildDisplayingObserver()
        addEditChildViewModel.disposeChildRemovingObserver()
        addIllnessViewModel.disposeIllnessListObserver()
        super.onDestroy()
    }
}
