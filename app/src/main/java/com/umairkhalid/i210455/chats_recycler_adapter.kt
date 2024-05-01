package com.umairkhalid.i210455

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class chats_recycler_adapter(val itemslist: ArrayList<chats_recycler_data>, private val listener: click_listner)
    : RecyclerView.Adapter<chats_recycler_adapter.chats_recycler_viewholder>()

{

    inner class chats_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rootView: View = itemView
        lateinit var display_pic :ImageView
        lateinit var title : TextView
//        lateinit var message_count : TextView
        lateinit var mentorID : String

        init {
            display_pic= itemView.findViewById(R.id.display_image)
            title = itemView.findViewById(R.id.name_title)
//            message_count = itemView.findViewById(R.id.messeages)

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

//        holder.display_pic.setImageResource(itemslist[position].img)
        val url = holder.itemView.context.getString(R.string.url)
        val imageURL = "${url}MentorImages/${itemslist[position].img}"
        Glide.with(holder.itemView.context)
            .load(imageURL)
            .into(holder.display_pic)

        holder.title.setText(itemslist[position].title)
        holder.mentorID= itemslist[position].mentorID.toString()

        holder.rootView.setOnClickListener{
            val txt:String =itemslist[position].title.toString()
            listener.click_function(txt,holder.mentorID)
//            Toast.makeText(holder.itemView.context,"Clicked on a Row",Toast.LENGTH_SHORT).show()

        }

//        holder.message_count.setText(itemslist[position].messagecount)

    }

}