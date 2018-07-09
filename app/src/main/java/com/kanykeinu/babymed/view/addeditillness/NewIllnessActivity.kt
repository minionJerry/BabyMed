package com.kanykeinu.babymed.view.addeditillness

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import com.kanykeinu.babymed.utils.CameraRequestHandler
import com.kanykeinu.babymed.utils.Constants
import com.kanykeinu.babymed.utils.Constants.PHOTO_NAME
import com.kanykeinu.babymed.utils.showToast
import com.kanykeinu.babymed.view.addeditchild.AddEditChildViewModel
import com.kanykeinu.babymed.view.addeditchild.AddEditChildViewModelFactory
import com.mikelau.croperino.CroperinoConfig
import com.mikelau.croperino.CroperinoFileUtil
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_new_illness.*
import javax.inject.Inject

class NewIllnessActivity : AppCompatActivity() {

    var child : Child? = null
    var uriTreatmentPhoto : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_new_illness)
        CroperinoConfig(PHOTO_NAME, Constants.DIRECTORY, Constants.RAW_DIRECTORY)
        CroperinoFileUtil.setupDirectory(this)
        child = intent.getParcelableExtra<Child>(Constants.CHILD)
        initCurrentDate()

        editTextIllnessName.setOnFocusChangeListener({_,focus ->
                if (!focus && editTextIllnessName.text!!.equals(""))
                    wrapperIllness.error = getString(R.string.empty_illness_not_allowed)
                else wrapperIllness.error = null
        })
        btnSaveIllness.setOnClickListener({
           addIllnessToDb()
        })
        btnAddPhoto.setOnClickListener({
            CameraRequestHandler.showPictureDialog(this)
        })
    }

    fun addIllnessToDb(){
        var illness : Illness? = null
        if (editTextSymptoms.text.toString().equals("") || editTextTreatment.text.toString().equals("")
                || editTextIllnessName.text!!.equals(""))
            showToast(getString(R.string.empty_fields_not_allowed))
        else
        illness = Illness(0, editTextIllnessName.text.toString(), editTextSymptoms.text.toString(), editTextTreatment.text.toString(),
                uriTreatmentPhoto.toString(), editTextDate.text.toString(), childId = child!!.id)
        showToast(getString(R.string.data_saved))
        finish()
    }

    fun initCurrentDate(){
        editTextDate.setText(Child.getCurrentDate())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uriTreatmentPhoto = CameraRequestHandler.handleOnActivityResult(requestCode,resultCode,data,this)
        editTextTreatment.setCompoundDrawables(null,null,null,Drawable.createFromPath(uriTreatmentPhoto?.path))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        CameraRequestHandler.handleRequestPermissionResult(requestCode,grantResults,this)
    }
}
