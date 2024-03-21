package com.umairkhalid.i210455

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class recycler_educator_adapter(val itemslist: ArrayList<recycler_educator_data>, private val listener: click_listner)
    : RecyclerView.Adapter<recycler_educator_adapter.educator_viewholder>()

{

    inner class educator_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rootView: View = itemView
        lateinit var display_pic :ImageView
        lateinit var title : TextView
        lateinit var designation : TextView
        lateinit var status : TextView
        lateinit var price:TextView
        lateinit var heart:ImageView
        var my_flag:Int=0

        init {
            display_pic= itemView.findViewById(R.id.display_image)
            title = itemView.findViewById(R.id.title)
            designation = itemView.findViewById(R.id.designation)
            status = itemView.findViewById(R.id.status)
            price=itemView.findViewById(R.id.edu_price)
            heart=itemView.findViewById(R.id.heart_btn)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): educator_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_educator,parent,false)

        return educator_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: educator_viewholder, position: Int) {

        holder.rootView.setOnClickListener{
            val txt:String =itemslist[position].title.toString()
            listener.click_function(txt)
//            Toast.makeText(holder.itemView.context,"Clicked on a Row",Toast.LENGTH_SHORT).show()

        }
        holder.heart.setOnClickListener{
            if(holder.my_flag==0){
                holder.heart.setImageResource(R.drawable.red_heart_btn)
                val txt:String =itemslist[position].title.toString()
                listener.change_heart(holder.my_flag,txt)
                holder.my_flag=1

            }else{
                holder.heart.setImageResource(R.drawable.heart_unfilled)
                val txt:String =itemslist[position].title.toString()
                listener.change_heart(holder.my_flag,txt)
                holder.my_flag=0
            }
        }


//        holder.display_pic.setImageResource(itemslist[position].img)
        Glide.with(holder.itemView.context)
            .load(itemslist[position].img)
            .into(holder.display_pic)

        holder.title.setText(itemslist[position].title)
        holder.designation.setText(itemslist[position].desig)
        holder.status.setText(itemslist[position].status)
        holder.price.setText(itemslist[position].price)
        holder.heart.setImageResource(itemslist[position].heart_img)
        holder.my_flag = itemslist[position].flag

    }

}