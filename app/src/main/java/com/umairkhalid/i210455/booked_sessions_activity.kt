package com.umairkhalid.i210455

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class booked_sessions_activity : AppCompatActivity() , click_listner{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.booked_sessions)

        val back_btn: ImageButton =findViewById(R.id.back_btn)

        back_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }
        if (!isNetworkAvailable()) {
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show()

            val recyclerView_booked : RecyclerView = findViewById(R.id.recycleview_booked_sessions)
            recyclerView_booked.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false
            )

            // Initialize SharedPreferences
            val sharedPref = getSharedPreferences("booked_prefs", Context.MODE_PRIVATE)
            val json = sharedPref.getString("booked_list", "")

            val gson = Gson()
            val type = object : TypeToken<ArrayList<recycler_booked_data>>() {}.type
            val adapter_data_list_booked: ArrayList<recycler_booked_data> = gson.fromJson(json, type)


            val adapter_rev = recycler_booked_adapter(adapter_data_list_booked,this@booked_sessions_activity)
            recyclerView_booked.adapter = adapter_rev

            // Notify your adapter that the data has changed
            adapter_rev.notifyDataSetChanged()


        }

        val recyclerView_booked : RecyclerView = findViewById(R.id.recycleview_booked_sessions)
        recyclerView_booked.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        var adapter_data_list_booked : ArrayList<recycler_booked_data> = ArrayList()

        var database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val id = currentUser?.uid

        if(id!=null){

            database = FirebaseDatabase.getInstance()
            val mentorsRef = database.getReference("users").child(id).child("booked_sessions") // Replace with your actual reference

            mentorsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (mentorSnapshot in dataSnapshot.children) {
                        val name = mentorSnapshot.child("mentor_name").getValue(String::class.java)
                        val occupation = mentorSnapshot.child("occupation").getValue(String::class.java)
                        val date= mentorSnapshot.child("date").getValue(String::class.java)
                        val time= mentorSnapshot.child("time").getValue(String::class.java)
                        val profilePicUrl=mentorSnapshot.child("img_url").getValue(String::class.java)

                        // Check if all required fields are present
                        if (name != null && occupation!= null && date!=null && time!=null) {
                            val mentorData = recycler_booked_data(
                                profilePicUrl,
                                name,
                                occupation,
                                date,
                                time
                            )
                            adapter_data_list_booked.add(mentorData)

                            val gson = Gson()
                            val json = gson.toJson(adapter_data_list_booked)
                            val sharedPref = getSharedPreferences("booked_prefs", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putString("booked_list", json)
                            editor.apply()
                        }
                    }

                    val adapter_rev = recycler_booked_adapter(adapter_data_list_booked,this@booked_sessions_activity)
                    recyclerView_booked.adapter = adapter_rev

                    // Notify your adapter that the data has changed
                    adapter_rev.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })


        }

    }
    override fun click_function(txt:String){
        val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
        nextActivityIntent.putExtra("user_name", txt)
        startActivity(nextActivityIntent)

    }
    override fun change_heart(flag:Int,txt:String){
        TODO()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}