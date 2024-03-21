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

class search_results_activity : AppCompatActivity(), click_listner {
    private var  mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.search_results)

        val home_btn: ImageButton =findViewById(R.id.home_btn)
        val home_txt: TextView =findViewById(R.id.home_txt)
        val search_btn: ImageButton =findViewById(R.id.search_btn)
        val search_txt: TextView =findViewById(R.id.search_txt)
        val chat_btn: ImageButton =findViewById(R.id.chat_btn)
        val chat_txt: TextView =findViewById(R.id.chat_txt)
        val profile_btn: ImageButton =findViewById(R.id.profile_btn)
        val profile_txt: TextView =findViewById(R.id.profile_txt)
        val plus_btn: ImageButton =findViewById(R.id.plus_btn)
        val back_btn_search: ImageButton =findViewById(R.id.back_btn)


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

        back_btn_search.setOnClickListener{
//            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }

        // 1- AdapterView: RecyclerView
        val recyclerView : RecyclerView = findViewById(R.id.recycleview_searched_results)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )


        // 2- Data Source: List of  Objects
        var adapter_data_list : ArrayList<recycler_searchresults_data> = ArrayList()

        val input_txt = intent.getStringExtra("search_txt")

        if(input_txt=="Entrepreneurship" || input_txt=="Personal" || input_txt=="Education" ){
            val database = FirebaseDatabase.getInstance()
            val mentorsRef = database.getReference("mentors")

            val query = mentorsRef.limitToFirst(7) // Limit the query to the first 4 mentors

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (mentorSnapshot in dataSnapshot.children) {
                        val name = mentorSnapshot.child("name").getValue(String::class.java)
                        val occupation = mentorSnapshot.child("occupation").getValue(String::class.java)
                        val price = mentorSnapshot.child("price").getValue(String::class.java)
                        val status = mentorSnapshot.child("status").getValue(String::class.java)
                        val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)

                        // Check if all required fields are present
                        if (name != null && occupation != null && price != null && status != null) {
                            val curr_usr = mAuth.currentUser
                            val user_id = curr_usr?.uid.toString()
                            val userRef = database.getReference("users").child(user_id)

                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.hasChild("favourite")) {
                                        val fav_ref = userRef.child("favourite")
                                        fav_ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    if (dataSnapshot.hasChild(name)) {
                                                        val mentorData = recycler_searchresults_data(
                                                            profilePicUrl,
                                                            name,
                                                            occupation,
                                                            status,
                                                            price,R.drawable.red_heart_btn,1
                                                        )
                                                        adapter_data_list.add(mentorData)
                                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
                                                        recyclerView.adapter = adapter

                                                        // Notify your adapter that the data has changed
                                                        adapter.notifyDataSetChanged()

                                                    } else {
                                                        val mentorData = recycler_searchresults_data(
                                                            profilePicUrl,
                                                            name,
                                                            occupation,
                                                            status,
                                                            price,R.drawable.heart_unfilled,0
                                                        )
                                                        adapter_data_list.add(mentorData)
                                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
                                                        recyclerView.adapter = adapter

                                                        // Notify your adapter that the data has changed
                                                        adapter.notifyDataSetChanged()

                                                    }
                                                }
                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {
                                                // Handle error
                                            }
                                        })
                                    } else {

                                        val mentorData = recycler_searchresults_data(
                                            profilePicUrl,
                                            name,
                                            occupation,
                                            status,
                                            price,R.drawable.heart_unfilled,0
                                        )
                                        adapter_data_list.add(mentorData)
                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
                                        recyclerView.adapter = adapter

                                        // Notify your adapter that the data has changed
                                        adapter.notifyDataSetChanged()

                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle error
                                }
                            })
                        }
                    }

//                    val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
//                    recyclerView.adapter = adapter
//
//                    // Notify your adapter that the data has changed
//                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Toast.makeText(this@search_results_activity, "Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
                }
            })
        }
        else{

            val database = FirebaseDatabase.getInstance()
            val mentorsRef = database.getReference("mentors")

            val query = mentorsRef.orderByChild("name").equalTo(input_txt.toString())

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (mentorSnapshot in dataSnapshot.children) {
                        val name = mentorSnapshot.child("name").getValue(String::class.java)
                        val occupation = mentorSnapshot.child("occupation").getValue(String::class.java)
                        val price = mentorSnapshot.child("price").getValue(String::class.java)
                        val status = mentorSnapshot.child("status").getValue(String::class.java)
                        val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)


                        // Check if all required fields are present
                        if (name != null && occupation != null && price != null && status != null) {
                            val curr_usr = mAuth.currentUser
                            val user_id = curr_usr?.uid.toString()
                            val userRef = database.getReference("users").child(user_id)

                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.hasChild("favourite")) {
                                        val fav_ref = userRef.child("favourite")
                                        fav_ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    if (dataSnapshot.hasChild(name)) {
                                                        val mentorData = recycler_searchresults_data(
                                                            profilePicUrl,
                                                            name,
                                                            occupation,
                                                            status,
                                                            price,R.drawable.red_heart_btn,1
                                                        )
                                                        adapter_data_list.add(mentorData)
                                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
                                                        recyclerView.adapter = adapter
                                                        Toast.makeText(this@search_results_activity,"SUII1",Toast.LENGTH_SHORT).show()


                                                        // Notify your adapter that the data has changed
                                                        adapter.notifyDataSetChanged()

                                                    } else {
                                                        val mentorData = recycler_searchresults_data(
                                                            profilePicUrl,
                                                            name,
                                                            occupation,
                                                            status,
                                                            price,R.drawable.heart_unfilled,0
                                                        )
                                                        adapter_data_list.add(mentorData)
                                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
                                                        recyclerView.adapter = adapter

                                                        Toast.makeText(this@search_results_activity,"SUII2",Toast.LENGTH_SHORT).show()

                                                        // Notify your adapter that the data has changed
                                                        adapter.notifyDataSetChanged()

                                                    }
                                                }
                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {
                                                // Handle error
                                            }
                                        })
                                    } else {

                                        val mentorData = recycler_searchresults_data(
                                            profilePicUrl,
                                            name,
                                            occupation,
                                            status,
                                            price,R.drawable.heart_unfilled,0
                                        )
                                        adapter_data_list.add(mentorData)
                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
                                        recyclerView.adapter = adapter
                                        Toast.makeText(this@search_results_activity,"SUII3",Toast.LENGTH_SHORT).show()


                                        // Notify your adapter that the data has changed
                                        adapter.notifyDataSetChanged()

                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle error
                                }
                            })
                        }
                    }
//                    val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
//                    recyclerView.adapter = adapter
//
//
//                    // Notify your adapter that the data has changed
//                     adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Toast.makeText(this@search_results_activity,"Unable to Fetch Mentor Data",Toast.LENGTH_LONG).show()
                }
            })

        }



//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (mentorSnapshot in dataSnapshot.children) {
//                    val name = mentorSnapshot.child("name").getValue(String::class.java)
//                    val occupation = mentorSnapshot.child("occupation").getValue(String::class.java)
//                    val price = mentorSnapshot.child("price").getValue(String::class.java)
//                    val status = mentorSnapshot.child("status").getValue(String::class.java)
//                    val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)
//
//                    // Check if all required fields are present
//                    if (name != null && occupation != null && price != null && status != null && profilePicUrl != null) {
//                        // Load the profile picture using Glide
//
//                        Glide.with(this@search_results_activity)
//                            .load(profilePicUrl)
//                            .into(object : CustomTarget<Drawable>() {
//                                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                                    val mentorData = recycler_searchresults_data(
//                                        resource,
//                                        name,
//                                        occupation,
//                                        status,
//                                        price
//                                    )
//                                    adapter_data_list.add(mentorData)
//                                    // Notify your adapter that the data has changed
//                                    // adapter.notifyDataSetChanged()
//                                }
//
//                                override fun onLoadCleared(placeholder: Drawable?) {
//                                    // Optional: Handle case when the load is cleared
//                                }
//                            })
//                    }
//                }
//                val adapter = recycler_searchresults_adapter(adapter_data_list)
//                recyclerView.adapter = adapter
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//                Toast.makeText(this@search_results_activity,"Unable to Fetch Mentor Data",Toast.LENGTH_LONG).show()
//            }
//        })



//        if(input_txt!=null){
//            var filteredItems = mutableListOf<String>()
//            if (input_txt.isEmpty()) {
//                filteredItems.addAll(data_array)
//            } else {
//                filteredItems.clear()
//                for (item in data_array) {
//                    if (item.toInt()<=input_txt.toInt()) { // Directly check if the string contains the text
//                        filteredItems.add(item)
//                    }
////                if (item.contains(text, ignoreCase = true)) { // Directly check if the string contains the text
////                    filteredItems.add(item)
////                }
//                }
//            }
//            adapter.filter_list(filteredItems)
//
//        }

//        val v1  = recycler_searchresults_data(R.drawable.rectangle_blank,"Sample 1","Lead - Technology Officer","Available")
//        val v2  = recycler_searchresults_data(R.drawable.rectangle_blank,"Sample 2","Lead - Technology Officer"," Not Available")
//        val v3  = recycler_searchresults_data(R.drawable.rectangle_blank,"Sample 3","Lead - Technology Officer","Not Available")
//        val v4  = recycler_searchresults_data(R.drawable.rectangle_blank,"Sample 4","Lead - Technology Officer","Available")
//        val v5  = recycler_searchresults_data(R.drawable.rectangle_blank,"Sample 5","Lead - Technology Officer","Available")
//        val v6  = recycler_searchresults_data(R.drawable.rectangle_blank,"Sample 6","Lead - Technology Officer","Not Available")
//        val v7  = recycler_searchresults_data(R.drawable.rectangle_blank,"Sample 7","Lead - Technology Officer","Available")
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//        adapter_data_list.add(v4)
//        adapter_data_list.add(v5)
//        adapter_data_list.add(v6)
//        adapter_data_list.add(v7)
//
        // 3- Adapter
//        val adapter = recycler_searchresults_adapter(adapter_data_list)
//        recyclerView.adapter = adapter

    }
    override fun click_function(txt:String){
        val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
        nextActivityIntent.putExtra("user_name", txt)
        startActivity(nextActivityIntent)

    }

    override fun change_heart(flag:Int,txt:String) {
        val database = FirebaseDatabase.getInstance()
        var my_ref = database.getReference("users")

        val curr = mAuth.currentUser
        val id= curr?.uid.toString()

        if(flag==0){

            my_ref = database.reference.child("users").child(id)
            my_ref.child("favourite").child(txt).setValue("true")

        }
        else if (flag==1){

            val favouriteRef = database.reference.child("users").child(id).child("favourite")
            favouriteRef.child(txt).removeValue()

        }
    }
}