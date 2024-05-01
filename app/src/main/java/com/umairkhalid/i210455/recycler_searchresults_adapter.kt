package com.umairkhalid.i210455

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class recycler_searchresults_adapter(val itemslist: ArrayList<recycler_searchresults_data>, private val listener: click_listner) :
RecyclerView.Adapter<recycler_searchresults_adapter.searchresult_viewholder>()

{
//    class recycler_searchresults_adapter(val itemslist: ArrayList<recycler_searchresults_data> , private val listener: click_listner) :
//        RecyclerView.Adapter<recycler_searchresults_adapter.searchresult_viewholder>()
//
//    {

        inner class searchresult_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val rootView: View = itemView
            lateinit var display_pic :ImageView
            lateinit var title : TextView
            lateinit var designation : TextView
            lateinit var status : TextView
            lateinit var price:TextView
            lateinit var heart:ImageView
            lateinit var mentorID : String
            var my_flag:Int=0

            init {
                display_pic= itemView.findViewById(R.id.display_image)
                title = itemView.findViewById(R.id.title)
                designation = itemView.findViewById(R.id.designation)
                status = itemView.findViewById(R.id.status)
                price=itemView.findViewById(R.id.mentor_price)
                heart=itemView.findViewById(R.id.heart_button)

            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchresult_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_searchresults,parent,false)

        return searchresult_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: searchresult_viewholder, position: Int) {

        holder.rootView.setOnClickListener{
            val txt:String =itemslist[position].title.toString()
            listener.click_function(txt,holder.mentorID)
//            Toast.makeText(holder.itemView.context,"Clicked on a Row",Toast.LENGTH_SHORT).show()

        }

        holder.heart.setOnClickListener{
            if(holder.my_flag==0){
                holder.heart.setImageResource(R.drawable.red_heart_btn)
                val txt:String =itemslist[position].title.toString()
                listener.change_heart(holder.my_flag,txt,holder.mentorID)
                holder.my_flag=1

            }else{
                holder.heart.setImageResource(R.drawable.heart_unfilled)
                val txt:String =itemslist[position].title.toString()
                listener.change_heart(holder.my_flag,txt,holder.mentorID)
                holder.my_flag=0
            }
        }

//        holder.display_pic.setImageResource(itemslist[position].img)

        val url = holder.itemView.context.getString(R.string.url)
        val imageURL = "${url}MentorImages/${itemslist[position].img}"

        Glide.with(holder.itemView.context)
            .load(imageURL)
            .into(holder.display_pic)

        holder.mentorID= itemslist[position].mentorID.toString()
        holder.title.setText(itemslist[position].title)
        holder.designation.setText(itemslist[position].desig)
        holder.status.setText(itemslist[position].status)
        holder.price.setText(itemslist[position].price)
        holder.heart.setImageResource(itemslist[position].heart_img)
        holder.my_flag = itemslist[position].flag

    }

}