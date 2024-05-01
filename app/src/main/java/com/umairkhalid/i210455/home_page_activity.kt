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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
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
import kotlin.math.max

class home_page_activity : AppCompatActivity(), click_listner {
    @SuppressLint("MissingInflatedId")
    private var mAuth = FirebaseAuth.getInstance();
    lateinit var userID: String
    lateinit var url: String
    var mentorListTop = mutableListOf<mentorData>()
    var mentorListEdu = mutableListOf<mentorData>()
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

        setContentView(R.layout.home_page)
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
            ActivityResultContracts.RequestPermission(),
        )
        { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
                askNotificationPermission()
            }
        }

        val home_btn: ImageButton = findViewById(R.id.home_btn)
        val home_txt: TextView = findViewById(R.id.home_txt)
        val search_btn: ImageButton = findViewById(R.id.search_btn)
        val search_txt: TextView = findViewById(R.id.search_txt)
        val chat_btn: ImageButton = findViewById(R.id.chat_btn)
        val chat_txt: TextView = findViewById(R.id.chat_txt)
        val profile_btn: ImageButton = findViewById(R.id.profile_btn)
        val profile_txt: TextView = findViewById(R.id.profile_txt)
        val plus_btn: ImageButton = findViewById(R.id.plus_btn)
        val notifications_btn: ImageButton = findViewById(R.id.notifications_btn)
        val username: TextView = findViewById(R.id.txt_username)


        if (userID != null) {
            val tempUrl =
                "${url}getusername.php" // Assuming the PHP file to retrieve username is named get_username.php

            val stringRequest = object : StringRequest(
                com.android.volley.Request.Method.POST, tempUrl,
                com.android.volley.Response.Listener { response ->
                    // Handle successful response
                    if (response != "User not found") {
                        // Username is retrieved successfully
                        val user_name = response
                        username.text = user_name.toString()
                        // Use the username as needed
                    } else {
                        // User not found with the provided user ID
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    }
                },
                com.android.volley.Response.ErrorListener { error ->
                    // Handle error
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                    Log.e("API Error", "Error occurred: ${error.message}")
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["userID"] = userID
                    return params
                }
            }

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(stringRequest)
        } else {
            // userID is null, handle accordingly
            Toast.makeText(this, "Invalid userID", Toast.LENGTH_SHORT).show()
        }


        home_btn.setOnClickListener {
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
            finish()
        }

        home_txt.setOnClickListener {
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
            finish()
        }

        search_btn.setOnClickListener {
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        search_txt.setOnClickListener {
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        chat_btn.setOnClickListener {
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        chat_txt.setOnClickListener {
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        profile_btn.setOnClickListener {
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        profile_txt.setOnClickListener {
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener {
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener {
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            startActivity(nextActivityIntent)
        }

        notifications_btn.setOnClickListener {
            val nextActivityIntent = Intent(this, notifications_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        getFavouriteMentor(userID) {

            getTopMentors() {
                // 1- AdapterView: RecyclerView
                val recyclerView_top: RecyclerView = findViewById(R.id.recyclerview_educator_top)
                recyclerView_top.layoutManager = LinearLayoutManager(
                    this,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

                var adapter_data_list_top: ArrayList<recycler_educator_data> = ArrayList()
                for (mentor in mentorListTop) {
                    if (mentor.mentorID in favList) {
                        val mentorData = recycler_educator_data(
                            mentor.mentorID,
                            mentor.profileImg,
                            mentor.name,
                            mentor.occupation,
                            mentor.status,
                            mentor.price, R.drawable.red_heart_btn, 1
                        )
                        adapter_data_list_top.add(mentorData)

                    } else {

                        val mentorData = recycler_educator_data(
                            mentor.mentorID,
                            mentor.profileImg,
                            mentor.name,
                            mentor.occupation,
                            mentor.status,
                            mentor.price, R.drawable.heart_unfilled, 0
                        )
                        adapter_data_list_top.add(mentorData)
                    }

                }

                val adapter_top = recycler_educator_adapter(adapter_data_list_top, this)
                recyclerView_top.adapter = adapter_top

            }


            getEduMentors() {
                // 1- AdapterView: RecyclerView
                val recyclerView_edu: RecyclerView = findViewById(R.id.recyclerview_educator_edu)
                recyclerView_edu.layoutManager = LinearLayoutManager(
                    this,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

                // 2- Data Source: List of  Objects
                var adapter_data_list_edu: ArrayList<recycler_educator_data> = ArrayList()
                for (mentor in mentorListEdu) {
                    if (mentor.mentorID in favList) {
                        val mentorData = recycler_educator_data(
                            mentor.mentorID,
                            mentor.profileImg,
                            mentor.name,
                            mentor.occupation,
                            mentor.status,
                            mentor.price, R.drawable.red_heart_btn, 1
                        )
                        adapter_data_list_edu.add(mentorData)

                    } else {

                        val mentorData = recycler_educator_data(
                            mentor.mentorID,
                            mentor.profileImg,
                            mentor.name,
                            mentor.occupation,
                            mentor.status,
                            mentor.price, R.drawable.heart_unfilled, 0
                        )
                        adapter_data_list_edu.add(mentorData)
                    }

                }
                val adapter_edu = recycler_educator_adapter(adapter_data_list_edu, this)
                recyclerView_edu.adapter = adapter_edu

            }
        }

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

        val tempURL = "${url}getfirst4mentors.php" // Replace "your_domain" with your actual domain
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

    private fun getEduMentors(callback: () -> Unit) {
        val tempURL = "${url}getlast4mentors.php" // Replace "your_domain" with your actual domain
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
                    mentorListEdu.add(mentor)
                }
                callback()
            },
            com.android.volley.Response.ErrorListener { error ->
                Log.e("Error", "Error fetching mentors: ${error.message}")
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    override fun click_function(txt: String, mentorID: String) {
        val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
        nextActivityIntent.putExtra("mentorID", mentorID)
        nextActivityIntent.putExtra("userID", userID)
        nextActivityIntent.putExtra("user_name", txt)
        startActivity(nextActivityIntent)

    }

    override fun change_heart(flag: Int, txt: String, mentorID: String) {

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

    fun sendPushNotification(
        token: String,
        title: String,
        subtitle: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ) {
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

        var key =
            "AAAAhfqz-ls:APA91bEQFjo8C3YLtR6V0AsR6m52hVMniNYzBC8GTWMkU6gyx8YzP5wPxFhieeyQPYcoQFmPEx0bXmMumzk3cYj3jiQ4uMm-NvlP5YOYeabErgHvFqF5Rwac8NwLeg3_005xdofYV0l6"
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