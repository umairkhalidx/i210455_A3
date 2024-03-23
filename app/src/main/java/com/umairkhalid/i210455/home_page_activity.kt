package com.umairkhalid.i210455

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import kotlin.math.max

class home_page_activity : AppCompatActivity() , click_listner {
    @SuppressLint("MissingInflatedId")
    private var  mAuth = FirebaseAuth.getInstance();

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

        setContentView(R.layout.home_page)

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
        val notifications_btn: ImageButton =findViewById(R.id.notifications_btn)
        val username : TextView=findViewById(R.id.txt_username)

        if (!isNetworkAvailable()) {
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show()

            val sharedPref_1 = getSharedPreferences("home_page_prefs", Context.MODE_PRIVATE)
            val storedUsername = sharedPref_1.getString("username", "")
            username.text=storedUsername

            // Initialize SharedPreferences
            val sharedPref = getSharedPreferences("home_adapter_top_prefs", Context.MODE_PRIVATE)
            val json = sharedPref.getString("home_adapter_top", "")

            val gson = Gson()
            val type = object : TypeToken<ArrayList<recycler_educator_data>>() {}.type
            val adapter_data_list_top: ArrayList<recycler_educator_data> = gson.fromJson(json, type)

            // 1- AdapterView: RecyclerView
            val recyclerView_top : RecyclerView = findViewById(R.id.recyclerview_educator_top)
            recyclerView_top.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            val adapter_top = recycler_educator_adapter(adapter_data_list_top,this@home_page_activity)
            recyclerView_top.adapter = adapter_top

            // Notify your adapter that the data has changed
            adapter_top.notifyDataSetChanged()


            val recyclerView_edu : RecyclerView = findViewById(R.id.recyclerview_educator_edu)
            recyclerView_edu.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            // Initialize SharedPreferences
            val sharedPref_2 = getSharedPreferences("home_adapter_edu_prefs", Context.MODE_PRIVATE)
            val json_1 = sharedPref_2.getString("home_adapter_edu", "")

            val gson_1 = Gson()
            val type_1 = object : TypeToken<ArrayList<recycler_educator_data>>() {}.type
            val adapter_data_list_edu: ArrayList<recycler_educator_data> = gson_1.fromJson(json_1, type_1)


            val lastFourMentors = adapter_data_list_edu.takeLast(4)
            val adapter_edu = recycler_educator_adapter(ArrayList(lastFourMentors),this@home_page_activity)
            recyclerView_edu.adapter = adapter_edu

            // Notify your adapter that the data has changed
            adapter_edu.notifyDataSetChanged()

        }

        var database = FirebaseDatabase.getInstance()
        val my_ref = database.getReference("users")
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if(userId!=null){

            my_ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val name = dataSnapshot.child(userId).child("name").value.toString()
                    username.text = name
                    val sharedPref = getSharedPreferences("home_page_prefs", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("username", name)
                    editor.apply()
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.d("TAG", "Unable to retrieve Data")

                }
            })
        }


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

        notifications_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, notifications_activity::class.java)
            startActivity(nextActivityIntent)
        }

        // 1- AdapterView: RecyclerView
        val recyclerView_top : RecyclerView = findViewById(R.id.recyclerview_educator_top)
        recyclerView_top.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false
        )


//         2- Data Source: List of  Objects
        var adapter_data_list_top : ArrayList<recycler_educator_data> = ArrayList()

        database = FirebaseDatabase.getInstance()
        val mentorsRef = database.getReference("mentors")

        val query = mentorsRef.limitToFirst(4) // Limit the query to the first 4 mentors

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
                                                    val mentorData = recycler_educator_data(
                                                        profilePicUrl,
                                                        name,
                                                        occupation,
                                                        status,
                                                        price,R.drawable.red_heart_btn,1
                                                    )
                                                    adapter_data_list_top.add(mentorData)
                                                    val adapter_top = recycler_educator_adapter(adapter_data_list_top,this@home_page_activity)
                                                    recyclerView_top.adapter = adapter_top

                                                    // Notify your adapter that the data has changed
                                                    adapter_top.notifyDataSetChanged()

                                                    val gson = Gson()
                                                    val json = gson.toJson(adapter_data_list_top)
                                                    val sharedPref = getSharedPreferences("home_adapter_top_prefs", Context.MODE_PRIVATE)
                                                    val editor = sharedPref.edit()
                                                    editor.putString("home_adapter_top", json)
                                                    editor.apply()
                                                } else {
                                                    val mentorData = recycler_educator_data(
                                                        profilePicUrl,
                                                        name,
                                                        occupation,
                                                        status,
                                                        price,R.drawable.heart_unfilled,0
                                                    )
                                                    adapter_data_list_top.add(mentorData)
                                                    val adapter_top = recycler_educator_adapter(adapter_data_list_top,this@home_page_activity)
                                                    recyclerView_top.adapter = adapter_top

                                                    // Notify your adapter that the data has changed
                                                    adapter_top.notifyDataSetChanged()

                                                    val gson = Gson()
                                                    val json = gson.toJson(adapter_data_list_top)
                                                    val sharedPref = getSharedPreferences("home_adapter_top_prefs", Context.MODE_PRIVATE)
                                                    val editor = sharedPref.edit()
                                                    editor.putString("home_adapter_top", json)
                                                    editor.apply()
                                                }
                                            }
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            // Handle error
                                        }
                                    })
                                } else {

                                    val mentorData = recycler_educator_data(
                                        profilePicUrl,
                                        name,
                                        occupation,
                                        status,
                                        price,R.drawable.heart_unfilled,0
                                    )
                                    adapter_data_list_top.add(mentorData)
                                    val adapter_top = recycler_educator_adapter(adapter_data_list_top,this@home_page_activity)
                                    recyclerView_top.adapter = adapter_top

                                    // Notify your adapter that the data has changed
                                    adapter_top.notifyDataSetChanged()

                                    val gson = Gson()
                                    val json = gson.toJson(adapter_data_list_top)
                                    val sharedPref = getSharedPreferences("home_adapter_top_prefs", Context.MODE_PRIVATE)
                                    val editor = sharedPref.edit()
                                    editor.putString("home_adapter_top", json)
                                    editor.apply()

                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle error
                            }
                        })
                    }
                }
//                val adapter_top = recycler_educator_adapter(adapter_data_list_top,this@home_page_activity)
//                recyclerView_top.adapter = adapter_top
//
//                // Notify your adapter that the data has changed
//                adapter_top.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@home_page_activity, "Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
            }
        })

//        val v1  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 1","Lead - Technology Officer","Available")
//        val v2  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 2","Lead - Technology Officer"," Not Available")
//        val v3  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 3","Lead - Technology Officer","Not Available")
//        val v4  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 4","Lead - Technology Officer","Available")
//        val v5  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 5","Lead - Technology Officer","Available")
//        val v6  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 6","Lead - Technology Officer","Not Available")
//        val v7  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 7","Lead - Technology Officer","Available")
//
//        adapter_data_list_top.add(v1)
//        adapter_data_list_top.add(v2)
//        adapter_data_list_top.add(v3)
//        adapter_data_list_top.add(v4)
//        adapter_data_list_top.add(v5)
//        adapter_data_list_top.add(v6)
//        adapter_data_list_top.add(v7)
//
//        // 3- Adapter
//        val adapter_top = recycler_educator_adapter(adapter_data_list_top)
//        recyclerView_top.adapter = adapter_top




        // 1- AdapterView: RecyclerView
        val recyclerView_edu : RecyclerView = findViewById(R.id.recyclerview_educator_edu)
        recyclerView_edu.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        // 2- Data Source: List of  Objects
        var adapter_data_list_edu : ArrayList<recycler_educator_data> = ArrayList()

        database = FirebaseDatabase.getInstance()
        val mentorsRef_2 = database.getReference("mentors")

        mentorsRef_2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val totalMentors = dataSnapshot.childrenCount.toInt()

                // Calculate the starting index to retrieve the last four mentors
                val startIndex = max(0, totalMentors - 4)

                // Now, construct the query to retrieve the last four mentors
                val query_2 = mentorsRef_2.orderByKey().startAt(startIndex.toString())

                query_2.addListenerForSingleValueEvent(object : ValueEventListener {
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
                                                            val mentorData = recycler_educator_data(
                                                                profilePicUrl,
                                                                name,
                                                                occupation,
                                                                status,
                                                                price,R.drawable.red_heart_btn,1
                                                            )
                                                            adapter_data_list_edu.add(mentorData)
                                                            val lastFourMentors = adapter_data_list_edu.takeLast(4)
                                                            val adapter_edu = recycler_educator_adapter(ArrayList(lastFourMentors),this@home_page_activity)
                                                            recyclerView_edu.adapter = adapter_edu

                                                            // Notify your adapter that the data has changed
                                                            adapter_edu.notifyDataSetChanged()

                                                            val gson = Gson()
                                                            val json = gson.toJson(adapter_data_list_edu)
                                                            val sharedPref = getSharedPreferences("home_adapter_edu_prefs", Context.MODE_PRIVATE)
                                                            val editor = sharedPref.edit()
                                                            editor.putString("home_adapter_edu", json)
                                                            editor.apply()

                                                        } else {
                                                            val mentorData = recycler_educator_data(
                                                                profilePicUrl,
                                                                name,
                                                                occupation,
                                                                status,
                                                                price,R.drawable.heart_unfilled,0
                                                            )
                                                            adapter_data_list_edu.add(mentorData)
                                                            val lastFourMentors = adapter_data_list_edu.takeLast(4)
                                                            val adapter_edu = recycler_educator_adapter(ArrayList(lastFourMentors),this@home_page_activity)
                                                            recyclerView_edu.adapter = adapter_edu

                                                            // Notify your adapter that the data has changed
                                                            adapter_edu.notifyDataSetChanged()

                                                            val gson = Gson()
                                                            val json = gson.toJson(adapter_data_list_edu)
                                                            val sharedPref = getSharedPreferences("home_adapter_edu_prefs", Context.MODE_PRIVATE)
                                                            val editor = sharedPref.edit()
                                                            editor.putString("home_adapter_edu", json)
                                                            editor.apply()

                                                        }
                                                    }
                                                }

                                                override fun onCancelled(databaseError: DatabaseError) {
                                                    // Handle error
                                                }
                                            })
                                        } else {

                                            val mentorData = recycler_educator_data(
                                                profilePicUrl,
                                                name,
                                                occupation,
                                                status,
                                                price,R.drawable.heart_unfilled,0
                                            )
                                            adapter_data_list_edu.add(mentorData)
                                            val lastFourMentors = adapter_data_list_edu.takeLast(4)
                                            val adapter_edu = recycler_educator_adapter(ArrayList(lastFourMentors),this@home_page_activity)
                                            recyclerView_edu.adapter = adapter_edu

                                            // Notify your adapter that the data has changed
                                            adapter_edu.notifyDataSetChanged()

                                            val gson = Gson()
                                            val json = gson.toJson(adapter_data_list_edu)
                                            val sharedPref = getSharedPreferences("home_adapter_edu_prefs", Context.MODE_PRIVATE)
                                            val editor = sharedPref.edit()
                                            editor.putString("home_adapter_edu", json)
                                            editor.apply()

                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle error
                                    }
                                })
                            }
                        }
//                        val lastFourMentors = adapter_data_list_edu.takeLast(4)
//                        val adapter_edu = recycler_educator_adapter(ArrayList(lastFourMentors),this@home_page_activity)
//                        recyclerView_edu.adapter = adapter_edu
//
//                        // Notify your adapter that the data has changed
//                        adapter_edu.notifyDataSetChanged()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                        Toast.makeText(this@home_page_activity, "Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@home_page_activity, "Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
            }
        })

//        val v8  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 1","Lead - Technology Officer","Available")
//        val v9  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 2","Lead - Technology Officer"," Not Available")
//        val v10  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 3","Lead - Technology Officer","Not Available")
//        val v11  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 4","Lead - Technology Officer","Available")
//        val v12  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 5","Lead - Technology Officer","Available")
//        val v13  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 6","Lead - Technology Officer","Not Available")
//        val v14  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 7","Lead - Technology Officer","Available")
//
//        adapter_data_list_edu.add(v8)
//        adapter_data_list_edu.add(v9)
//        adapter_data_list_edu.add(v10)
//        adapter_data_list_edu.add(v11)
//        adapter_data_list_edu.add(v12)
//        adapter_data_list_edu.add(v13)
//        adapter_data_list_edu.add(v14)
//
//        // 3- Adapter
//        val adapter_edu = recycler_educator_adapter(adapter_data_list_edu)
//        recyclerView_edu.adapter = adapter_edu



        // 1- AdapterView: RecyclerView
//        val recyclerView_rec : RecyclerView = findViewById(R.id.recyclerview_educator_rec)
//        recyclerView_rec.layoutManager = LinearLayoutManager(this,
//            LinearLayoutManager.HORIZONTAL,
//            false
//        )

//
//        // 2- Data Source: List of  Objects
//        var adapter_data_list_rec : ArrayList<recycler_educator_data> = ArrayList()
//
//        val v15  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 1","Lead - Technology Officer","Available")
//        val v16  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 2","Lead - Technology Officer"," Not Available")
//        val v17  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 3","Lead - Technology Officer","Not Available")
//        val v18  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 4","Lead - Technology Officer","Available")
//        val v19  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 5","Lead - Technology Officer","Available")
//        val v20  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 6","Lead - Technology Officer","Not Available")
//        val v21  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 7","Lead - Technology Officer","Available")
//
//        adapter_data_list_rec.add(v15)
//        adapter_data_list_rec.add(v9)
//        adapter_data_list_rec.add(v10)
//        adapter_data_list_rec.add(v11)
//        adapter_data_list_rec.add(v12)
//        adapter_data_list_rec.add(v13)
//        adapter_data_list_rec.add(v14)
//
//        // 3- Adapter
//        val adapter_rec = recycler_educator_adapter(adapter_data_list_rec)
//        recyclerView_rec.adapter = adapter_rec



    }
    override fun click_function(txt:String){
        val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
        nextActivityIntent.putExtra("user_name", txt)
        startActivity(nextActivityIntent)

    }

    override fun change_heart(flag:Int,txt:String){
        val database = FirebaseDatabase.getInstance()
        var my_ref = database.getReference("users")

        val curr = mAuth.currentUser
        val id= curr?.uid.toString()

        if(flag==0){

            my_ref = database.reference.child("users").child(id)
            my_ref.child("favourite").child(txt).setValue("true")

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

        }
        else if (flag==1){

            val favouriteRef = database.reference.child("users").child(id).child("favourite")
            favouriteRef.child(txt).removeValue()

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

        }

    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
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