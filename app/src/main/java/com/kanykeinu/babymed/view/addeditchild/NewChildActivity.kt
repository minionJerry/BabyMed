package com.kanykeinu.babymed.view.addeditchild

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.utils.Constants.DIRECTORY
import com.kanykeinu.babymed.utils.Constants.RAW_DIRECTORY
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.utils.CameraRequestHandler
import com.kanykeinu.babymed.utils.CameraRequestHandler.Companion.handleOnActivityResult
import com.kanykeinu.babymed.utils.CameraRequestHandler.Companion.handleRequestPermissionResult
import com.kanykeinu.babymed.utils.Constants.PHOTO_NAME
import com.kanykeinu.babymed.utils.DialogView
import com.kanykeinu.babymed.utils.showToast
import com.mikelau.croperino.CroperinoConfig
import com.mikelau.croperino.CroperinoFileUtil
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_new_child.*
import java.lang.ref.WeakReference
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject

class NewChildActivity : AppCompatActivity() , View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var addEditChildViewModelFactory: AddEditChildViewModelFactory
    lateinit var addEditChildViewModel: AddEditChildViewModel
    private var uriPhoto: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectAddChildViewModel()
        setContentView(R.layout.activity_new_child)
        initActionBar()
        CroperinoConfig(PHOTO_NAME, DIRECTORY, RAW_DIRECTORY)
        CroperinoFileUtil.setupDirectory(this)
        imgChildPhoto.setOnClickListener(this)
        editTextBirthDate.setOnClickListener(this)
        editTextGender.setOnClickListener(this)
        editTextName.setOnFocusChangeListener(this)
        editTextBirthDate.setOnFocusChangeListener(this)
        btnSave.setOnClickListener(this)
    }

    private fun injectAddChildViewModel(){
        AndroidInjection.inject(this)
        addEditChildViewModel = ViewModelProviders.of(this,addEditChildViewModelFactory).get(AddEditChildViewModel::class.java)
        addEditChildViewModel.initDisposableObserver()

        addEditChildViewModel.onError().observe( this, androidx.lifecycle.Observer { error ->
            if (error!=null)
                showToast(error)
        })

        addEditChildViewModel.onComplete().observe(this, androidx.lifecycle.Observer {
            showToast(getString(R.string.data_saved))
            finish()
        })
    }

    private fun initActionBar(){
        setSupportActionBar(newChildToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onClick(v: View?) {
        when(v){
            btnSave -> {
                validateFieldsAndAddChild()
            }
            imgChildPhoto -> {
                CameraRequestHandler.showPictureDialog(this)
            }
            editTextGender -> {
                DialogView.genderDialog(this, WeakReference(editTextGender))
            }
            editTextBirthDate -> {
                setChildBirthdate()
            }
            else ->{

            }
        }
    }

    fun validateFieldsAndAddChild(){
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
                    weight, uriPhoto.toString(), bloodType)
            Log.e("Child", child.toString())
            addEditChildViewModel.saveChild(child)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            android.R.id.home -> onBackPressed ()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        addEditChildViewModel.disposeObserver()
        super.onDestroy()
    }
}


