package com.kanykeinu.babymed.view.addeditillness

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
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
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_new_child.*
import kotlinx.android.synthetic.main.activity_new_illness.*
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject

class NewIllnessActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    @Inject
    lateinit var addIllnessViewModelFactory: AddIllnessViewModelFactory

    lateinit var addIllnessViewModel: AddIllnessViewModel
    var child : Child? = null
    var uriTreatmentPhoto : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectAddChildViewModel()
        setContentView(R.layout.activity_new_illness)
        initActionBar()
        CroperinoConfig(PHOTO_NAME, Constants.DIRECTORY, Constants.RAW_DIRECTORY)
        CroperinoFileUtil.setupDirectory(this)
        child = intent.getParcelableExtra<Child>(Constants.CHILD)
        tvIllnessTitle.append(child?.name)
        validateFieldsOnFocus()
        btnAddPhoto.setOnClickListener{CameraRequestHandler.showPictureDialog(this)}
        btnSaveIllness.setOnClickListener{validateFieldsAndAddIllness()}
        editTextDate.setOnClickListener{setIllnessDate()}
    }

    private fun injectAddChildViewModel(){
        AndroidInjection.inject(this)
        addIllnessViewModel = ViewModelProviders.of(this,addIllnessViewModelFactory).get(AddIllnessViewModel::class.java)
        addIllnessViewModel.initDisposableObserver()

        addIllnessViewModel.onError().observe( this, androidx.lifecycle.Observer { error ->
            if (error!=null)
                showToast(error)
        })

        addIllnessViewModel.onComplete().observe(this, androidx.lifecycle.Observer {
            showToast(getString(R.string.data_saved))
            finish()
        })
    }

    private fun initActionBar(){
        setSupportActionBar(newIllnessToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
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
                || editTextIllnessName.text!!.equals(""))
            showToast(getString(R.string.empty_fields_not_allowed))
        else {
            val illness = Illness(0, editTextIllnessName.text.toString(), editTextSymptoms.text.toString(), editTextTreatment.text.toString(),
                    uriTreatmentPhoto.toString(), editTextDate.text.toString(), childId = child!!.id, illnessWeight = child!!.weight!!)
            addIllnessViewModel.saveIllness(illness)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uriTreatmentPhoto = CameraRequestHandler.handleOnActivityResult(requestCode,resultCode,data,this)
        editTextTreatment.setCompoundDrawablesWithIntrinsicBounds(null,null,null,Drawable.createFromPath(uriTreatmentPhoto?.path))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        CameraRequestHandler.handleRequestPermissionResult(requestCode,grantResults,this)
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
        editTextDate.setText("""${dayOfMonth} ${DateFormatSymbols(Locale.getDefault()).months[monthOfYear]} ${year}""")
    }

    override fun onDestroy() {
        addIllnessViewModel.disposeObserver()
        super.onDestroy()
    }
}
