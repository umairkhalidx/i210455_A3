package com.umairkhalid.i210455

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class chats_recycler_adapter(val itemslist: ArrayList<chats_recycler_data>)
    : RecyclerView.Adapter<chats_recycler_adapter.chats_recycler_viewholder>()

{

    inner class chats_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var display_pic :ImageView
        lateinit var title : TextView
        lateinit var message_count : TextView

        init {
            display_pic= itemView.findViewById(R.id.display_image)
            title = itemView.findViewById(R.id.name_title)
            message_count = itemView.findViewById(R.id.messeages)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chats_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.chats_recycler_view,parent,false)

        return chats_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: chats_recycler_viewholder, position: Int) {

        holder.display_pic.setImageResource(itemslist[position].img)
        holder.title.setText(itemslist[position].title)
        holder.message_count.setText(itemslist[position].messagecount)

    }

}