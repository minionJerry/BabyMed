package com.kanykeinu.babymed

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kanykeinu.babymed.database.AppDatabase
import com.kanykeinu.babymed.model.Child
import com.kanykeinu.babymed.model.Illness
import com.kanykeinu.babymed.util.AgeUtil
import com.kanykeinu.babymed.util.CameraRequestUtil
import com.mikelau.croperino.CroperinoConfig
import com.mikelau.croperino.CroperinoFileUtil
import kotlinx.android.synthetic.main.activity_new_illness.*

class NewIllnessActivity : AppCompatActivity() {
    var child : Child? = null
    var uriTreatmentPhoto : Uri? = null

    private var database: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_illness)
        CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", Constants.DIRECTORY, Constants.RAW_DIRECTORY)
        CroperinoFileUtil.setupDirectory(this)
        child = intent.getParcelableExtra<Child>(Constants.CHILD)
        database = AppDatabase.getInstance(this)
        initCurrentDate()

        editTextIllnessName.setOnFocusChangeListener({_,focus ->
                if (!focus && editTextIllnessName.text.equals(""))
                    wrapperIllness.error = getString(R.string.empty_illness_not_allowed)
                else wrapperIllness.error = null
        })
        btnSaveIllness.setOnClickListener({
           addIllnessToDb()
        })
        btnAddPhoto.setOnClickListener({ v->
            CameraRequestUtil.showPictureDialog(this)
        })
    }

    fun addIllnessToDb(){
        var illness : Illness? = null
        if (editTextSymptoms.text.toString().equals("") || editTextTreatment.text.toString().equals("")
                || editTextIllnessName.text.equals(""))
            showToast(getString(R.string.empty_fields_not_allowed))
        else
        illness = Illness(0,editTextIllnessName.text.toString(),editTextSymptoms.text.toString(),editTextTreatment.text.toString(),
                    uriTreatmentPhoto.toString(),editTextDate.text.toString(), childId = child!!.id)
        database?.illnessDao()?.insert(illness!!)
        showToast(getString(R.string.data_saved))
        finish()
    }

    fun initCurrentDate(){
        editTextDate.setText(AgeUtil.getCurrentDate())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uriTreatmentPhoto = CameraRequestUtil.handleOnActivityResult(requestCode,resultCode,data,this)
        editTextTreatment.setCompoundDrawables(null,null,null,Drawable.createFromPath(uriTreatmentPhoto?.path))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        CameraRequestUtil.handleRequestPermissionResult(requestCode,grantResults,this)
    }
}
