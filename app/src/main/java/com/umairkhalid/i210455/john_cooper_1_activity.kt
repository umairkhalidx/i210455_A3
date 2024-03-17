package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class john_cooper_1_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.john_cooper_1)


        val back_btn: ImageButton =findViewById(R.id.back_arrow)
        val dropreview_btn: Button =findViewById(R.id.review_btn)
        val community_btn: Button =findViewById(R.id.community_btn)
        val book_session_btn: Button =findViewById(R.id.book_session_btn)

        val user_img:ImageView=findViewById(R.id.user_img)
        val user_name:TextView=findViewById(R.id.user_name_txt)
        val user_desc:TextView=findViewById(R.id.user_desc)
        val user_occu:TextView=findViewById(R.id.user_occupation_txt)

        val input_txt = intent.getStringExtra("user_name")

        val database = FirebaseDatabase.getInstance()
        val mentorsRef = database.getReference("mentors")

        val query = mentorsRef.orderByChild("name").equalTo(input_txt.toString())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (mentorSnapshot in dataSnapshot.children) {

                    val name = mentorSnapshot.child("name").getValue(String::class.java)
                    val occupation = mentorSnapshot.child("occupation").getValue(String::class.java)
                    val desc = mentorSnapshot.child("description").getValue(String::class.java)
                    val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)


                    // Check if all required fields are present
                    if (name != null && occupation != null && desc!=null && profilePicUrl != null) {
                        user_name.text=name.toString()
                        user_desc.text=desc.toString()
                        user_occu.text=occupation.toString()

                        Picasso.get().load(profilePicUrl).into(user_img)

                    }
                }


                // Notify your adapter that the data has changed
                // adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@john_cooper_1_activity,"Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
            }
        })


        back_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        dropreview_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, john_cooper_2_activity::class.java)
            nextActivityIntent.putExtra("user_name", input_txt)
            startActivity(nextActivityIntent)
        }

        community_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, chat_2_activity::class.java)
            startActivity(nextActivityIntent)
        }

        book_session_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, book_session_activity::class.java)
            nextActivityIntent.putExtra("user_name", input_txt)
            startActivity(nextActivityIntent)
        }
    }
}