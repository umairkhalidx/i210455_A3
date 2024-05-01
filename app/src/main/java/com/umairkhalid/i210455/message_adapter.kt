package com.umairkhalid.i210455

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class message_adapter(private val messages: List<message_data>, private val onMessageClickListener: OnMessageClickListener)
    : RecyclerView.Adapter<message_adapter.ViewHolder>() {

    interface OnMessageClickListener {
        fun onMessageClick(position: Int)
        fun onMessageLongClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private val audioIcon: ImageView = itemView.findViewById(R.id.audioIcon)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
//        private val display_pic : ImageView = itemView.findViewById(R.id.message_img)

        fun bind(message: message_data) {
            messageTextView.text = message.messageText

//            Glide.with(itemView.context)
//                .load(message.img)
//                .into(display_pic)

//            Glide.with(holder.itemView.context)
//                .load(itemslist[position].img)
//                .into(holder.display_pic)

            if (message.imageUrl != null) {
                audioIcon.visibility = View.GONE
                imageView.visibility = View.VISIBLE

//                val url = R.string.url
//                val imageURL = "${url}MessageImages/${message.imageUrl}"
                if(message.imageUrl!!.length>50){
                    val base64Image = message.imageUrl
                    val decodedBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
                    val bitmap = android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    imageView.setImageBitmap(bitmap)
                }else{
                    val url =  itemView.context.getString(R.string.url)
                    val imageURL = "${url}MessageImages/${message.imageUrl}"
                    Glide.with(itemView.context)
                    .load(imageURL)
                    .into(imageView)
                }


//                Glide.with(itemView.context)
//                    .load(imageURL)
//                    .into(imageView)

//                Glide.with(itemView.context)
//                    .load(message.imageUrl)
//                    .into(imageView)

//                Glide.with(itemView.context)
//                    .load(message.img)
//                    .into(display_pic)

            } else if (message.fileUrl != null) {
                audioIcon.visibility = View.GONE
                imageView.visibility = View.GONE
                messageTextView.text = message.fileUrl
//                val fileName = getFileNameFromUrl(message.fileUrl!!)
//                messageTextView.text = fileName
//                messageTextView.setOnClickListener {
//                    onMessageClickListener.onMessageClick(adapterPosition)
//                }
            } else if (message.audioUrl != null) {
                audioIcon.visibility = View.VISIBLE
                imageView.visibility = View.GONE
//                audioIcon.setOnClickListener {
//                    onMessageClickListener.onMessageClick(adapterPosition)
//                }
            } else {
                audioIcon.visibility = View.GONE
                imageView.visibility = View.GONE
            }

            itemView.setOnClickListener {
                onMessageClickListener.onMessageClick(adapterPosition)
            }

            itemView.setOnLongClickListener {
                onMessageClickListener.onMessageLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }

        private fun getFileNameFromUrl(fileUrl: String): String {
            val uri = Uri.parse(fileUrl)
            return uri.lastPathSegment ?: "File"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() =messages.size
}