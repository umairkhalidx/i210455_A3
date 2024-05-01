package com.umairkhalid.i210455

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class search_results_activity : AppCompatActivity(), click_listner {
    @SuppressLint("MissingInflatedId")
    private var  mAuth = FirebaseAuth.getInstance();
    lateinit var userID: String
    lateinit var url: String
    var mentorListTop = mutableListOf<mentorData>()
    var mentorList = mutableListOf<mentorData>()
    val favList = ArrayList<String>()


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, you can proceed with sending notifications
        } else {
            // Permission is not granted, handle accordingly
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.search_results)

        userID = intent.getStringExtra("userID").toString()
        url = getString(R.string.url)


        FirebaseApp.initializeApp(this)
        //firebase token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("MyToken", token)
        })

        askNotificationPermission()

        var requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),)
        { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
                askNotificationPermission()
            }
        }

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
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
            finish()
        }

        home_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
            finish()
        }

        search_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        search_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        chat_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        chat_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        profile_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        profile_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        back_btn_search.setOnClickListener{
//            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }

        val input_txt = intent.getStringExtra("search_txt")

        // 1- AdapterView: RecyclerView
        val recyclerView : RecyclerView = findViewById(R.id.recycleview_searched_results)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        // 2- Data Source: List of  Objects
        var adapter_data_list : ArrayList<recycler_searchresults_data> = ArrayList()


        if(input_txt=="Entrepreneurship" || input_txt=="Personal" || input_txt=="Education" ){

            getFavouriteMentor(userID) {
                getTopMentors() {
                    for (mentor in mentorListTop) {
                        if (mentor.mentorID in favList) {
                            val mentorData = recycler_searchresults_data(
                                mentor.mentorID,
                                mentor.profileImg,
                                mentor.name,
                                mentor.occupation,
                                mentor.status,
                                mentor.price, R.drawable.red_heart_btn, 1
                            )
                            adapter_data_list.add(mentorData)

                        } else {

                            val mentorData = recycler_searchresults_data(
                                mentor.mentorID,
                                mentor.profileImg,
                                mentor.name,
                                mentor.occupation,
                                mentor.status,
                                mentor.price,R.drawable.heart_unfilled,0
                            )
                            adapter_data_list.add(mentorData)
                        }

                    }
                    val adapter = recycler_searchresults_adapter(adapter_data_list,this)
                    recyclerView.adapter = adapter


                }

            }

        }else{
            val mentorName=input_txt.toString().trim()

            getFavouriteMentor(userID) {
                getMentors(mentorName) {
                    for (mentor in mentorList) {
                        if (mentor.mentorID in favList) {
                            val mentorData = recycler_searchresults_data(
                                mentor.mentorID,
                                mentor.profileImg,
                                mentor.name,
                                mentor.occupation,
                                mentor.status,
                                mentor.price, R.drawable.red_heart_btn, 1
                            )
                            adapter_data_list.add(mentorData)

                        } else {

                            val mentorData = recycler_searchresults_data(
                                mentor.mentorID,
                                mentor.profileImg,
                                mentor.name,
                                mentor.occupation,
                                mentor.status,
                                mentor.price,R.drawable.heart_unfilled,0
                            )
                            adapter_data_list.add(mentorData)
                        }

                    }
                    val adapter = recycler_searchresults_adapter(adapter_data_list,this)
                    recyclerView.adapter = adapter
                }

            }

        }



//
//        if(input_txt=="Entrepreneurship" || input_txt=="Personal" || input_txt=="Education" ){
//            val database = FirebaseDatabase.getInstance()
//            val mentorsRef = database.getReference("mentors")
//
//            val query = mentorsRef.limitToFirst(7) // Limit the query to the first 4 mentors
//
//            query.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                    for (mentorSnapshot in dataSnapshot.children) {
//                        val name = mentorSnapshot.child("name").getValue(String::class.java)
//                        val occupation = mentorSnapshot.child("occupation").getValue(String::class.java)
//                        val price = mentorSnapshot.child("price").getValue(String::class.java)
//                        val status = mentorSnapshot.child("status").getValue(String::class.java)
//                        val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)
//
//                        // Check if all required fields are present
//                        if (name != null && occupation != null && price != null && status != null) {
//                            val curr_usr = mAuth.currentUser
//                            val user_id = curr_usr?.uid.toString()
//                            val userRef = database.getReference("users").child(user_id)
//
//                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                    if (dataSnapshot.hasChild("favourite")) {
//                                        val fav_ref = userRef.child("favourite")
//                                        fav_ref.addListenerForSingleValueEvent(object : ValueEventListener {
//                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                                if (dataSnapshot.exists()) {
//                                                    if (dataSnapshot.hasChild(name)) {
//                                                        val mentorData = recycler_searchresults_data(
//                                                            profilePicUrl,
//                                                            name,
//                                                            occupation,
//                                                            status,
//                                                            price,R.drawable.red_heart_btn,1
//                                                        )
//                                                        adapter_data_list.add(mentorData)
//                                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
//                                                        recyclerView.adapter = adapter
//
//                                                        // Notify your adapter that the data has changed
//                                                        adapter.notifyDataSetChanged()
//
//                                                    } else {
//                                                        val mentorData = recycler_searchresults_data(
//                                                            profilePicUrl,
//                                                            name,
//                                                            occupation,
//                                                            status,
//                                                            price,R.drawable.heart_unfilled,0
//                                                        )
//                                                        adapter_data_list.add(mentorData)
//                                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
//                                                        recyclerView.adapter = adapter
//
//                                                        // Notify your adapter that the data has changed
//                                                        adapter.notifyDataSetChanged()
//
//                                                    }
//                                                }
//                                            }
//
//                                            override fun onCancelled(databaseError: DatabaseError) {
//                                                // Handle error
//                                            }
//                                        })
//                                    } else {
//
//                                        val mentorData = recycler_searchresults_data(
//                                            profilePicUrl,
//                                            name,
//                                            occupation,
//                                            status,
//                                            price,R.drawable.heart_unfilled,0
//                                        )
//                                        adapter_data_list.add(mentorData)
//                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
//                                        recyclerView.adapter = adapter
//
//                                        // Notify your adapter that the data has changed
//                                        adapter.notifyDataSetChanged()
//
//                                    }
//                                }
//
//                                override fun onCancelled(databaseError: DatabaseError) {
//                                    // Handle error
//                                }
//                            })
//                        }
//                    }
//
////                    val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
////                    recyclerView.adapter = adapter
////
////                    // Notify your adapter that the data has changed
////                    adapter.notifyDataSetChanged()
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // Handle error
//                    Toast.makeText(this@search_results_activity, "Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
//                }
//            })
//        }
//        else{
//
//            val database = FirebaseDatabase.getInstance()
//            val mentorsRef = database.getReference("mentors")
//
//            val query = mentorsRef.orderByChild("name").equalTo(input_txt.toString())
//
//            query.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    for (mentorSnapshot in dataSnapshot.children) {
//                        val name = mentorSnapshot.child("name").getValue(String::class.java)
//                        val occupation = mentorSnapshot.child("occupation").getValue(String::class.java)
//                        val price = mentorSnapshot.child("price").getValue(String::class.java)
//                        val status = mentorSnapshot.child("status").getValue(String::class.java)
//                        val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)
//
//
//                        // Check if all required fields are present
//                        if (name != null && occupation != null && price != null && status != null) {
//                            val curr_usr = mAuth.currentUser
//                            val user_id = curr_usr?.uid.toString()
//                            val userRef = database.getReference("users").child(user_id)
//
//                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                    if (dataSnapshot.hasChild("favourite")) {
//                                        val fav_ref = userRef.child("favourite")
//                                        fav_ref.addListenerForSingleValueEvent(object : ValueEventListener {
//                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                                if (dataSnapshot.exists()) {
//                                                    if (dataSnapshot.hasChild(name)) {
//                                                        val mentorData = recycler_searchresults_data(
//                                                            profilePicUrl,
//                                                            name,
//                                                            occupation,
//                                                            status,
//                                                            price,R.drawable.red_heart_btn,1
//                                                        )
//                                                        adapter_data_list.add(mentorData)
//                                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
//                                                        recyclerView.adapter = adapter
//                                                        Toast.makeText(this@search_results_activity,"SUII1",Toast.LENGTH_SHORT).show()
//
//
//                                                        // Notify your adapter that the data has changed
//                                                        adapter.notifyDataSetChanged()
//
//                                                    } else {
//                                                        val mentorData = recycler_searchresults_data(
//                                                            profilePicUrl,
//                                                            name,
//                                                            occupation,
//                                                            status,
//                                                            price,R.drawable.heart_unfilled,0
//                                                        )
//                                                        adapter_data_list.add(mentorData)
//                                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
//                                                        recyclerView.adapter = adapter
//
//                                                        Toast.makeText(this@search_results_activity,"SUII2",Toast.LENGTH_SHORT).show()
//
//                                                        // Notify your adapter that the data has changed
//                                                        adapter.notifyDataSetChanged()
//
//                                                    }
//                                                }
//                                            }
//
//                                            override fun onCancelled(databaseError: DatabaseError) {
//                                                // Handle error
//                                            }
//                                        })
//                                    } else {
//
//                                        val mentorData = recycler_searchresults_data(
//                                            profilePicUrl,
//                                            name,
//                                            occupation,
//                                            status,
//                                            price,R.drawable.heart_unfilled,0
//                                        )
//                                        adapter_data_list.add(mentorData)
//                                        val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
//                                        recyclerView.adapter = adapter
//                                        Toast.makeText(this@search_results_activity,"SUII3",Toast.LENGTH_SHORT).show()
//
//
//                                        // Notify your adapter that the data has changed
//                                        adapter.notifyDataSetChanged()
//
//                                    }
//                                }
//
//                                override fun onCancelled(databaseError: DatabaseError) {
//                                    // Handle error
//                                }
//                            })
//                        }
//                    }
////                    val adapter = recycler_searchresults_adapter(adapter_data_list,this@search_results_activity)
////                    recyclerView.adapter = adapter
////
////
////                    // Notify your adapter that the data has changed
////                     adapter.notifyDataSetChanged()
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // Handle error
//                    Toast.makeText(this@search_results_activity,"Unable to Fetch Mentor Data",Toast.LENGTH_LONG).show()
//                }
//            })
//
//        }
//



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

    fun getMentors(mentorName: String, callback: () -> Unit) {
        val tempURL = "${url}getmentordata.php"

        // Create a StringRequest to send a POST request
        val request = object : StringRequest(
            com.android.volley.Request.Method.POST, tempURL,
            com.android.volley.Response.Listener { response ->
                // Handle response
                try {
                    val jsonArray = JSONArray(response)

                    // Parse mentor data from JSON response
                    for (i in 0 until jsonArray.length()) {
                        val mentorObject = jsonArray.getJSONObject(i)
                        val mentor = mentorData(
                            mentorObject.getString("mentorID"),
                            mentorObject.getString("name"),
                            mentorObject.getString("occupation"),
                            mentorObject.getString("description"),
                            mentorObject.getString("price"),
                            mentorObject.getString("profileImg"),
                            mentorObject.getString("status"),
                            mentorObject.getString("favourite")
                        )
                        mentorList.add(mentor)
                    }

                    // Invoke the callback with mentor data
                    callback()
                } catch (e: JSONException) {
                    // Handle JSON parsing error
                    Log.e("JSON Parse Error", e.message ?: "Unknown error")
                }
            },
            com.android.volley.Response.ErrorListener { error ->
                // Handle error
                Log.e("Error", "Error occurred: ${error.message}")
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["mentorName"] = mentorName
                return params
            }
        }

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request)
    }

    private fun getFavouriteMentor(userID: String, callback: () -> Unit) {
        val tempURL = "${url}getfavourite.php"

        // Create a StringRequest to send a POST request
        val request = object : StringRequest(com.android.volley.Request.Method.POST, tempURL,
            com.android.volley.Response.Listener { response ->
                // Handle response
                Log.d("Response", response)

                // Parse JSON response
                val jsonArray = JSONArray(response)

                for (i in 0 until jsonArray.length()) {
                    val mentorID = jsonArray.getString(i)
                    favList.add(mentorID)
                }
                callback()
            },
            com.android.volley.Response.ErrorListener { error ->
                // Handle error
                Log.e("Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): Map<String, String> {
                // Set POST parameters
                val params = HashMap<String, String>()
                params["userID"] = userID
                return params
            }
        }
        Volley.newRequestQueue(this).add(request)
    }


    private fun getTopMentors(callback: () -> Unit) {

        val tempURL = "${url}getfirst7mentors.php" // Replace "your_domain" with your actual domain
        val request = JsonArrayRequest(
            com.android.volley.Request.Method.GET,
            tempURL,
            null,
            com.android.volley.Response.Listener { response ->

                for (i in 0 until response.length()) {
                    val mentorObject = response.getJSONObject(i)
                    val mentor = mentorData(
                        mentorObject.getString("mentorID"),
                        mentorObject.getString("name"),
                        mentorObject.getString("occupation"),
                        mentorObject.getString("description"),
                        mentorObject.getString("price"),
                        mentorObject.getString("profileImg"),
                        mentorObject.getString("status"),
                        mentorObject.getString("favourite")
                    )
                    mentorListTop.add(mentor)
                }
                callback()
            },
            com.android.volley.Response.ErrorListener { error ->
                Log.e("Error", "Error fetching mentors: ${error.message}")
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    override fun click_function(txt:String,mentorID: String){
        val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
        nextActivityIntent.putExtra("mentorID", mentorID)
        nextActivityIntent.putExtra("userID", userID)
        nextActivityIntent.putExtra("user_name", txt)
        startActivity(nextActivityIntent)

    }

    override fun change_heart(flag:Int,txt:String,mentorID:String) {

        if (flag == 0) {

            val tempURL = "${url}setfavourite.php"

            // Create a StringRequest to send a POST request
            val request = object : StringRequest(com.android.volley.Request.Method.POST, tempURL,
                com.android.volley.Response.Listener { response ->

                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            return@addOnCompleteListener
                        }
                        val token = task.result
                        sendPushNotification(
                            token,
                            "MentorMe",
                            "Subtitle: Class",
                            "$txt Added As Favourite",
                            mapOf("key1" to "value1", "key2" to "value2")
                        )

                    }
                },
                com.android.volley.Response.ErrorListener { error ->
                    // Handle error
                    Log.e("Error", "Error occurred: ${error.message}")
                }) {
                override fun getParams(): Map<String, String> {
                    // Set POST parameters
                    val params = HashMap<String, String>()
                    params["userID"] = userID
                    params["mentorID"] = mentorID
                    return params
                }
            }

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(request)

        } else if (flag == 1) {

            val tempURL = "${url}removefavourite.php"

            // Create a StringRequest to send a POST request
            val request = object : StringRequest(com.android.volley.Request.Method.POST, tempURL,
                com.android.volley.Response.Listener { response ->

                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            return@addOnCompleteListener
                        }
                        val token = task.result
                        sendPushNotification(
                            token,
                            "MentorMe",
                            "Subtitle: Class",
                            "$txt Removed From Favourite",
                            mapOf("key1" to "value1", "key2" to "value2")
                        )

                    }
                },
                com.android.volley.Response.ErrorListener { error ->
                    // Handle error
                    Log.e("Error", "Error occurred: ${error.message}")
                }) {
                override fun getParams(): Map<String, String> {
                    // Set POST parameters
                    val params = HashMap<String, String>()
                    params["userID"] = userID
                    params["mentorID"] = mentorID
                    return params
                }
            }

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(request)

        }

//        val database = FirebaseDatabase.getInstance()
//        var my_ref = database.getReference("users")
//
//        val curr = mAuth.currentUser
//        val id= curr?.uid.toString()
//
//        if(flag==0){
//
//            my_ref = database.reference.child("users").child(id)
//            my_ref.child("favourite").child(txt).setValue("true")
//
//            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    return@addOnCompleteListener
//                }
//                val token = task.result
//                sendPushNotification(
//                    token,
//                    "MentorMe",
//                    "Subtitle: Class",
//                    "$txt Added As Favourite",
//                    mapOf("key1" to "value1", "key2" to "value2")
//                )
//
//            }
//
//        }
//        else if (flag==1){
//
//            val favouriteRef = database.reference.child("users").child(id).child("favourite")
//            favouriteRef.child(txt).removeValue()
//
//            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    return@addOnCompleteListener
//                }
//                val token = task.result
//                sendPushNotification(
//                    token,
//                    "MentorMe",
//                    "Subtitle: Class",
//                    "$txt Removed From Favourite",
//                    mapOf("key1" to "value1", "key2" to "value2")
//                )
//
//            }
//
//        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun sendPushNotification(token: String, title: String, subtitle: String, body: String, data: Map<String, String> = emptyMap()) {
        val url = "https://fcm.googleapis.com/fcm/send"
        val bodyJson = JSONObject()
        bodyJson.put("to", token)
        bodyJson.put("notification",
            JSONObject().also {
                it.put("title", title)
                it.put("subtitle", subtitle)
                it.put("body", body)
                it.put("sound", "social_notification_sound.wav")
            }
        )
        Log.d("TAG", "sendPushNotification: ${JSONObject(data)}")
        if (data.isNotEmpty()) {
            bodyJson.put("data", JSONObject(data))
        }

        var key="AAAAhfqz-ls:APA91bEQFjo8C3YLtR6V0AsR6m52hVMniNYzBC8GTWMkU6gyx8YzP5wPxFhieeyQPYcoQFmPEx0bXmMumzk3cYj3jiQ4uMm-NvlP5YOYeabErgHvFqF5Rwac8NwLeg3_005xdofYV0l6"
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=$key")
            .post(
                bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            )
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(
            object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    println("Received data: ${response.body?.string()}")
                    Log.d("TAG", "onResponse: ${response}   ")
                    Log.d("TAG", "onResponse Message: ${response.message}   ")
                }

                override fun onFailure(call: Call, e: IOException) {
                    println(e.message.toString())
                    Log.d("TAG", "onFailure: ${e.message.toString()}")
                }
            }
        )
    }
}