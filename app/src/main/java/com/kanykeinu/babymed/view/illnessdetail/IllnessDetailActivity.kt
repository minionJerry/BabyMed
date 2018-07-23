package com.kanykeinu.babymed.view.illnessdetail

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import com.kanykeinu.babymed.utils.Constants.CHILD
import com.kanykeinu.babymed.utils.Constants.ILLNESS
import com.kanykeinu.babymed.utils.DialogView
import com.kanykeinu.babymed.utils.OnDialogItemSelected
import com.kanykeinu.babymed.utils.showToast
import kotlinx.android.synthetic.main.activity_illness_detail.*

class IllnessDetailActivity : AppCompatActivity() {

    lateinit var illness: Illness
    lateinit var child: Child
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_illness_detail)
        initActionBar()
        illness = intent.getParcelableExtra<Illness>(ILLNESS)
        child = intent.getParcelableExtra(CHILD)
        initFields()
    }

    private fun initActionBar(){
        setSupportActionBar(illnessDetailToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun initFields(){
        tvTitleAboutIllness.text = getString(R.string.illness_descrip,child.name,illness.date)
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
//                        deleteChild()
                        finish()

                    }
                })
            }
            R.id.edit -> {
//                openEditChildDataScreen()
            }
            R.id.share -> {
                showToast("Share is clicked")
            }
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
