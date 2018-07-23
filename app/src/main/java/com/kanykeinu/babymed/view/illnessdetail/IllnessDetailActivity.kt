package com.kanykeinu.babymed.view.illnessdetail

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import com.kanykeinu.babymed.utils.Constants.CHILD
import com.kanykeinu.babymed.utils.Constants.ILLNESS
import kotlinx.android.synthetic.main.activity_illness_detail.*
import kotlinx.android.synthetic.main.activity_medical_file.*

class IllnessDetailActivity : AppCompatActivity() {

    lateinit var illness: Illness
    lateinit var child: Child
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_illness_detail)
        illness = intent.getParcelableExtra<Illness>(ILLNESS)
        child = intent.getParcelableExtra(CHILD)
        initFields()
    }


    private fun initFields(){
        tvTitleAboutIllness.text = getString(R.string.illness_descrip,child.name,illness.date)
        tvIllnesName.text = illness.name
        tvSymptoms.text = illness.symptoms
        tvTreatment.text = illness.treatment
        tvTreatment.setCompoundDrawablesWithIntrinsicBounds(null,null,null, Drawable.createFromPath(illness.treatmentPhotoUri))

    }
}
