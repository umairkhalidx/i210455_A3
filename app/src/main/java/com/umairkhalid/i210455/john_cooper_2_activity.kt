package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class john_cooper_2_activity : AppCompatActivity() {
    private var  mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.john_cooper_2)

        val back_btn: ImageButton =findViewById(R.id.back_arrow)
        val feedback_btn: Button =findViewById(R.id.feedback_btn)

        val input_txt = intent.getStringExtra("user_name")

        val review:TextView=findViewById(R.id.txt_type)
        val user_img:ImageView=findViewById(R.id.mentor_img)
        val user_name:TextView=findViewById(R.id.txt_username_review)

        val database = FirebaseDatabase.getInstance()
        val mentorsRef = database.getReference("mentors")

        val query = mentorsRef.orderByChild("name").equalTo(input_txt.toString())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (mentorSnapshot in dataSnapshot.children) {

                    val name = mentorSnapshot.child("name").getValue(String::class.java)
                    val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)

                    // Check if all required fields are present
                    if (name != null && profilePicUrl != null) {
                        user_name.text=name.toString()
                        Picasso.get().load(profilePicUrl).into(user_img)
                    }
                }


                // Notify your adapter that the data has changed
                // adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@john_cooper_2_activity,"Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
            }
        })


        back_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
            onBackPressed()
            finish()
        }

        feedback_btn.setOnClickListener{
            if(review.text!=null){

                val database = FirebaseDatabase.getInstance()
                var my_ref = database.getReference("users")
                val curr = mAuth.currentUser
                val id= curr?.uid.toString()

                if(input_txt!=null){
//                    my_ref.child(id).child("reviews").setValue(null)
                    my_ref.child(id).child("reviews").child(input_txt).child("mentor_name").setValue(input_txt)
                    my_ref.child(id).child("reviews").child(input_txt).child("mentor_review").setValue(review.text.toString())
                    Toast.makeText(this,"Feedback Added Successfully", Toast.LENGTH_LONG).show()
                }

            }
            else{
                Toast.makeText(this,"Please fill Feedback", Toast.LENGTH_LONG).show()
            }
            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }
    }
}