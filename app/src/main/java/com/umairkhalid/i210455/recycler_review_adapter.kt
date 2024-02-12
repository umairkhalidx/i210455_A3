package com.umairkhalid.i210455

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class recycler_review_adapter(val itemslist: ArrayList<recycler_review_data>)
    : RecyclerView.Adapter<recycler_review_adapter.review_viewholder>()

{

    inner class review_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var title : TextView
        lateinit var description : TextView

        init {
            title = itemView.findViewById(R.id.review_title)
            description = itemView.findViewById(R.id.review_description)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): review_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_review,parent,false)

        return review_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: review_viewholder, position: Int) {

        holder.title.setText(itemslist[position].title)
        holder.description.setText(itemslist[position].descrip)

    }

}