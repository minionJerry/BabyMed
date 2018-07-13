package com.kanykeinu.babymed.view.childdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kanykeinu.babymed.R
import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import kotlinx.android.synthetic.main.illness_list_item.view.*


class IllnessAdapter(private val mContext: Context, private var objects: List<Illness>, private var onAgeSet: OnAgeAndWeightSet) : RecyclerView.Adapter<IllnessAdapter.IllnessHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IllnessHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.illness_list_item,parent,false)
        return IllnessHolder(view)
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    override fun onBindViewHolder(holder: IllnessHolder, position: Int) {
        holder.bind(objects.get(position))
        val child : Child = onAgeSet.getChildAge(objects.get(position).childId)
        holder.getChildAgeEditText()?.text = "в " + Child.getCurrentAge(child.birthDate) + " лет" + "при массе " + child.weight
    }

    class IllnessHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bind(illness: Illness) {
            itemView.tvIllnessName.text = illness.name
        }

        fun getChildAgeEditText() : TextView? {
            return itemView.tvIllChildIllnessDetails
        }
    }

    interface OnAgeAndWeightSet{
        fun getChildAge(id : Long) : Child
    }
}