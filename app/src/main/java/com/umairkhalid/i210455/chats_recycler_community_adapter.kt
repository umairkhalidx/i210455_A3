package com.umairkhalid.i210455

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class chats_recycler_community_adapter (val itemslist: ArrayList<chats_recycler_community_data>, private val listener: click_listner_community)
    : RecyclerView.Adapter<chats_recycler_community_adapter.chats_recycler_viewholder>()

{

    inner class chats_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rootView: View = itemView
        lateinit var display_pic : ImageView
        lateinit var title : TextView

        init {
            display_pic= itemView.findViewById(R.id.community_mentor_img)
            title = itemView.findViewById(R.id.community_mentor_name)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chats_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_community,parent,false)

        return chats_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: chats_recycler_viewholder, position: Int) {

//        holder.display_pic.setImageResource(itemslist[position].img)

        Glide.with(holder.itemView.context)
            .load(itemslist[position].img)
            .into(holder.display_pic)

        holder.title.setText(itemslist[position].title)

        holder.rootView.setOnClickListener{
            val txt:String =itemslist[position].title.toString()
            listener.chat_community_click_function(txt)
        }

    }

}