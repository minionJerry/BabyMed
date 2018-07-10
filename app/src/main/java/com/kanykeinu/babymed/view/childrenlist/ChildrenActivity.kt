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
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.utils.showToast
import com.kanykeinu.babymed.view.addeditchild.NewChildActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recyclerChildren
import javax.inject.Inject

class ChildrenActivity : AppCompatActivity() {

    @Inject
    lateinit var childrenViewModelFactory: ChildrenViewModelFactory
    lateinit var childrenViewModel: ChildrenViewModel
    private val childAdapter = ChildrenAdapter(this,ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectChildrenViewModel()
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

        childrenViewModel.onSuccess()
                .observe(this, Observer<List<Child>> { children->
                    if (children != null)
                        initChildrenList(children)
                })

        childrenViewModel.onError()
                .observe(this, Observer { error->
                    if (error!=null)
                        showToast(error)
                })

        childrenViewModel.onLoader()
                .observe(this, Observer { isLoading ->
                    if (isLoading == false)
                        progressBar.visibility = View.GONE
                })
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
        menuInflater.inflate(R.menu.menu_children_list,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.delete -> showToast("Delete is clicked")
            R.id.edit -> showToast("Edit is clicked")
        }
        return super.onOptionsItemSelected(item)
    }
}
