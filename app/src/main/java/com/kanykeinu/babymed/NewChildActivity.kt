package com.kanykeinu.babymed

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.kanykeinu.babymed.util.CameraRequestUtil.Companion.allowToPickAvatar
import com.kanykeinu.babymed.Constants.DIRECTORY
import com.kanykeinu.babymed.Constants.RAW_DIRECTORY
import com.kanykeinu.babymed.Constants.REQUEST_CODE_CAMERA
import com.kanykeinu.babymed.Constants.REQUEST_CODE_GALLERY
import com.kanykeinu.babymed.database.AppDatabase
import com.kanykeinu.babymed.model.Child
import com.kanykeinu.babymed.util.CameraRequestUtil
import com.kanykeinu.babymed.util.CameraRequestUtil.Companion.handleOnActivityResult
import com.kanykeinu.babymed.util.CameraRequestUtil.Companion.handleRequestPermissionResult
import com.kanykeinu.babymed.util.DialogUtil
import com.mikelau.croperino.Croperino
import com.mikelau.croperino.CroperinoConfig
import com.mikelau.croperino.CroperinoFileUtil
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_new_child.*
import java.text.DateFormatSymbols
import java.util.*
import javax.security.auth.callback.Callback

class NewChildActivity : AppCompatActivity() , View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {


    private var database: AppDatabase? = null
    private var uriPhoto: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_child)
        CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", DIRECTORY, RAW_DIRECTORY)
        CroperinoFileUtil.setupDirectory(this)
        database = AppDatabase.getInstance(this)
        btnAddChild.setOnClickListener(this)
        imgChildPhoto.setOnClickListener(this)
        editTextBirthDate.setOnClickListener(this)
        editTextGender.setOnClickListener(this)
        editTextName.setOnFocusChangeListener(this)
        editTextBirthDate.setOnFocusChangeListener(this)
    }

    fun addNewChildToDb(){
        if (editTextBirthDate.text.toString().equals(""))
            wrapperBirthDate.error = getString(R.string.enter_birthdate)
        else if (editTextName.text.toString().equals(""))
            textInputLayout.error = getString(R.string.enter_name)
        else {
            wrapperBirthDate.error = null
            textInputLayout.error = null
            val weight = if (!editTextWeight.text.toString().equals("")) editTextWeight.text.toString().toInt() else null
            val bloodType = if (!editTextBloodType.text.toString().equals("")) editTextBloodType.text.toString().toInt() else null
            val child = Child(0, editTextName.text.toString(), editTextBirthDate.text.toString(), editTextGender.text.toString(),
                    weight, uriPhoto.toString(),bloodType)
            Log.e("Child", child.toString())
            database?.childDao()?.insert(child)
            showToast(getString(R.string.data_saved))
            finish()
        }
    }

    override fun onClick(v: View?) {
        when(v){
            btnAddChild -> {
                addNewChildToDb()
            }
            imgChildPhoto -> {
                CameraRequestUtil.showPictureDialog(this)
            }
            editTextGender -> {
                DialogUtil.genderDialog(this,editTextGender)
            }
            editTextBirthDate -> {
                setChildBirthdate()
            }
            else ->{

            }
        }
    }

    fun setChildBirthdate(){
        val calendar = Calendar.getInstance()
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)
        SpinnerDatePickerDialogBuilder()
                .context(this)
                .callback(this)
                .minDate(2000, mm, dd)
                .build()
                .show()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when(v){
            editTextName -> {
                if (hasFocus == false && editTextName.text.toString().equals(""))
                    textInputLayout.error =  getString(R.string.enter_name)
                else textInputLayout.error = null
            }
            editTextBirthDate -> {
                if (hasFocus == false && editTextBirthDate.text.toString().equals(""))
                    wrapperBirthDate.error =  getString(R.string.enter_birthdate)
                else wrapperBirthDate.error = null
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri : Uri?  = handleOnActivityResult(requestCode,resultCode,data,this)
        if (uri!=null)
        {
            imgChildPhoto.setImageURI(uri)
            uriPhoto = uri
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        handleRequestPermissionResult(requestCode,grantResults,this)
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
       val date = dayOfMonth.toString() + " " + DateFormatSymbols(Locale.getDefault()).months[monthOfYear] + " " + year.toString()
        editTextBirthDate.setText(date)
    }

}


