package com.umairkhalid.i210455

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

            init {
                display_pic= itemView.findViewById(R.id.display_image)
                title = itemView.findViewById(R.id.title)
                designation = itemView.findViewById(R.id.designation)
                status = itemView.findViewById(R.id.status)
                price=itemView.findViewById(R.id.mentor_price)

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
            listener.click_function(txt)
//            Toast.makeText(holder.itemView.context,"Clicked on a Row",Toast.LENGTH_SHORT).show()

        }

//        holder.display_pic.setImageResource(itemslist[position].img)
        Glide.with(holder.itemView.context)
            .load(itemslist[position].img)
            .into(holder.display_pic)

        holder.title.setText(itemslist[position].title)
        holder.designation.setText(itemslist[position].desig)
        holder.status.setText(itemslist[position].status)
        holder.price.setText(itemslist[position].price)

    }

}