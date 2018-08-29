package com.kanykeinu.babymed.view.childrenlist

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.widget.toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.R.id.*
import com.kanykeinu.babymed.data.source.local.sharedpref.SharedPreferencesManager
import com.kanykeinu.babymed.utils.showInfoToast
import com.kanykeinu.babymed.utils.showSuccessToast
import com.kanykeinu.babymed.view.singup.SignInActivity
import com.kanykeinu.babymed.view.singup.UserViewModel
import com.kanykeinu.babymed.view.singup.UserViewModelFactory
import com.kanykeinu.babymed.view.singup.UserViewModelFactory_Factory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import kotlinx.android.synthetic.main.fragment_item_list_dialog.*
import javax.inject.Inject

class BottomNavigationDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvUsernameDialog.text = FirebaseAuth.getInstance().currentUser?.displayName
        tvUserEmail.text = FirebaseAuth.getInstance().currentUser?.email

        navigationView.setNavigationItemSelectedListener {
            menuItem ->
            // Bottom Navigation Drawer menu item clicks
            when (menuItem.itemId) {
                myChildren -> { this.dismiss()}
                sendFeedback -> context?.showInfoToast("my children")
                exit -> {
                    goToSignInScreen()
                }
            }
            true
        }
    }


    private fun goToSignInScreen(){
        startActivity(Intent(context,SignInActivity::class.java))
        activity?.finish()
    }

    private fun disableNavigationViewScrollbars(navigation_view: NavigationView?) {
        val navigationMenuView = navigation_view?.getChildAt(0) as NavigationMenuView
        navigationMenuView.isVerticalScrollBarEnabled = false
        navigationMenuView.isSelected = true
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  BottomSheetDialog(requireContext(),theme)
        return dialog
    }

    companion object {
        private val USERNAME = "USERNAME"

        fun newInstance(username : String?): BottomNavigationDialogFragment {
            val args: Bundle = Bundle()
            args.putSerializable(USERNAME, username)
            val fragment = BottomNavigationDialogFragment()
            fragment.arguments = args
            return fragment
        }

    }
}
