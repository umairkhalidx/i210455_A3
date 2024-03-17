package com.umairkhalid.i210455

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class recycler_booked_adapter(val itemslist: ArrayList<recycler_booked_data>, private val listener: click_listner) :
    RecyclerView.Adapter<recycler_booked_adapter.booked_viewholder>()

{
//    class recycler_searchresults_adapter(val itemslist: ArrayList<recycler_searchresults_data> , private val listener: click_listner) :
//        RecyclerView.Adapter<recycler_searchresults_adapter.searchresult_viewholder>()
//
//    {

    inner class booked_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rootView: View = itemView
        lateinit var display_pic : ImageView
        lateinit var title : TextView
        lateinit var occupation: TextView
        lateinit var date : TextView
        lateinit var time: TextView

        init {
            display_pic= itemView.findViewById(R.id.mentor_image)
            title = itemView.findViewById(R.id.mentor_name)
            occupation = itemView.findViewById(R.id.mentor_title)
            date = itemView.findViewById(R.id.session_date)
            time=itemView.findViewById(R.id.session_time)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): booked_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_booked_item,parent,false)

        return booked_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: booked_viewholder, position: Int) {

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
        holder.occupation.setText(itemslist[position].occu)
        holder.date.setText(itemslist[position].date)
        holder.time.setText(itemslist[position].time)

    }

}