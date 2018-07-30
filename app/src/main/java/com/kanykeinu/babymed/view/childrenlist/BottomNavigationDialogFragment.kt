package com.kanykeinu.babymed.view.childrenlist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.widget.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.utils.showInfoToast
import com.kanykeinu.babymed.utils.showSuccessToast
import kotlinx.android.synthetic.main.fragment_item_list_dialog.*

// TODO: Customize parameter argument names
class BottomNavigationDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener {
            menuItem ->
            // Bottom Navigation Drawer menu item clicks
            when (menuItem.itemId) {
                R.id.myChildren -> { this.dismiss()}
                R.id.sendFeedback -> context?.showInfoToast("my children")
                R.id.exit -> context?.showInfoToast("my children")
            }
            true
        }
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

        // TODO: Customize parameters
        fun newInstance(): BottomNavigationDialogFragment =
                BottomNavigationDialogFragment()

    }
}
