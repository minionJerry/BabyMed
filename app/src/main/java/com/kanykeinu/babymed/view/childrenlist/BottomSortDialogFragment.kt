package com.kanykeinu.babymed.view.childrenlist

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.kanykeinu.babymed.R;
import com.kanykeinu.babymed.utils.showInfoToast
import com.kanykeinu.babymed.view.addeditchild.AddEditChildViewModel
import com.kanykeinu.babymed.view.addeditchild.AddEditChildViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_item_list_dialog.*
import kotlinx.android.synthetic.main.fragment_sort_list_dialog.*
import kotlinx.android.synthetic.main.fragment_sort_list_dialog_item.view.*
import javax.inject.Inject



class BottomSortDialogFragment : BottomSheetDialogFragment() {
    private var onSortChildrenClick: OnSortChildrenClick? = null

    fun setOnSortChildrenListener(onSortChildrenClick: OnSortChildrenClick){
        this.onSortChildrenClick = onSortChildrenClick
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sort_list_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigationViewSort.setNavigationItemSelectedListener {
            menuItem ->
            when (menuItem.itemId) {
                R.id.sortByName -> {
                    onSortChildrenClick?.sortByName()
                }
                R.id.sortByBirthdateAsc -> {
                    onSortChildrenClick?.sortByBirthdateAsc()
                }
                R.id.sortByBirthdateDesc -> {
                    onSortChildrenClick?.sortByBirthdateDesc()
                }
            }
            true
        }
    }

    companion object {
        fun newInstance(): BottomSortDialogFragment = BottomSortDialogFragment()
    }
}

interface OnSortChildrenClick{
   fun sortByName()
   fun sortByBirthdateAsc()
   fun sortByBirthdateDesc()
}
