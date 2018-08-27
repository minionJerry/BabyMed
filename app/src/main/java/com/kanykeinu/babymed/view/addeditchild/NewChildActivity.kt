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
import com.bumptech.glide.Glide
import com.kanykeinu.babymed.BabyMedApplication_MembersInjector
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.data.source.BabyMedRepository
import com.kanykeinu.babymed.utils.Constants.DIRECTORY
import com.kanykeinu.babymed.utils.Constants.RAW_DIRECTORY
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.sharedpref.SharedPreferencesManager
import com.kanykeinu.babymed.utils.*
import com.kanykeinu.babymed.utils.Constants.CHILD
import com.kanykeinu.babymed.utils.Constants.PHOTO_NAME
import com.mikelau.croperino.CroperinoConfig
import com.mikelau.croperino.CroperinoFileUtil
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_new_child.*
import kotlinx.android.synthetic.main.children_list_item.view.*
import java.lang.ref.WeakReference
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject

class NewChildActivity : AppCompatActivity() , View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager
    @Inject
    lateinit var addEditChildViewModelFactory: AddEditChildViewModelFactory
    lateinit var addEditChildViewModel: AddEditChildViewModel
    private var uriPhoto: Uri? = null
    private var child : Child? = null
    lateinit var cameraRequestHandler: CameraRequestHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectAddChildViewModel()
        setContentView(R.layout.activity_new_child)
        initActionBar()
        child = intent.getParcelableExtra(CHILD)
        if (child!=null)
            getChildFromIntentAndInitFields(child!!)
        setupCameraCropConfigs()
        imgChildPhoto.setOnClickListener(this)
        editTextBirthDate.setOnClickListener(this)
        editTextBirthDate.setOnFocusChangeListener(this)
        editTextGender.setOnClickListener(this)
        editTextName.setOnFocusChangeListener(this)
        btnSave.setOnClickListener(this)
    }

    private fun injectAddChildViewModel(){
        AndroidInjection.inject(this)
        addEditChildViewModel = ViewModelProviders.of(this,addEditChildViewModelFactory).get(AddEditChildViewModel::class.java)
        addEditChildViewModel.initChildSavingObserver()
        addEditChildViewModel.initChildUpdatingObserver()

        addEditChildViewModel.onCompleteSaveUpdate().observe(this, androidx.lifecycle.Observer { isSuccessfull ->
            if (isSuccessfull)  {
                showSuccessToast(getString(R.string.data_saved))
                finish()
            } })
        addEditChildViewModel.onSaveUpdateError().observe(this, androidx.lifecycle.Observer { error -> showErrorToast(error) })
    }

    private fun initActionBar(){
        setSupportActionBar(newChildToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun getChildFromIntentAndInitFields(child: Child){
            editTextName.setText(child.name)
            editTextBirthDate.setText(child.birthDate)
            editTextWeight.setText(child.weight.toString())
            editTextBloodType.setText(child.bloodType.toString())
            editTextGender.setText(child.gender)
            uriPhoto = Uri.parse(child.photoUri)
            if (!uriPhoto.toString().equals("null"))
                imgChildPhoto.setImageURI(uriPhoto)
    }

    private fun setupCameraCropConfigs(){
        cameraRequestHandler = CameraRequestHandler(this)
        CroperinoConfig(PHOTO_NAME, DIRECTORY, RAW_DIRECTORY)
        CroperinoFileUtil.setupDirectory(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btnSave -> {
                saveOrUpdateChild()
            }
            imageButton -> {
                cameraRequestHandler.showPictureDialog()
            }
            imgChildPhoto -> {
                cameraRequestHandler.showPictureDialog()
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

    private fun saveOrUpdateChild(){
        val areFieldsFalidated = validateFields()
        if (areFieldsFalidated) {
            wrapperBirthDate.error = null
            textInputLayout.error = null
            val weight = if (!editTextWeight.text.toString().equals("")) editTextWeight.text.toString().toInt() else null
            val bloodType = if (!editTextBloodType.text.toString().equals("")) editTextBloodType.text.toString().toInt() else null

            Log.e("Child", child.toString())
            if (child == null) {
               saveChild(weight ,bloodType)
            }
            else {
               updateChild(weight ,bloodType)
            }
        }
    }

    fun saveChild(weight : Int?, bloodType : Int?){
        val firebaseChild = com.kanykeinu.babymed.data.source.remote.firebase.Child(null,editTextName.text.toString(), editTextBirthDate.text.toString(), editTextGender.text.toString(),
                weight, uriPhoto.toString(), bloodType,sharedPreferencesManager.getUserId(),null)
        val newChild = Child(0, null, firebaseChild.name, firebaseChild.birthDate, firebaseChild.gender,
                firebaseChild.weight, firebaseChild.photoUri, firebaseChild.bloodType)
        val childFirebaseId = addEditChildViewModel.saveChildToFirebase(firebaseChild)
        firebaseChild.id = childFirebaseId
        addEditChildViewModel.updateChildFromFirebase(childFirebaseId!!,firebaseChild)
        newChild.firebaseId = childFirebaseId
        addEditChildViewModel.saveChild(newChild)
    }

    fun updateChild(weight: Int?,bloodType: Int?){
        val newChild = Child(child!!.id, child!!.firebaseId, editTextName.text.toString(), editTextBirthDate.text.toString(), editTextGender.text.toString(),
                weight, uriPhoto.toString(), bloodType)
        val firebaseChild = com.kanykeinu.babymed.data.source.remote.firebase.Child(newChild.firebaseId,newChild.name,newChild.birthDate,newChild.gender,newChild.weight,
                newChild.photoUri,newChild.bloodType,sharedPreferencesManager.getUserId(),null)
        addEditChildViewModel.updateChild(newChild)
        addEditChildViewModel.updateChildFromFirebase(child!!.firebaseId!!,firebaseChild)
    }

    private fun validateFields() : Boolean{
        if (editTextBirthDate.text.toString().equals("")) {
            wrapperBirthDate.error = getString(R.string.enter_birthdate)
            return false
        }
        else if (editTextName.text.toString().equals("")) {
            textInputLayout.error = getString(R.string.enter_name)
            return false
        }
        else return true
    }

    private fun setChildBirthdate(){
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
        val uri : Uri?  = cameraRequestHandler.handleOnActivityResult(requestCode,resultCode,data)
        if (uri!=null)
        {
            imgChildPhoto.setImageURI(uri)
            uriPhoto = uri
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        cameraRequestHandler.handleRequestPermissionResult(requestCode,grantResults)
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
       val date = dayOfMonth.toString() + " " + DateFormatSymbols(Locale.getDefault()).months[monthOfYear] + " " + year.toString()
        editTextBirthDate.setText(date)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        addEditChildViewModel.disposeChildSavingObserver()
        addEditChildViewModel.disposeChildUpdatingObserver()
        super.onDestroy()
    }
}
