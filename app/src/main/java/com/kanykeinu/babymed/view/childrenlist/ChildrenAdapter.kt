package com.kanykeinu.babymed.view.childrenlist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanykeinu.babymed.utils.Constants
import com.kanykeinu.babymed.view.childdetail.ChildDetailActivity
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.utils.listen
import com.kanykeinu.babymed.data.source.local.entity.Child
import kotlinx.android.synthetic.main.children_list_item.view.*


class ChildrenAdapter(private val mContext: Context, private var objects: MutableList<Child>, private var onChildItemClick: OnChildItemClick) : RecyclerView.Adapter<ChildrenAdapter.ChildHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.children_list_item,parent,false)
        return ChildHolder(view)
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    override fun onBindViewHolder(holder: ChildHolder, position: Int) {
        holder.bind(objects.get(position),mContext)
        holder.listen({ pos, _ ->
            val item = objects.get(pos)
            onChildItemClick.onChildClick(item)
        })
    }

    fun addChildrenToList(children: List<Child>){
        objects = arrayListOf()
        (objects).addAll(children)
        notifyDataSetChanged()
    }

    class ChildHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(child: Child, mContext: Context) {
            itemView.childName.text = child.name
            itemView.childAge.text = Child.getCurrentAge(childBirthDate = child.birthDate).toString() + " лет"
            if (child.photoUri!=null)
                 Glide.with(mContext)
                    .load(child.photoUri)
                    .into(itemView.childPhoto);
        }

    }
}


interface OnChildItemClick{
    fun onChildClick(child: Child)
}