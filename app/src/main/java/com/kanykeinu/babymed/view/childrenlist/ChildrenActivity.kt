package com.kanykeinu.babymed.view.childrenlist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.R.id.bottomAppBar
import com.kanykeinu.babymed.R.id.progressBar
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.sharedpref.SharedPreferencesManager
import com.kanykeinu.babymed.data.source.remote.firebase.FirebaseHandler
import com.kanykeinu.babymed.utils.Constants
import com.kanykeinu.babymed.utils.showErrorToast
import com.kanykeinu.babymed.utils.showInfoToast
import com.kanykeinu.babymed.view.addeditchild.NewChildActivity
import com.kanykeinu.babymed.view.childdetail.ChildDetailActivity
import com.kanykeinu.babymed.view.singup.SignInActivity
import com.kanykeinu.babymed.view.singup.UserViewModel
import com.kanykeinu.babymed.view.singup.UserViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recyclerChildren
import javax.inject.Inject

class ChildrenActivity : AppCompatActivity() {

    @Inject
    lateinit var childrenViewModelFactory: ChildrenViewModelFactory
    lateinit var childrenViewModel: ChildrenViewModel
    @Inject
    lateinit var userViewModelFactory : UserViewModelFactory
    lateinit var userViewModel : UserViewModel
    @Inject
    lateinit var prefs : SharedPreferencesManager
    @Inject
    lateinit var firebaseHandler: FirebaseHandler
    private val childAdapter = ChildrenAdapter(this,ArrayList(),object : OnChildItemClick{
        override fun onChildClick(child: Child) {
            startActivity(Intent(applicationContext, ChildDetailActivity::class.java).putExtra(Constants.CHILD,child))
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectChildrenViewModel()
        checkIfUserAuthorized()
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomAppBar)
        progressBar.visibility = View.VISIBLE
        initRecyclerView()
        openAddChildScreen()
        loadData()
    }

    private fun injectChildrenViewModel(){
        AndroidInjection.inject(this)
        childrenViewModel = ViewModelProviders.of(this,childrenViewModelFactory).get(ChildrenViewModel::class.java)
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)

        childrenViewModel.onSuccess()
                .observe(this, Observer<List<Child>> { children->
                    if (children != null)
                        initChildrenList(children)
                })

        childrenViewModel.onError()
                .observe(this, Observer { error->
                    if (error!=null)
                        showErrorToast(error)
                })

        childrenViewModel.onLoader()
                .observe(this, Observer { isLoading ->
                    if (isLoading == false)
                        progressBar.visibility = View.GONE
                })
    }

    private fun checkIfUserAuthorized(){
        val user = firebaseHandler.getCurrentUser()
        if (user != null)
            prefs.saveUserId(user.uid)
        else goToSignInScreen()
    }

    private fun goToSignInScreen(){
        startActivity(Intent(this,SignInActivity::class.java))
    }

    private fun loadData() {
        childrenViewModel.getChildrenList()
    }

    private fun initRecyclerView(){
        val childrenLinearManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerChildren.layoutManager = childrenLinearManager
        recyclerChildren.adapter = childAdapter
    }

    private fun initChildrenList(children: List<Child>){
        childAdapter.addChildrenToList(children)
    }

    private fun openAddChildScreen(){
        fab.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext, NewChildActivity::class.java))
            }
        })
    }

    override fun onDestroy() {
        childrenViewModel.disposeObserver()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_settings,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.settings -> showInfoToast("Settings is clicked")
            android.R.id.home -> BottomNavigationDialogFragment.newInstance(userViewModel.getCurrentUser()?.displayName).show(supportFragmentManager, "dialog")
        }
        return super.onOptionsItemSelected(item)
    }

}
