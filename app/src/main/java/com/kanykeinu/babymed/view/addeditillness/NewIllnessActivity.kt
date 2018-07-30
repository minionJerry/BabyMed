package com.kanykeinu.babymed.view.addeditillness

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import com.kanykeinu.babymed.utils.*
import com.kanykeinu.babymed.utils.Constants.PHOTO_NAME
import com.mikelau.croperino.CroperinoConfig
import com.mikelau.croperino.CroperinoFileUtil
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_new_illness.*
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject

class NewIllnessActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    @Inject
    lateinit var addIllnessViewModelFactory: AddIllnessViewModelFactory
    lateinit var addIllnessViewModel: AddIllnessViewModel
    private var child : Child? = null
    private var illness : Illness? = null
    private var uriTreatmentPhoto : Uri? = null
    lateinit var cameraRequestHandler: CameraRequestHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectAddChildViewModel()
        setContentView(R.layout.activity_new_illness)
        initActionBar()
        setupCameraConfigs()
        child = intent.getParcelableExtra<Child>(Constants.CHILD)
        illness = intent.getParcelableExtra(Constants.ILLNESS)
        if (illness == null) tvIllnessTitle.append(" " + child?.name)
        else {
            tvIllnessTitle.setText(getString(R.string.illness_editing, child?.name))
            tvIllnessTitle.gravity = Gravity.CENTER
            uriTreatmentPhoto = Uri.parse(illness?.treatmentPhotoUri)
        }
        fillFields()
        validateFieldsOnFocus()
        btnAddPhoto.setOnClickListener{cameraRequestHandler.showPictureDialog()}
        btnSaveIllness.setOnClickListener{validateFieldsAndAddIllness()}
        editTextDate.setOnClickListener{setIllnessDate()}
    }

    private fun injectAddChildViewModel(){
        AndroidInjection.inject(this)
        addIllnessViewModel = ViewModelProviders.of(this,addIllnessViewModelFactory).get(AddIllnessViewModel::class.java)
        addIllnessViewModel.initDisposableObserver()

        addIllnessViewModel.onError().observe( this, androidx.lifecycle.Observer { error ->
            if (error!=null)
                showErrorToast(error)
        })

        addIllnessViewModel.onComplete().observe(this, androidx.lifecycle.Observer {
            showSuccessToast(getString(R.string.data_saved))
            finish()
        })

        addIllnessViewModel.onSuccessUpdating().observe(this, androidx.lifecycle.Observer { isUpdated ->
            if (isUpdated) {
                showSuccessToast(getString(R.string.data_saved))
                finish()
            }
        })
    }

    private fun initActionBar(){
        setSupportActionBar(newIllnessToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun setupCameraConfigs(){
        cameraRequestHandler = CameraRequestHandler(this)
        CroperinoConfig(PHOTO_NAME, Constants.DIRECTORY, Constants.RAW_DIRECTORY)
        CroperinoFileUtil.setupDirectory(this)
    }

    private fun fillFields(){
        if (illness!=null){
            editTextIllnessName.setText(illness!!.name)
            editTextDate.setText(illness!!.date)
            editTextSymptoms.setText(illness!!.symptoms)
            editTextTreatment.setText(illness!!.treatment)
            imgViewTreatmentPhoto.visibility = View.VISIBLE
            imgViewTreatmentPhoto.setImageURI(Uri.parse(illness!!.treatmentPhotoUri))
        }
    }

    private  fun validateFieldsOnFocus(){
        editTextIllnessName.setOnFocusChangeListener{_,focus ->
            if (!focus && editTextIllnessName.text!!.equals(""))
                wrapperIllness.error = getString(R.string.empty_illness_not_allowed)
            else wrapperIllness.error = null
        }
    }

    private fun validateFieldsAndAddIllness(){
        if (editTextSymptoms.text.toString().equals("") || editTextTreatment.text.toString().equals("")
                || editTextIllnessName.text!!.equals("") || editTextDate.text.toString().equals(""))
            showErrorToast(getString(R.string.empty_fields_not_allowed))
        else {
            if (illness!=null)
                updateExistingIllness()
            else saveNewIllness()
        }
    }

    private fun updateExistingIllness(){
        val illnessId = illness!!.id
        val illness = Illness(illnessId, editTextIllnessName.text.toString(), editTextSymptoms.text.toString(), editTextTreatment.text.toString(),
                      uriTreatmentPhoto.toString(), editTextDate.text.toString(), childId = child!!.id, illnessWeight = child!!.weight!!)
        addIllnessViewModel.updateIllness(illness)
    }


    private fun saveNewIllness(){
        val illness = Illness(0, editTextIllnessName.text.toString(), editTextSymptoms.text.toString(), editTextTreatment.text.toString(),
                      uriTreatmentPhoto.toString(), editTextDate.text.toString(), childId = child!!.id, illnessWeight = child!!.weight!!)
        addIllnessViewModel.saveIllness(illness)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uriTreatmentPhoto = cameraRequestHandler.handleOnActivityResult(requestCode,resultCode,data)
        imgViewTreatmentPhoto.setImageURI(uriTreatmentPhoto)
        imgViewTreatmentPhoto.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraRequestHandler.handleRequestPermissionResult(requestCode,grantResults)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setIllnessDate(){
        val calendar = Calendar.getInstance()
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)
        val yy = calendar.get(Calendar.YEAR)
        SpinnerDatePickerDialogBuilder()
                .context(this)
                .callback(this)
                .maxDate(yy,mm,dd)
                .defaultDate(yy,mm,dd)
                .build()
                .show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        editTextDate.setText("${dayOfMonth} ${DateFormatSymbols(Locale.getDefault()).months[monthOfYear]} ${year}")
    }

    override fun onDestroy() {
        addIllnessViewModel.disposeObserver()
        super.onDestroy()
    }
}
