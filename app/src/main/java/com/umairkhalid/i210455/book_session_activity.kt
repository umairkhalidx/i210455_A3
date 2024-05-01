package com.umairkhalid.i210455

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.annotation.SuppressLint
import android.app.Notification
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException


class book_session_activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private var  mAuth = FirebaseAuth.getInstance();
    lateinit var userID: String
    lateinit var url: String
    lateinit var mentorID: String
    lateinit var Mentor: mentorData

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

        setContentView(R.layout.book_your_session)
        userID = intent.getStringExtra("userID").toString()
        mentorID = intent.getStringExtra("mentorID").toString()
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
            selected_time=time_btn_2.text.toString()
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
            selected_time=time_btn_3.text.toString()
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

        Mentor = mentorData(mentorID, "", "", "", "", "", "", "")
        getMentors(mentorID) {
            user_name.text = Mentor.name
            price.text=Mentor.price
            val imageURL = "${url}MentorImages/${Mentor.profileImg}"
            Picasso.get().load(imageURL).into(user_img)

        }

        val database = FirebaseDatabase.getInstance()
        val mentorsRef = database.getReference("mentors")
        var profilePicUrl:String=""
//
//        val query = mentorsRef.orderByChild("name").equalTo(input_txt.toString())
//
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (mentorSnapshot in dataSnapshot.children) {
//
//                    val name = mentorSnapshot.child("name").getValue(String::class.java)
//                    val mentor_price = mentorSnapshot.child("name").getValue(String::class.java)
//                    profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java).toString()
//                    mentor_occupation = mentorSnapshot.child("occupation").getValue(String::class.java).toString()
//
//                    // Check if all required fields are present
//                    if (name != null && mentor_price!=null && profilePicUrl != null) {
//                        user_name.text=name.toString()
//                        price.text=mentor_price.toString()
//                        Picasso.get().load(profilePicUrl).into(user_img)
//                    }
//                }
//                // Notify your adapter that the data has changed
//                // adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//                Toast.makeText(this@book_session_activity,"Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
//            }
//        })


        back_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }

        book_appoint_btn.setOnClickListener{

            val database = FirebaseDatabase.getInstance()
            var my_ref = database.getReference("users")
            val curr = mAuth.currentUser
            val id= curr?.uid.toString()

            if(input_txt!=null){

                if(Mentor.profileImg!=""){

                    val tempUrl = "${url}addbookedsession.php"

                    val stringRequest = object : StringRequest(
                        com.android.volley.Request.Method.POST, tempUrl,
                        com.android.volley.Response.Listener { response ->

                            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    return@addOnCompleteListener
                                }
                                val token = task.result
                                sendPushNotification(
                                    token,
                                    input_txt.toString(),
                                    "Subtitle: Class",
                                    "Hi! There",
                                    mapOf("key1" to "value1", "key2" to "value2")
                                )

                            }

                            Toast.makeText(this,"Session Booked Successfully", Toast.LENGTH_LONG).show()

                            // Handle response
//                            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
//                            startActivity(nextActivityIntent)
                            onBackPressed()
                            finish()
                                          },
                        com.android.volley.Response.ErrorListener { error ->
                            // Handle error
                            Toast.makeText(this, "Error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
                        }) {
                        override fun getParams(): MutableMap<String, String> {
                            val params = HashMap<String, String>()
                            params["userID"] = userID
                            params["mentorID"] = Mentor.mentorID
                            params["name"] = Mentor.name
                            params["occupation"] = Mentor.occupation
                            params["date"] = selectedDate
                            params["time"] = selected_time
                            params["profileImg"] = Mentor.profileImg
                            return params
                        }
                    }

                    // Add the request to the RequestQueue
                    Volley.newRequestQueue(this).add(stringRequest)
                }
                else{
                    Toast.makeText(this,"Fetching Data, Please Wait",Toast.LENGTH_LONG).show()
                }

            }
            else{
                Toast.makeText(this,"Still Fetching Data from Database",Toast.LENGTH_LONG).show()

            }

        }

        message_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, chat_1_activity::class.java)
            nextActivityIntent.putExtra("mentorID", mentorID)
            nextActivityIntent.putExtra("userID", userID)
            nextActivityIntent.putExtra("MENTOR_NAME", input_txt)
            startActivity(nextActivityIntent)
//            val nextActivityIntent = Intent(this, chat_1_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        audiocall_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, call_1_activity::class.java)
            nextActivityIntent.putExtra("mentorID", mentorID)
            nextActivityIntent.putExtra("userID", userID)
            nextActivityIntent.putExtra("MENTOR_NAME", input_txt)
            startActivity(nextActivityIntent)
            finish()
        }

        videocall_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, call_2_activity::class.java)
            nextActivityIntent.putExtra("mentorID", mentorID)
            nextActivityIntent.putExtra("userID", userID)
            nextActivityIntent.putExtra("MENTOR_NAME", input_txt)
            startActivity(nextActivityIntent)
            finish()
        }


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

    private fun getMentors(mentorID: String, callback: () -> Unit) {

        val tempURL = "${url}getmentordata_id.php"

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST, tempURL,
            com.android.volley.Response.Listener { response ->
                // Handle response
                if (response.startsWith("Mentor not found")) {
                    // User not found, handle accordingly
                    Toast.makeText(this, "Mentor not found", Toast.LENGTH_SHORT).show()
                } else {
                    // Parse JSON response
                    try {
                        val jsonObject = JSONObject(response)
                        Mentor.mentorID = jsonObject.getString("mentorID")
                        Mentor.name = jsonObject.getString("name")
                        Mentor.occupation = jsonObject.getString("occupation")
                        Mentor.description = jsonObject.getString("description")
                        Mentor.price = jsonObject.getString("price")
                        Mentor.profileImg = jsonObject.getString("profileImg")
                        Mentor.status = jsonObject.getString("status")
                        Mentor.favourite = jsonObject.getString("favourite")
                        callback()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Error Fetching Data", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            com.android.volley.Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "Error Fetching Data", Toast.LENGTH_SHORT).show()
                Log.e("API Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["mentorID"] = mentorID
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

    }

}