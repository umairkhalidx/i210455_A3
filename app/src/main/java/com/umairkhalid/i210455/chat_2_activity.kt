package com.umairkhalid.i210455

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class chat_2_activity : AppCompatActivity() {
    lateinit var userID: String
    lateinit var url: String
    lateinit var mentorID: String
    lateinit var mentorImg: String
    var lastcheck: Long = 0
    var maxcounter :Int =0

    private lateinit var imgBitmap : Bitmap
    private lateinit var selectedImageUri :String
    private lateinit var encodedImage:String
    private lateinit var uri:Uri



    @SuppressLint("MissingInflatedId")

    private lateinit var message_recycle_view: RecyclerView
    private lateinit var message_adapter: message_adapter
    private lateinit var editTextMessage: EditText
    private lateinit var send_btn: ImageButton
    private lateinit var file_btn: ImageButton
    private lateinit var image_upload_btn: ImageButton
    private lateinit var voice_message_btn: ImageButton
    private lateinit var camera_btn: ImageButton
    private lateinit var call_button: ImageButton
    private lateinit var videocall_button: ImageButton

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, you can proceed with sending notifications
        } else {
            // Permission is not granted, handle accordingly
        }
    }

    var my_flag:Int=0


    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var messages_ref: DatabaseReference
    private lateinit var storage_ref: StorageReference

    private val IMAGE_PICK_CODE = 1000
    private val FILE_PICK_CODE = 1001
    private val CAMERA_REQUEST_CODE = 1002
    private val message_list = mutableListOf<message_data>()
    private var selected_msg_position: Int = -1

    private lateinit var audio_recorder: message_audio_record

    private val offline_messages = mutableListOf<message_data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(R.layout.chat_2)
        userID = intent.getStringExtra("userID").toString()
        mentorID = intent.getStringExtra("mentorID").toString()
        url = getString(R.string.url)
        getMaxCounter(mentorID)

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
        val back_btn_letsfind: ImageButton =findViewById(R.id.back_btn)

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

        back_btn_letsfind.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }



        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid.toString()

        messages_ref = database.reference.child("community")
        storage_ref = FirebaseStorage.getInstance().reference

        audio_recorder = message_audio_record()

        // Initialize views
        val mentorNameTextView = findViewById<TextView>(R.id.community_mentor_name)

        message_recycle_view = findViewById(R.id.recyclerview_messages)
        editTextMessage = findViewById(R.id.message_txt)
        send_btn = findViewById(R.id.message_send)
        file_btn = findViewById(R.id.message_attach)
        image_upload_btn = findViewById(R.id.message_img)
        voice_message_btn = findViewById(R.id.message_audio)
        camera_btn = findViewById(R.id.photo_btn)
//        callbutton = findViewById(R.id.audiocall_button)
//        videocallbutton = findViewById(R.id.videocall_btn)



        val mentorName = intent.getStringExtra("MENTOR_NAME")
        val user_img:ImageView=findViewById(R.id.comunity_img)


        getMentorImg(mentorID){
            if(mentorImg!=""){
                mentorNameTextView.text = mentorName
                val imageURL = "${url}MentorImages/${mentorImg}"
                Picasso.get().load(imageURL).into(user_img)
            }
        }


//        mentorName?.let {
//            mentorNameTextView.text = it
//            messages_ref = database.reference.child("community").child("messages_$it") // Use mentor's name in the database path
//        }


        val audiocall_btn: ImageButton =findViewById(R.id.audiocall_btn)
        val videocall_btn: ImageButton =findViewById(R.id.call_btn_1)

        audiocall_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, call_1_activity::class.java)
            nextActivityIntent.putExtra("mentorID", mentorID)
            nextActivityIntent.putExtra("userID", userID)
            nextActivityIntent.putExtra("MENTOR_NAME",mentorName )
            startActivity(nextActivityIntent)
        }

        videocall_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, call_2_activity::class.java)
            nextActivityIntent.putExtra("mentorID", mentorID)
            nextActivityIntent.putExtra("userID", userID)
            nextActivityIntent.putExtra("MENTOR_NAME",mentorName )
            startActivity(nextActivityIntent)
        }


//        val temp_database = FirebaseDatabase.getInstance()
//        val temp_ref= temp_database.getReference("mentors")
//
//        val temp_query = temp_ref.orderByChild("name").equalTo(mentorName.toString())
//
//        temp_query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (mentorSnapshot in dataSnapshot.children) {
//                    val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)
//                    // Check if all required fields are present
//                    if ( profilePicUrl != null) {
//                        Picasso.get().load(profilePicUrl).into(user_img)
//                    }
//                }
//                // Notify your adapter that the data has changed
//                // adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//                Toast.makeText(this@chat_2_activity,"Unable to Mentor Image", Toast.LENGTH_LONG).show()
//            }
//        })



        // Setup RecyclerView and Adapter
        message_adapter = message_adapter(message_list, object : message_adapter.OnMessageClickListener {
            override fun onMessageClick(position: Int) {
                selected_msg_position = position
                val message = message_list[position]
                if (message.audioUrl != null) {
                    // Play audio
//                    play_audio_func(message.audioUrl!!)
                } else {
                    // Handle other message types
                    show_edit_dialogbox(message)
                }
            }

            override fun onMessageLongClick(position: Int) {
                selected_msg_position = position
                show_delete_dialog()
            }
        })

//        messageRecyclerView.apply {
//            layoutManager = LinearLayoutManager(this@chat_1_activity)
//            adapter = messageAdapter
//            addItemDecoration(ItemOffsetDecoration(8)) // Adding item decoration with 16dp offset
//        }
        message_recycle_view.apply {
            val layoutManager = LinearLayoutManager(this@chat_2_activity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
//            layoutManager.reverseLayout = true // Display items from bottom
            layoutManager.stackFromEnd = true
            this.layoutManager = layoutManager
            adapter = message_adapter
        }

        // Send message button click listener
        send_btn.setOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                send_message_func(messageText,mentorName.toString())
            }
        }

        // File button click listener
        file_btn.setOnClickListener {
            pickFileFromDevice()
        }

        // Image upload button click listener
        image_upload_btn.setOnClickListener {
            pickImageFromGallery()
        }

        // Camera button click listener
        camera_btn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                open_camera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
            }
        }

        // Voice message button click listener
        voice_message_btn.setOnClickListener {

            //Ask for audio permission here

            if (audio_recorder.isRecording) {
                audio_recorder.stopRecording { audioUri ->
                    if (audioUri != null) {
                        Toast.makeText(this, "Recording Stoped", Toast.LENGTH_SHORT).show()
                        upload_audio(audioUri)

                    } else {
                        // Handle audio recording failure
                        Toast.makeText(this, "Failed to record audio", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                audio_recorder.startRecording()
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
            }
        }








        // Listen for new messages

//        var lastCheckedTimestamp = System.currentTimeMillis()
//        lastCheckedTimestamp=-1
//        lastcheck=-1
//        longPolling(lastCheckedTimestamp, mentorID)
        getCommunityData(mentorID)









//
//        messages_ref.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                val message = snapshot.getValue(message_data::class.java)
//                if (message != null) {
//                    message_list.add(message)
//                    message_adapter.notifyItemInserted(message_list.size - 1)
//                    scrollToBottom()
//                }
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                val updatedMessage = snapshot.getValue(message_data::class.java)
//                if (updatedMessage != null) {
//                    val existingMessage = message_list.find { it.userId == updatedMessage.userId }
//                    if (existingMessage != null) {
//                        val index = message_list.indexOf(existingMessage)
//                        message_list[index] = updatedMessage
//                        message_adapter.notifyItemChanged(index)
//                    }
//                }
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                val removedMessage = snapshot.getValue(message_data::class.java)
//                if (removedMessage != null) {
//                    val existingMessage = message_list.find { it.userId == removedMessage.userId }
//                    if (existingMessage != null) {
//                        val index = message_list.indexOf(existingMessage)
//                        message_list.removeAt(index)
//                        message_adapter.notifyItemRemoved(index)
//                    }
//                }
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//
//            override fun onCancelled(error: DatabaseError) {}
//        })

        // Check for internet connectivity and sync offline messages
        if (check_network()) {
            sync_offline_messages(mentorName.toString())
        } else {
            Toast.makeText(this, "No internet connection. Offline mode.", Toast.LENGTH_SHORT).show()
        }
    }



    fun getMentorImg(mentorID: String, callback: () -> Unit) {
        val tempUrl = "${url}getmentorimg.php"

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST, tempUrl,
            com.android.volley.Response.Listener { response ->
                mentorImg=response
                callback()
            },
            com.android.volley.Response.ErrorListener { error ->
                // Handle error
                error.printStackTrace()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["mentorID"] = mentorID
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)
    }




    private fun scrollToBottom() {
        message_recycle_view.scrollToPosition(message_adapter.itemCount - 1)
    }

    private fun send_message_func(messageText: String,mentor_name:String) {

        val tempUrl = "${url}insertcommunitymessage.php"
        val type:Int=1
        maxcounter=maxcounter+1

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST, tempUrl,
            com.android.volley.Response.Listener { response ->
                // Handle response
                val message = message_data(userID,mentorID, messageText, System.currentTimeMillis(), null, null, null,"","1",maxcounter-1)
                message_list.add(message)
                // Notify the adapter about the new message
                message_adapter.notifyItemInserted(message_list.size - 1)
//                     Scroll to the bottom of the message list
                scrollToBottom()

                if(my_flag==0){

                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            return@addOnCompleteListener
                        }
                        val token = task.result
                        sendPushNotification(
                            token,
                            mentor_name.toString(),
                            "Subtitle: Class",
                            "I'll Get Back to You Soon",
                            mapOf("key1" to "value1", "key2" to "value2")
                        )

                    }
                    my_flag=1;

                }
            },
            com.android.volley.Response.ErrorListener { error ->
                // Handle message sending failure
                Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                // Add message to offline list
                val message = message_data(userID,mentorID, messageText, System.currentTimeMillis(), null, null, null,"","1",maxcounter-1)
                offline_messages.add(message)
                message_list.add(message)
                // Notify the adapter about the new message
                message_adapter.notifyItemInserted(message_list.size - 1)
//                     Scroll to the bottom of the message list
                scrollToBottom()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["mentorID"] = mentorID
                params["message"] =  messageText
                params["image"] = ""
                params["file"] = ""
                params["audio"] = ""
                params["type"] = type.toString()
                params["time"] = System.currentTimeMillis().toString()
                params["counter"] = maxcounter.toString()
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

//        val userId = auth.currentUser?.uid
//        if (userId != null) {
//            val message = message_data(userId, messageText, System.currentTimeMillis(), null, null, null)
//            messages_ref.push().setValue(message)
//                .addOnSuccessListener {
//                    editTextMessage.text.clear()
//
//                    if(my_flag==0){
//
//                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//                            if (!task.isSuccessful) {
//                                return@addOnCompleteListener
//                            }
//                            val token = task.result
//                            sendPushNotification(
//                                token,
//                                mentor_name.toString(),
//                                "Subtitle: Class",
//                                "I'll Get Back to You Soon",
//                                mapOf("key1" to "value1", "key2" to "value2")
//                            )
//
//                        }
//                        my_flag=1;
//
//                    }
//
//                }
//                .addOnFailureListener {
//                    // Handle message sending failure
//                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
//                    // Add message to offline list
//                    offline_messages.add(message)
//                }
//        }
    }

    fun getMaxCounter(mentorID: String) {
        val tempURL = "${url}getmaxcounter.php"

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST, tempURL,
            com.android.volley.Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    maxcounter = jsonObject.getInt("max_counter")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Handle JSON exception
                }
            },
            com.android.volley.Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle error
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["mentorID"] = mentorID
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun getCommunityData(mentorID: String) {
        val tempURL = "${url}getcommunitymessages.php"

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST, tempURL,
            com.android.volley.Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val message = message_data(
                            userID,
                            mentorID,
                            jsonObject.getString("message"),
                            jsonObject.getLong("time").toLong(),
                            jsonObject.getString("audio"),
                            jsonObject.getString("image"),
                            jsonObject.getString("file"),
                            "",
                            jsonObject.getString("type"),
                            jsonObject.getInt("counter")
                        )
                        if(message.type=="1"){
                            message.imageUrl=null;
                            message.fileUrl=null;
                            message.audioUrl=null;
                        }
                        if(message.type=="2"){
                            message.fileUrl=null;
                            message.audioUrl=null;
                        }
                        if(message.type=="3"){
                            message.imageUrl=null;
                            message.audioUrl=null;
                        }
                        if(message.type=="4"){
                            message.imageUrl=null;
                            message.fileUrl=null;
                        }
                        message_list.add(message)
                        message_adapter.notifyItemInserted(message_list.size - 1)
                        scrollToBottom()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Handle JSON exception
                }
            },
            com.android.volley.Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle error
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["mentorID"] = mentorID
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)
    }












    // Define a function to fetch new messages
    private fun fetchNewMessages(lastCheckedTimestamp: Long, mentorID: String, callback: (JSONArray) -> Unit) {
        val tempUrl = "${url}listenformessages.php"
        val request = object : StringRequest(
            com.android.volley.Request.Method.GET,
            "$tempUrl?counter=$lastCheckedTimestamp&mentorID=$mentorID",
            com.android.volley.Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    callback(jsonArray)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            com.android.volley.Response.ErrorListener { error ->
                error.printStackTrace()
            }) {}

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request)
    }

    // Long-polling function
    private fun longPolling(lastCheckedTimestamp: Long, mentorID: String) {
        fetchNewMessages(lastCheckedTimestamp, mentorID) { jsonArray ->
            // Iterate through the JSON array of new messages
            for (i in 0 until jsonArray.length()) {
                try {
                    val jsonObject = jsonArray.getJSONObject(i)
//                    if(lastcheck<=(maxcounter-1)){
//                        lastcheck= maxcounter.toLong()
//                    }
//                    else{
//                        lastcheck= jsonObject.getLong("counter").toLong()+1
//                    }
                    lastcheck= maxcounter.toLong()

                    Toast.makeText(this, this.lastcheck.toString(),Toast.LENGTH_SHORT).show()
                    val message = message_data(
                        userID,
                        mentorID,
                        jsonObject.getString("message"),
                        jsonObject.getLong("time").toLong(),
                        jsonObject.getString("audio"),
                        jsonObject.getString("image"),
                        jsonObject.getString("file"),
                        "",
                        jsonObject.getString("type"),
                        jsonObject.getInt("counter")
                    )
                    // Add the message to the message list
                    message_list.add(message)
                    // Notify the adapter about the new message
                    message_adapter.notifyItemInserted(message_list.size - 1)
//                     Scroll to the bottom of the message list
                    scrollToBottom()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            // Continue long-polling for new messages
//            lastcheck= maxcounter.toLong()
            longPolling(lastcheck.toLong(), mentorID)
        }
    }






    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun pickFileFromDevice() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "/"
        intent.type ="*/*"
        startActivityForResult(intent, FILE_PICK_CODE)
    }

    private fun open_camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_CODE -> {
                    uri = data?.data!!
                    imageStore(uri){
                        upload_img(uri)
                    }
//                    data?.data?.let { imageUri ->
//                        imageStore(imageUri){
//                            upload_img(imageUri)
//                        }
////                        upload_img_to_firebase(imageUri)
//                    }
                }
                FILE_PICK_CODE -> {
                    data?.data?.let { fileUri ->
                        upload_file(fileUri)
                    }
                }
                CAMERA_REQUEST_CODE -> {
//                    uri = data?.data!!
//                    imageStore(uri){
//                        upload_img(uri)
//                    }

                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        val imageUri = getImageUriFromBitmap(it)
                        imageStore(imageUri){
                            upload_img(imageUri)
                        }
//                        upload_img(imageUri)
                    }
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Image Title",
            null
        )
        return Uri.parse(path)
    }


    private fun imageStore(uri: Uri,callback: () -> Unit) {
        var inputStream: InputStream? = null
        try {
            inputStream = contentResolver.openInputStream(uri)
            imgBitmap = BitmapFactory.decodeStream(inputStream)
            val stream = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val imageByte: ByteArray = stream.toByteArray()
            encodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT)
            callback()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
    private fun upload_img(imageUri: Uri) {
        var tempUrl= "${url}insertcommunitymessageimage.php"
        val type:Int=2
        maxcounter=maxcounter+1

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST, tempUrl,
            com.android.volley.Response.Listener { response ->
                // Handle response
                val message = message_data(userID,mentorID, "", System.currentTimeMillis(), null, encodedImage.toString(), null,"","2",maxcounter-1)
                message_list.add(message)
                // Notify the adapter about the new message
                message_adapter.notifyItemInserted(message_list.size - 1)
//                     Scroll to the bottom of the message list
                scrollToBottom()
            },
            com.android.volley.Response.ErrorListener { error ->
                // Handle message sending failure
                Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                // Add message to offline list
                val message = message_data(userID,mentorID, "", System.currentTimeMillis(), null,encodedImage.toString() ,null,"","2",maxcounter-1)
                offline_messages.add(message)
                message_list.add(message)
                // Notify the adapter about the new message
                message_adapter.notifyItemInserted(message_list.size - 1)
//                     Scroll to the bottom of the message list
                scrollToBottom()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["mentorID"] = mentorID
                params["message"] = ""
                params["image"] = encodedImage
                params["file"] = ""
                params["audio"] = ""
                params["type"] = type.toString()
                params["time"] = System.currentTimeMillis().toString()
                params["counter"] = maxcounter.toString()
                return params
            }
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)


//        val userId = auth.currentUser?.uid
//        if (userId != null) {
//            val imageRef = storage_ref.child("profile_images/${UUID.randomUUID()}")
//            val uploadTask = imageRef.putFile(imageUri)
//            uploadTask.continueWithTask { task ->
//                if (!task.isSuccessful) {
//                    task.exception?.let {
//                        throw it
//                    }
//                }
//                imageRef.downloadUrl
//            }.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val downloadUri = task.result
//                    val message = message_data(
//                        userId,
//                        "",
//                        System.currentTimeMillis(),
//                        null,
//                        downloadUri.toString(),
//                        null
//                    )
//                    messages_ref.push().setValue(message)
//                } else {
//                    // Handle unsuccessful upload
//                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    @SuppressLint("Range")
    private fun getFileNameFromUri(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                cursor.close()
                return displayName
            }
        }
        return ""
    }

    private fun upload_file(fileUri: Uri) {

        val tempUrl = "${url}insertcommunitymessagefile.php"
        val type: Int = 3 // Assuming file type is 3
        maxcounter = maxcounter + 1
        val name = getFileNameFromUri(fileUri)

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST, tempUrl,
            com.android.volley.Response.Listener { response ->
                val message = message_data(userID,mentorID, "", System.currentTimeMillis(), null,null, name.toString(),"","3",maxcounter-1)
                message_list.add(message)
                // Notify the adapter about the new message
                message_adapter.notifyItemInserted(message_list.size - 1)
//                     Scroll to the bottom of the message list
                scrollToBottom()
            },
            com.android.volley.Response.ErrorListener { error ->

                Toast.makeText(this, "Failed to Upload File", Toast.LENGTH_SHORT).show()
                // Add message to offline list
                val message = message_data(userID,mentorID, "", System.currentTimeMillis(), null,encodedImage.toString() ,null,"","3",maxcounter-1)
                offline_messages.add(message)
                message_list.add(message)
                // Notify the adapter about the new message
                message_adapter.notifyItemInserted(message_list.size - 1)
//                     Scroll to the bottom of the message list
                scrollToBottom()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["mentorID"] = mentorID
                params["message"] = ""
                params["image"] = ""
                params["file"] = name
                params["audio"] = ""
                params["type"] = type.toString()
                params["time"] = System.currentTimeMillis().toString()
                params["counter"] = maxcounter.toString()
                return params
            }
            fun getByteData(): Map<String, DataPart> {
                val fileName = getFileNameFromUri(fileUri)
                val params = HashMap<String, DataPart>()
                val fileInputStream = contentResolver.openInputStream(fileUri)
                val bytes = fileInputStream?.readBytes()
                fileInputStream?.close()
                bytes?.let {
                    // Here, we are sending the actual file data to the server
                    params["file1"] = DataPart(fileName, it, "application/octet-stream")
                }
                return params
            }
        }

// Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

//        val userId = auth.currentUser?.uid
//        if (userId != null) {
//            val fileRef = storage_ref.child("files/${UUID.randomUUID()}")
//            val uploadTask = fileRef.putFile(fileUri)
//            uploadTask.continueWithTask { task ->
//                if (!task.isSuccessful) {
//                    task.exception?.let {
//                        throw it
//                    }
//                }
//                fileRef.downloadUrl
//            }.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val downloadUri = task.result
//                    val message = message_data(
//                        userId,
//                        "",
//                        System.currentTimeMillis(),
//                        null,
//                        null,
//                        downloadUri.toString()
//                    )
//                    messages_ref.push().setValue(message)
//                } else {
//                    // Handle unsuccessful upload
//                    Toast.makeText(this, "Failed to upload file", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    private fun upload_audio(audioUri: Uri) {

        val tempUrl = "${url}insertcommunitymessageaudio.php"
        val type: Int = 4
        maxcounter++
        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST, tempUrl,
            com.android.volley.Response.Listener { response ->
                val message = message_data(userID,mentorID, "", System.currentTimeMillis(), audioUri.toString(),null, null,"","4",maxcounter-1)
                message_list.add(message)
                // Notify the adapter about the new message
                message_adapter.notifyItemInserted(message_list.size - 1)
//                     Scroll to the bottom of the message list
                scrollToBottom()
            },
            com.android.volley.Response.ErrorListener { error ->
                Toast.makeText(this, "Failed to Upload Audio", Toast.LENGTH_SHORT).show()
                // Add message to offline list
                val message = message_data(userID,mentorID, "", System.currentTimeMillis(), audioUri.toString(),null ,null,"","4",maxcounter-1)
                offline_messages.add(message)
                message_list.add(message)
                // Notify the adapter about the new message
                message_adapter.notifyItemInserted(message_list.size - 1)
//                     Scroll to the bottom of the message list
                scrollToBottom()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["mentorID"] = mentorID
                params["message"] = ""
                params["image"] = ""
                params["file"] = ""
                params["audio"] = audioUri.toString()
                params["type"] = type.toString()
                params["time"] = System.currentTimeMillis().toString()
                params["counter"] = maxcounter.toString()
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)
//        val userId = auth.currentUser?.uid
//        if (userId != null) {
//            val audioRef = storage_ref.child("audio/${UUID.randomUUID()}")
//            val uploadTask = audioRef.putFile(audioUri)
//            uploadTask.continueWithTask { task ->
//                if (!task.isSuccessful) {
//                    task.exception?.let {
//                        throw it
//                    }
//                }
//                audioRef.downloadUrl
//            }.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val downloadUri = task.result
//                    val message = message_data(
//                        userId,
//                        "",
//                        System.currentTimeMillis(),
//                        downloadUri.toString(),
//                        null,
//                        null
//                    )
//                    messages_ref.push().setValue(message)
//                    Log.d("AudioUpload", "Audio uploaded successfully: $downloadUri")
//                } else {
//                    // Handle unsuccessful upload
//                    Toast.makeText(this, "Failed to upload audio", Toast.LENGTH_SHORT).show()
//                    Log.e("AudioUpload", "Failed to upload audio")
//                }
//            }
//        }
    }


    private fun play_audio_func(audioUrl: String) {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(audioUrl)
        mediaPlayer.prepare()
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
    }

    private fun show_edit_dialogbox(message: message_data) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit this Message")

        val input = EditText(this)
        input.setText(message.messageText)
        builder.setView(input)

        builder.setPositiveButton("Save") { dialog, which ->

            val editedMessage = input.text.toString().trim()

            if (editedMessage.isNotEmpty()) {
                update_message(message, editedMessage)
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun show_delete_dialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Message")
            .setMessage("Are you sure you want to delete the selected Message")
            .setPositiveButton("Delete") { dialog, which ->
                val message = message_list[selected_msg_position]
                delete_message(message)
                selected_msg_position = -1
            }
            .setNegativeButton("Cancel") { dialog, which ->
                selected_msg_position = -1
                dialog.cancel()
            }
            .show()
    }

    private fun update_message(oldMessage: message_data, newMessageText: String) {

        val tempUrl = "${url}editmessage.php"

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST, tempUrl,
            com.android.volley.Response.Listener { response ->
                val updatedMessage = oldMessage.copy(messageText = newMessageText)
                val index = message_list.indexOf(oldMessage)
                if (index != -1) {
                    message_list[index] = updatedMessage
                    message_adapter.notifyItemChanged(index)
                }
            },
            com.android.volley.Response.ErrorListener { error ->
                Toast.makeText(this,"Unable to Edit Message",Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["counter"] = oldMessage.counter.toString()
                params["mentorID"] = oldMessage.mentorId.toString()
                params["newMessageText"] = newMessageText
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

//        val messageRef = messages_ref.child(oldMessage.userId ?: "")
//        val updatedMessage = oldMessage.copy(messageText = newMessageText)
//        messageRef.setValue(updatedMessage)
//            .addOnSuccessListener {
//                // Update local list and notify adapter
//                val index = message_list.indexOf(oldMessage)
//                if (index != -1) {
//                    message_list[index] = updatedMessage
//                    message_adapter.notifyItemChanged(index)
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Failed to update message", Toast.LENGTH_SHORT).show()
//            }
    }

    private fun delete_message(message: message_data) {

        val tempUrl = "${url}deletemessage.php"

        val stringRequest = object : StringRequest(
            com.android.volley.Request.Method.POST, tempUrl,
            com.android.volley.Response.Listener { response ->

                val index = message_list.indexOf(message)
                if (index != -1) {
                    message_list.removeAt(index)
                    message_adapter.notifyItemRemoved(index)
                } else {
                    // Message not found in the list
                    // Handle this case if needed
                }
            },
            com.android.volley.Response.ErrorListener { error ->
                Toast.makeText(this,"Unable to Delete Message",Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["counter"] = message.counter.toString()
                params["mentorID"] = message.mentorId.toString()
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)


//        val messageRef = messages_ref.child(message.userId ?: "")
//        messageRef.removeValue()
//            .addOnSuccessListener {
//                // Remove from local list and notify adapter
//                val index = message_list.indexOf(message)
//                if (index != -1) {
//                    message_list.removeAt(index)
//                    message_adapter.notifyItemRemoved(index)
//                } else {
//                    // Message not found in the list
//                    // Handle this case if needed
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Failed to delete message", Toast.LENGTH_SHORT).show()
//            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    open_camera()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audio_recorder.cancelRecording()
    }

    private fun check_network(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

    private fun sync_offline_messages(mentor_name:String) {
        for (message in offline_messages) {
            send_message_func(message.messageText,mentor_name.toString())
        }
        offline_messages.clear()
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
