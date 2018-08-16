package com.kanykeinu.babymed.view.illnessdetail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import com.kanykeinu.babymed.utils.*
import com.kanykeinu.babymed.utils.Constants.CHILD
import com.kanykeinu.babymed.utils.Constants.ILLNESS
import com.kanykeinu.babymed.view.addeditillness.AddIllnessViewModel
import com.kanykeinu.babymed.view.addeditillness.AddIllnessViewModelFactory
import com.kanykeinu.babymed.view.addeditillness.NewIllnessActivity
import com.kanykeinu.babymed.view.childrenlist.ChildrenViewModel
import com.kanykeinu.babymed.view.childrenlist.ChildrenViewModelFactory
import dagger.android.AndroidInjection
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_illness_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class IllnessDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var addIllnessViewModelFactory: AddIllnessViewModelFactory
    lateinit var addIllnessViewModel: AddIllnessViewModel
    lateinit var illness: Illness
    lateinit var child: Child
    val MALE_GENDER = "мальчик"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectChildrenViewModel()
        setContentView(R.layout.activity_illness_detail)
        initActionBar()
        illness = intent.getParcelableExtra<Illness>(ILLNESS)
        addIllnessViewModel.getIllness(illness.id)
        child = intent.getParcelableExtra(CHILD)
        initFields(illness)
    }

    private fun injectChildrenViewModel() {
        AndroidInjection.inject(this)
        addIllnessViewModel = ViewModelProviders.of(this, addIllnessViewModelFactory).get(AddIllnessViewModel::class.java)
        addIllnessViewModel.initGettingIllnessObserver()
        addIllnessViewModel.initRemovingIllnessObserver()

        addIllnessViewModel.onGetIllnessByIdComplete().observe(this, Observer { illness -> initFields(illness)  })
        addIllnessViewModel.onGetIllnessError().observe(this, Observer { error -> showErrorToast(error) })

        addIllnessViewModel.onRemoveIllnessResult().observe(this, Observer { isSuccessfult -> if (isSuccessfult) showInfoToast(getString(R.string.illness_is_deleted))})
        addIllnessViewModel.onRemoveIllnessError().observe(this, Observer { error -> showErrorToast(error)})
    }

    private fun initActionBar(){
        setSupportActionBar(illnessDetailToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun initFields(illness: Illness){
        tvTitleAboutIllness.text = if (child.gender.equals(MALE_GENDER)) getString(R.string.illness_descrip_male,child.name,illness.date)
                                    else getString(R.string.illness_descrip_female,child.name,illness.date)
        tvIllnesName.text = illness.name
        tvSymptoms.text = illness.symptoms
        tvTreatment.text = illness.treatment
        treatmentPhoto.setImageURI(Uri.parse(illness.treatmentPhotoUri))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_illness_options,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.delete -> {
                DialogView.deletingConfirm(this, getString(R.string.deleting_illness_data_confirm), object : OnDialogItemSelected {
                    override fun onDialogClicked() {
                        deleteIllness()
                        finish()
                    }
                })
            }
            R.id.edit -> {
                openEditIllnessDataScreen()
            }
            R.id.share -> {
                showInfoToast("Share is clicked")
            }
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteIllness() {
        addIllnessViewModel.removeIllness(illness)
        addIllnessViewModel.removeIllnessFromFirebase(child.firebaseId!!, illness.firebaseId!!)
    }

    private fun openEditIllnessDataScreen() {
        startActivity(Intent(this,NewIllnessActivity::class.java).putExtra(Constants.ILLNESS,illness).putExtra(CHILD,child))
    }

    override fun onDestroy() {
        addIllnessViewModel.disposeGetIllnessObserver()
        addIllnessViewModel.disposeRemovingIllnessObserver()
        super.onDestroy()
    }
}
