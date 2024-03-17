package com.umairkhalid.i210455

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class book_session_activity : AppCompatActivity() {
    private var  mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.book_your_session)

        val back_btn: ImageButton =findViewById(R.id.back_arrow)
        val book_appoint_btn: Button =findViewById(R.id.book_appoint_btn)
        val message_btn: ImageButton =findViewById(R.id.message_btn)
        val audiocall_btn: ImageButton =findViewById(R.id.audiocall_btn)
        val videocall_btn: ImageButton =findViewById(R.id.videocall_btn)
        val time_btn_1 :Button = findViewById(R.id.time_btn_1)
        val time_btn_2 :Button = findViewById(R.id.time_btn_2)
        val time_btn_3 :Button = findViewById(R.id.time_btn_3)
        val calendar :CalendarView=findViewById(R.id.calender_1)


        var selected_time:String=time_btn_2.text.toString()

        // Get the current selected date from the CalendarView
        val currentDateMillis = calendar.date
        // Convert the millisecond value to a Date object
        val currentDate = Date(currentDateMillis)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var selectedDate: String = formatter.format(currentDate)

        val formattedDate = formatter.format(currentDate)
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
        }

        var c1 =0
        var c2 =0
        var c3 =0

        time_btn_1.setOnClickListener{
            selected_time=time_btn_1.text.toString()
            if(c1==0){
                time_btn_1.setBackgroundColor(Color.parseColor("#0b8fac"))
                c1=1
            }
            else{
                time_btn_1.setBackgroundColor(Color.parseColor("#ddefef"))
                c1=0
            }
        }

        time_btn_2.setOnClickListener{
            selected_time=time_btn_1.text.toString()
            if(c2==0){
                time_btn_2.setBackgroundColor(Color.parseColor("#0b8fac"))
                c2=1
            }
            else{
                time_btn_2.setBackgroundColor(Color.parseColor("#ddefef"))
                c2=0
            }
        }

        time_btn_3.setOnClickListener{
            selected_time=time_btn_1.text.toString()
            if(c3==0){
                time_btn_3.setBackgroundColor(Color.parseColor("#0b8fac"))
                c3=1
            }
            else{
                time_btn_3.setBackgroundColor(Color.parseColor("#ddefef"))
                c3=0
            }
        }


        val input_txt = intent.getStringExtra("user_name")

        val price: TextView =findViewById(R.id.txt_mentor_price)
        val user_img: ImageView =findViewById(R.id.mentor_image)
        val user_name: TextView =findViewById(R.id.txt_mentor_name)
        var mentor_occupation:String=""

        val database = FirebaseDatabase.getInstance()
        val mentorsRef = database.getReference("mentors")
        var profilePicUrl:String=""

        val query = mentorsRef.orderByChild("name").equalTo(input_txt.toString())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (mentorSnapshot in dataSnapshot.children) {

                    val name = mentorSnapshot.child("name").getValue(String::class.java)
                    val mentor_price = mentorSnapshot.child("name").getValue(String::class.java)
                    profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java).toString()
                    mentor_occupation = mentorSnapshot.child("occupation").getValue(String::class.java).toString()

                    // Check if all required fields are present
                    if (name != null && mentor_price!=null && profilePicUrl != null) {
                        user_name.text=name.toString()
                        price.text=mentor_price.toString()
                        Picasso.get().load(profilePicUrl).into(user_img)
                    }
                }
                // Notify your adapter that the data has changed
                // adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@book_session_activity,"Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
            }
        })


        back_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        book_appoint_btn.setOnClickListener{

            val database = FirebaseDatabase.getInstance()
            var my_ref = database.getReference("users")
            val curr = mAuth.currentUser
            val id= curr?.uid.toString()

            if(input_txt!=null){

                if(profilePicUrl!=""){

//                    my_ref.child(id).child("reviews").setValue(null)
                    my_ref.child(id).child("booked_sessions").child(input_txt).child("mentor_name").setValue(input_txt)
                    my_ref.child(id).child("booked_sessions").child(input_txt).child("occupation").setValue(mentor_occupation)
                    my_ref.child(id).child("booked_sessions").child(input_txt).child("date").setValue(selectedDate)
                    my_ref.child(id).child("booked_sessions").child(input_txt).child("time").setValue(selected_time)
                    my_ref.child(id).child("booked_sessions").child(input_txt).child("img_url").setValue(profilePicUrl)
                    Toast.makeText(this,"Session Booked Successfully", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"Fetching Data, Please Wait",Toast.LENGTH_LONG).show()
                }
                val nextActivityIntent = Intent(this, lets_find_activity::class.java)
                startActivity(nextActivityIntent)
                finish()

            }
            else{
                Toast.makeText(this,"Still Fetching Data from Database",Toast.LENGTH_LONG).show()

            }

        }

        message_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, chat_1_activity::class.java)
            nextActivityIntent.putExtra("MENTOR_NAME", input_txt)
            startActivity(nextActivityIntent)
//            val nextActivityIntent = Intent(this, chat_1_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        audiocall_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, call_1_activity::class.java)
            startActivity(nextActivityIntent)
        }

        videocall_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, call_2_activity::class.java)
            startActivity(nextActivityIntent)
        }


    }
}