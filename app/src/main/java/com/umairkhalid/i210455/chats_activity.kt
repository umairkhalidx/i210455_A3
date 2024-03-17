package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class chats_activity : AppCompatActivity() , click_listner,click_listner_community{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.chats)

        val home_btn: ImageButton =findViewById(R.id.home_btn)
        val home_txt: TextView =findViewById(R.id.home_txt)
        val search_btn: ImageButton =findViewById(R.id.search_btn)
        val search_txt: TextView =findViewById(R.id.search_txt)
        val chat_btn: ImageButton =findViewById(R.id.chat_btn)
        val chat_txt: TextView =findViewById(R.id.chat_txt)
        val profile_btn: ImageButton =findViewById(R.id.profile_btn)
        val profile_txt: TextView =findViewById(R.id.profile_txt)
        val plus_btn: ImageButton =findViewById(R.id.plus_btn)
        val back_btn_letsfind: ImageButton =findViewById(R.id.back_btn)

        home_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        home_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        search_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            startActivity(nextActivityIntent)
        }

        search_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            startActivity(nextActivityIntent)
        }

        chat_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            startActivity(nextActivityIntent)
        }

        chat_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            startActivity(nextActivityIntent)
        }

        profile_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            startActivity(nextActivityIntent)
        }

        profile_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            startActivity(nextActivityIntent)
        }

        back_btn_letsfind.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }



        // 1- AdapterView: RecyclerView
        val recyclerView : RecyclerView = findViewById(R.id.chats_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )


        // 2- Data Source: List of  Objects
        var adapter_data_list : ArrayList<chats_recycler_data> = ArrayList()

        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid.toString()
        val messagesRef = database.reference.child("users").child(userId).child("messages")


        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (conversationSnapshot in snapshot.children) {
                    for (messageSnapshot in conversationSnapshot.children) {
                        val temp_user_Id = messageSnapshot.child("userId").getValue(String::class.java).toString()
                        val conversationName = conversationSnapshot.key
                        val mentorName = conversationName?.substringAfter("_")

                        if (temp_user_Id == userId) {

                            val database_2 = FirebaseDatabase.getInstance()
                            val mentorsRef = database_2.getReference("mentors")

                            val query = mentorsRef.orderByChild("name").equalTo(mentorName)

                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (mentorSnapshot in dataSnapshot.children) {
                                        val name = mentorSnapshot.child("name").getValue(String::class.java)
                                        val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)

//                                        if (name != null && adapter_data_list.none { it.title == name }) {
//                                            val mentorData = chats_recycler_data(profilePicUrl, name)
//                                            adapter_data_list.add(mentorData)
//                                        }

                                        // Check if all required fields are present
                                        if (name != null) {
                                            val mentorData = chats_recycler_data(
                                                profilePicUrl,
                                                name
                                            )
                                            var exists = false

                                            for (data in adapter_data_list) {
                                                if (data.title == name) {
                                                    exists = true
                                                    break
                                                }
                                            }

                                            if (!exists) {
                                                adapter_data_list.add(mentorData)
                                            }
                                        }
                                    }
                                    val adapter = chats_recycler_adapter(adapter_data_list,this@chats_activity)
                                    recyclerView.adapter = adapter

                                    // Notify your adapter that the data has changed
                                    // adapter.notifyDataSetChanged()
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle error
                                    Toast.makeText(this@chats_activity,"Unable to Fetch Mentor Data",Toast.LENGTH_LONG).show()
                                }
                            })
                            break;
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
            }
        })

//        val v1  = chats_recycler_data(R.drawable.rectangle_blank,"John Cooper")
//        val v2  = chats_recycler_data(R.drawable.rectangle_blank,"Jack Watson")
//        val v3  = chats_recycler_data(R.drawable.rectangle_blank,"Emma Philips")
//
////        val v1  = chats_recycler_data(R.drawable.rectangle_blank,"John Cooper","1 New Message")
////        val v2  = chats_recycler_data(R.drawable.rectangle_blank,"Jack Watson","No New Message")
////        val v3  = chats_recycler_data(R.drawable.rectangle_blank,"Emma Philips","No New Message")
//
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//
//        // 3- Adapter
//        val adapter = chats_recycler_adapter(adapter_data_list,this)
//        recyclerView.adapter = adapter


        // 1- AdapterView: RecyclerView
        val recyclerView_community : RecyclerView = findViewById(R.id.chats_recyclerview_community)
        recyclerView_community.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        // 2- Data Source: List of  Objects
        var adapter_data_list_community : ArrayList<chats_recycler_community_data> = ArrayList()

        val database_2 = FirebaseDatabase.getInstance()
        val messagesRef_2 = database_2.reference.child("community")
        val curr = FirebaseAuth.getInstance().currentUser
        val userId_2 = curr?.uid.toString()

        messagesRef_2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (conversationSnapshot in snapshot.children) {
                    for (messageSnapshot in conversationSnapshot.children) {
                        val temp_user_Id_2 = messageSnapshot.child("userId").getValue(String::class.java).toString()
                        val conversationName = conversationSnapshot.key
                        val mentorName = conversationName?.substringAfter("_")

                        if (temp_user_Id_2 == userId_2) {

                            val database_2 = FirebaseDatabase.getInstance()
                            val mentorsRef = database_2.getReference("mentors")

                            val query = mentorsRef.orderByChild("name").equalTo(mentorName)

                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (mentorSnapshot in dataSnapshot.children) {
                                        val name = mentorSnapshot.child("name").getValue(String::class.java)
                                        val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)

//                                        if (name != null && adapter_data_list.none { it.title == name }) {
//                                            val mentorData = chats_recycler_data(profilePicUrl, name)
//                                            adapter_data_list.add(mentorData)
//                                        }

                                        // Check if all required fields are present
                                        if (name != null) {
                                            val mentorData = chats_recycler_community_data(
                                                profilePicUrl,
                                                name
                                            )
                                            var exists = false

                                            for (data in adapter_data_list_community) {
                                                if (data.title == name) {
                                                    exists = true
                                                    break
                                                }
                                            }

                                            if (!exists) {
                                                adapter_data_list_community.add(mentorData)
                                            }
                                        }
                                    }
                                    val adapter_2 = chats_recycler_community_adapter(adapter_data_list_community,this@chats_activity)
                                    recyclerView_community.adapter = adapter_2

                                    // Notify your adapter that the data has changed
                                    // adapter.notifyDataSetChanged()
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle error
                                    Toast.makeText(this@chats_activity,"Unable to Community Data",Toast.LENGTH_LONG).show()
                                }
                            })
                            break;
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
            }
        })

    }

    override fun click_function(txt:String){
        val nextActivityIntent = Intent(this, chat_1_activity::class.java)
        nextActivityIntent.putExtra("MENTOR_NAME", txt)
        startActivity(nextActivityIntent)

    }

    override fun chat_community_click_function(txt:String){
        val nextActivityIntent = Intent(this, chat_2_activity::class.java)
        nextActivityIntent.putExtra("MENTOR_NAME", txt)
        startActivity(nextActivityIntent)

    }
}