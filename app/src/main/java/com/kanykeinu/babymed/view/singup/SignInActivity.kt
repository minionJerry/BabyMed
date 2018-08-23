package com.kanykeinu.babymed.view.singup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.R.id.*
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import com.kanykeinu.babymed.data.source.local.sharedpref.SharedPreferencesManager
import com.kanykeinu.babymed.data.source.remote.firebase.FirebaseHandler
import com.kanykeinu.babymed.data.source.remote.firebase.User
import com.kanykeinu.babymed.utils.showErrorToast
import com.kanykeinu.babymed.utils.showSuccessToast
import com.kanykeinu.babymed.view.addeditchild.AddEditChildViewModel
import com.kanykeinu.babymed.view.addeditchild.AddEditChildViewModelFactory
import com.kanykeinu.babymed.view.addeditillness.AddIllnessViewModel
import com.kanykeinu.babymed.view.addeditillness.AddIllnessViewModelFactory
import com.kanykeinu.babymed.view.childrenlist.ChildrenActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity : BaseAuthActivity() {
    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    lateinit var userViewModel: UserViewModel
    @Inject
    lateinit var prefs : SharedPreferencesManager
    @Inject
    lateinit var firebaseHandler: FirebaseHandler
    @Inject
    lateinit var addEditChildViewModelFactory: AddEditChildViewModelFactory
    lateinit var addEditChildViewModel: AddEditChildViewModel
    @Inject
    lateinit var addEditIllnessViewModelFactory : AddIllnessViewModelFactory
    lateinit var addEditIllnessViewModel: AddIllnessViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectViewModels()
        //user initially should be null
        userViewModel.signOutUser()
        prefs.saveUserId(null)

        setContentView(R.layout.activity_sign_in)
        btnRegister.setOnClickListener { goToRegistrationScreen() }
        btnAuth.setOnClickListener{ singInUser()}
    }

    private fun injectViewModels(){
        AndroidInjection.inject(this)
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)
        addEditChildViewModel = ViewModelProviders.of(this, addEditChildViewModelFactory).get(AddEditChildViewModel::class.java)
        addEditIllnessViewModel = ViewModelProviders.of(this, addEditIllnessViewModelFactory).get(AddIllnessViewModel::class.java)

        addEditChildViewModel.initChildSavingObserver()
        addEditChildViewModel.initChildrenDeletingObserver()
        addEditIllnessViewModel.initSaveChildObserver()

        addEditChildViewModel.onCompleteSaveUpdate().observe(this, Observer { isSuccessfull ->
            if (isSuccessfull) showSuccessToast(getString(R.string.data_saved)) })
        addEditChildViewModel.onSaveUpdateError().observe(this, Observer { error -> showErrorToast(error) })

        addEditIllnessViewModel.onSaveChildComplete().observe(this, Observer { isSuccessfull -> showSuccessToast(getString(R.string.data_saved)) })
        addEditIllnessViewModel.onSaveChildError().observe(this, Observer { error -> showErrorToast(error) })

        addEditChildViewModel.onChildrenDeletingComplete().observe(this, Observer { isSuccessfull ->
            if (isSuccessfull){
                showSuccessToast("blabla")
                goToChildrenList()
            }
        })
        addEditChildViewModel.onChildrenDeletingError().observe(this, Observer { error -> showErrorToast(error) })
    }

    private fun goToRegistrationScreen(){
        startActivity(Intent(this,RegisterUserActivity::class.java))
    }

    private fun singInUser() {
        progressBar.visibility = VISIBLE
        progressBar.show()
        registerObservers()
        if (!isLoginFieldEmpty() && isPasswordNotEmptyAndValidated()) {
            userViewModel.signIn(eTextLogin.text.toString(),eTextPassword.text.toString())
        }
    }

    private fun registerObservers() {
        userViewModel.initSignInObserver()

        userViewModel.onSignInSuccess().observe(this, Observer { isSuccessfull ->
            progressBar.hide()
            if (isSuccessfull) {
                showSuccessToast(getString(R.string.welcome,firebaseHandler.getCurrentUser()?.displayName))
                prefs.saveUserId(firebaseHandler.getCurrentUser()!!.uid)
                retrieveUserChildren()
            }
        })

        userViewModel.onSignInError().observe(this, Observer { error ->
            if (error!=null) {
                progressBar.hide()
                showErrorToast(error)
                userViewModel.onSignInError().postValue(null)
            }
        })
    }

    fun retrieveUserChildren(){
        userViewModel.retrieveUserChildren(firebaseHandler.getCurrentUser()!!.uid, object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                showErrorToast(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                clearDatabase()
                if (p0.exists()) {
                    saveDataFromFirebaseToRoom(p0)
                }

            }
        })
    }

    fun saveDataFromFirebaseToRoom(p0 : DataSnapshot){
        for (childSnapshot in p0.children) {
            val child : com.kanykeinu.babymed.data.source.remote.firebase.Child = childSnapshot.getValue(com.kanykeinu.babymed.data.source.remote.firebase.Child::class.java)!!
            val firebaseChildId = childSnapshot.key
                val roomChild = Child(0, firebaseChildId, child.name, child.birthDate, child.gender, child.weight, child.photoUri, child.bloodType)
                addEditChildViewModel.saveChild(roomChild)
                saveIllnessesFromFirebaseToRoom(child as com.kanykeinu.babymed.data.source.remote.firebase.Child, childSnapshot, roomChild)
        }
    }

    fun saveIllnessesFromFirebaseToRoom(child : com.kanykeinu.babymed.data.source.remote.firebase.Child, childSnapshot : DataSnapshot, roomChild : Child){
        if (child.illnessList != null && child.illnessList.isNotEmpty()) {
            val childIllnesses = child.illnessList
            for (illness in childIllnesses) {
                val firebaseIllnessId = childSnapshot.child("illnessList").children.iterator().next().key
                val roomIllness = Illness(0, firebaseIllnessId, illness.name, illness.symptoms, illness.treatment, illness.treatmentPhotoUri,
                        illness.date, illness.illnessWeight, roomChild.id)
                addEditIllnessViewModel.saveIllness(roomIllness)
            }
        }
    }

    fun clearDatabase(){
        addEditChildViewModel.clearChildrenTable()
        addEditIllnessViewModel.clearIllnessesTable()
    }

    private fun goToChildrenList(){
        startActivity(Intent(this, ChildrenActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        userViewModel.disposeSignInObserver()
        addEditChildViewModel.disposeChildSavingObserver()
        addEditChildViewModel.disposeChildrenDeletingObserver()
        addEditIllnessViewModel.disposeSaveChildObserver()
        super.onDestroy()
    }
}