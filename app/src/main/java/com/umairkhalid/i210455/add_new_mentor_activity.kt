package com.umairkhalid.i210455

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class add_new_mentor_activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private var  mAuth = FirebaseAuth.getInstance();

    private val PICK_VIDEO_REQUEST = 101
    private val PICK_IMAGE_REQUEST = 71
    private var img_path: Uri? = null
    private var vid_path: Uri? = null
    private lateinit var storage_ref: StorageReference
    private lateinit var database_ref: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private var type:Int =0

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

        setContentView(R.layout.add_new_mentor)

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
        val back_btn: ImageButton =findViewById(R.id.back_btn)

        val upload_btn : Button =findViewById(R.id.upload_btn)
        val video_text : TextView =findViewById(R.id.video_text)
        val camera_text : TextView =findViewById(R.id.camera_text)
        val video_btn : ImageButton =findViewById(R.id.video_image)
        val camera_btn: ImageButton =findViewById(R.id.camera_image)
        val mentor_name : EditText=findViewById(R.id.add_mentor_name)
        val mentor_desc :EditText=findViewById(R.id.add_mentor_desc)


        val status_spinner: Spinner = findViewById(R.id.mentor_status)
        status_spinner.prompt = "Select City"
        val status_list = arrayOf("Available","Unavailable")

        val city_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, status_list)
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        status_spinner.adapter = city_adapter

        status_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Set the selected item text to Spinner's prompt
                status_spinner.prompt = status_list[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                status_spinner.prompt = "Available"
            }
        }


        upload_btn.setOnClickListener{
            val name=mentor_name.text.toString().trim()
            val desc=mentor_desc.text.toString().trim()
            val status= status_spinner.selectedItem.toString().trim()

            if (name.isNotEmpty() && desc.isNotEmpty() && status.isNotEmpty() )
            {
                if(type==1){
                    if(img_path!=null){

                        val database = FirebaseDatabase.getInstance()
                        var my_ref = database.getReference("mentors")
                        my_ref.child(name).setValue(null)
                        my_ref.child(name).child("name").setValue(name)
                        my_ref.child(name).child("description").setValue(desc)
                        my_ref.child(name).child("status").setValue(status)
                        my_ref.child(name).child("price").setValue("$100/Session")
                        my_ref.child(name).child("occupation").setValue("App Developer")
                        my_ref.child(name).child("favourite").setValue("False")
                        my_ref.child(name).child("profile_pic").setValue(img_path.toString())
                        my_ref.child(name).child("profile_vid").setValue(vid_path)

                        val storageRef = FirebaseStorage.getInstance().reference
                        val profileImageRef = storageRef.child("mentor_images/$name.jpg")

                        val uploadTask = profileImageRef.putFile(img_path!!)

                        uploadTask.addOnSuccessListener { taskSnapshot ->
                            // Image uploaded successfully, now get the download URL
                            profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                                // Save download URL to Firebase Realtime Database
                                val image_url = uri.toString()
                                val database = FirebaseDatabase.getInstance()
                                val myRef = database.getReference("mentors/$name/profile_pic")

                                myRef.setValue(image_url).addOnSuccessListener {

                                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                        if (!task.isSuccessful) {
                                            return@addOnCompleteListener
                                        }
                                        val token = task.result
                                        sendPushNotification(
                                            token,
                                            "MentorMe",
                                            "Subtitle: Class",
                                            "New Mentors Have been Added, Don't Forget To Check em Out",
                                            mapOf("key1" to "value1", "key2" to "value2")
                                        )

                                    }

                                    Toast.makeText(this, "Mentor Added Successfully", Toast.LENGTH_SHORT).show()
                                    val nextActivityIntent = Intent(this, home_page_activity::class.java)
                                    startActivity(nextActivityIntent)
                                    finish()
                                }
                                    .addOnFailureListener { e ->
//                            Toast.makeText(this, "Failed to update profile picture", Toast.LENGTH_SHORT).show()
                                        Log.d("TAG", "Failed To Upload Profile Image")

                                    }
                            }
                        }.addOnFailureListener { e ->
//                Toast.makeText(this, "Failed To Upload", Toast.LENGTH_SHORT).show()
                            Log.d("TAG", "Failed To Upload Profile Image")

                        }


                    }else{
                        Toast.makeText(this,"Image Content is still to be fetched", Toast.LENGTH_LONG).show()
                    }

                }
                else if(type==2){

                    if(vid_path!=null){
                        val database = FirebaseDatabase.getInstance()
                        var my_ref = database.getReference("mentors")
                        my_ref.child(name).setValue(null)
                        my_ref.child(name).child("name").setValue(name)
                        my_ref.child(name).child("description").setValue(desc)
                        my_ref.child(name).child("status").setValue(status)
                        my_ref.child(name).child("price").setValue("$100/Session")
                        my_ref.child(name).child("occupation").setValue("App Developer")
                        my_ref.child(name).child("favourite").setValue("False")
                        my_ref.child(name).child("profile_pic").setValue(img_path)
                        my_ref.child(name).child("profile_vid").setValue(vid_path.toString())

                        val storageRef = FirebaseStorage.getInstance().reference
                        val profileImageRef = storageRef.child("mentor_vids/$name.jpg")

                        val uploadTask = profileImageRef.putFile(vid_path!!)

                        uploadTask.addOnSuccessListener { taskSnapshot ->
                            // Image uploaded successfully, now get the download URL
                            profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                                // Save download URL to Firebase Realtime Database
                                val image_url = uri.toString()
                                val database = FirebaseDatabase.getInstance()
                                val myRef = database.getReference("mentors/$name/profile_vid")

                                myRef.setValue(image_url).addOnSuccessListener {

                                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                        if (!task.isSuccessful) {
                                            return@addOnCompleteListener
                                        }
                                        val token = task.result
                                        sendPushNotification(
                                            token,
                                            "MentorMe",
                                            "Subtitle: Class",
                                            "New Mentors Have been Added, Don't Forget To Check em Out",
                                            mapOf("key1" to "value1", "key2" to "value2")
                                        )

                                    }

                                    Toast.makeText(this, "Mentor Added Successfully", Toast.LENGTH_SHORT).show()
                                    val nextActivityIntent = Intent(this, home_page_activity::class.java)
                                    startActivity(nextActivityIntent)
                                    finish()
                                }
                                    .addOnFailureListener { e ->
//                            Toast.makeText(this, "Failed to update profile picture", Toast.LENGTH_SHORT).show()
                                        Log.d("TAG", "Failed To Upload Profile Image")

                                    }
                            }
                        }.addOnFailureListener { e ->
//                Toast.makeText(this, "Failed To Upload", Toast.LENGTH_SHORT).show()
                            Log.d("TAG", "Failed To Upload Profile Image")

                        }

                    }else{
                        Toast.makeText(this,"Video Content is still to be fetched", Toast.LENGTH_LONG).show()
                    }

                }
            }
            else{
                Toast.makeText(this,"Please fill in all fields", Toast.LENGTH_LONG).show()
            }

//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
        }

        video_text.setOnClickListener{
            type=2
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*" // Set the MIME type to video/*
            startActivityForResult(intent, PICK_VIDEO_REQUEST)
//            val nextActivityIntent = Intent(this, camera_video_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
        }

        video_btn.setOnClickListener{
            type=2
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*" // Set the MIME type to video/*
            startActivityForResult(intent, PICK_VIDEO_REQUEST)
//            val nextActivityIntent = Intent(this, camera_video_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
        }

        camera_btn.setOnClickListener{
            type=1
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
//            val nextActivityIntent = Intent(this, camera_photo_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
        }

        camera_text.setOnClickListener{
            type=1
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
//            val nextActivityIntent = Intent(this, camera_photo_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
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

        back_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
//            img_path = data.data
            val selectedImageUri: Uri = data.data!!
            // Now you can use the selectedImageUri as needed, for example:
            img_path = selectedImageUri
        }
        else if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                val selectedVideoUri: Uri = data.data!!
                // Now you can use the selectedVideoUri as needed, for example:
                vid_path = selectedVideoUri
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
}