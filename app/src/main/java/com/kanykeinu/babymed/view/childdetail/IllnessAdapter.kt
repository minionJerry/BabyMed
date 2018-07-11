package com.kanykeinu.babymed.view.childdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.data.source.local.entity.Illness
import kotlinx.android.synthetic.main.illness_list_item.view.*


class IllnessAdapter(private val mContext: Context, private var objects: List<Illness>, private var onAgeSet: OnAgeSet) : RecyclerView.Adapter<IllnessAdapter.IllnessHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IllnessHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.illness_list_item,parent,false)
        return IllnessHolder(view)
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    override fun onBindViewHolder(holder: IllnessHolder, position: Int) {
        holder.bind(objects.get(position))
        val age : Int? = onAgeSet.getChildAge(objects.get(position).childId)
        holder.getChildAgeEditText()?.text = "в " + age + " лет"
    }

    class IllnessHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bind(illness: Illness) {
            itemView.tvIllnessName.text = illness.name
        }

        fun getChildAgeEditText() : TextView? {
            return itemView.tvIllChildAge
        }
    }

    interface OnAgeSet{
        fun getChildAge(id : Long) : Int
    }
}