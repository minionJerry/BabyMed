package com.kanykeinu.babymed.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kanykeinu.babymed.Constants
import com.kanykeinu.babymed.MedicalFileActivity
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.listen
import com.kanykeinu.babymed.util.AgeUtil
import com.kanykeinu.babymed.model.Child
import kotlinx.android.synthetic.main.children_list_item.view.*


class ChildAdapter(private val mContext: Context, private var objects: List<Child>) : RecyclerView.Adapter<ChildAdapter.ChildHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.children_list_item,parent,false)
        return ChildHolder(view)
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    override fun onBindViewHolder(holder: ChildHolder, position: Int) {
        holder.bind(objects.get(position),mContext)
        holder.listen({ pos, type ->
            val item = objects.get(pos)
            mContext.startActivity(Intent(mContext,MedicalFileActivity::class.java).putExtra(Constants.CHILD,item))
        })
    }

    class ChildHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(child: Child,mContext: Context) {
            itemView.childName.text = child.name
            itemView.childAge.text = AgeUtil.getCurrentAge(childBirthDate = child.birthDate).toString() + " лет"
            Glide.with(mContext)
                    .load(child?.photoUri)
                    .into(itemView.childPhoto);
        }

    }
}