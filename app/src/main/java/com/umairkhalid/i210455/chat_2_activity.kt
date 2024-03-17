package com.umairkhalid.i210455

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.UUID

class chat_2_activity : AppCompatActivity() {

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
        setContentView(R.layout.chat_2)

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

        val audiocall_btn: ImageButton =findViewById(R.id.audiocall_btn)
        val videocall_btn: ImageButton =findViewById(R.id.call_btn_1)

        audiocall_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, call_1_activity::class.java)
            startActivity(nextActivityIntent)
        }

        videocall_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, call_2_activity::class.java)
            startActivity(nextActivityIntent)
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

        val user_img:ImageView=findViewById(R.id.comunity_img)

        val mentorName = intent.getStringExtra("MENTOR_NAME")
        mentorName?.let {
            mentorNameTextView.text = it
            messages_ref = database.reference.child("community").child("messages_$it") // Use mentor's name in the database path
        }

        val temp_database = FirebaseDatabase.getInstance()
        val temp_ref= temp_database.getReference("mentors")

        val temp_query = temp_ref.orderByChild("name").equalTo(mentorName.toString())

        temp_query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (mentorSnapshot in dataSnapshot.children) {
                    val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)
                    // Check if all required fields are present
                    if ( profilePicUrl != null) {
                        Picasso.get().load(profilePicUrl).into(user_img)
                    }
                }
                // Notify your adapter that the data has changed
                // adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@chat_2_activity,"Unable to Mentor Image", Toast.LENGTH_LONG).show()
            }
        })



        // Setup RecyclerView and Adapter
        message_adapter = message_adapter(message_list, object : message_adapter.OnMessageClickListener {
            override fun onMessageClick(position: Int) {
                selected_msg_position = position
                val message = message_list[position]
                if (message.audioUrl != null) {
                    // Play audio
                    play_audio_func(message.audioUrl)
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
                send_message_func(messageText)
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
                        upload_audio_toFirebase(audioUri)

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
        messages_ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(message_data::class.java)
                if (message != null) {
                    message_list.add(message)
                    message_adapter.notifyItemInserted(message_list.size - 1)
                    scrollToBottom()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updatedMessage = snapshot.getValue(message_data::class.java)
                if (updatedMessage != null) {
                    val existingMessage = message_list.find { it.userId == updatedMessage.userId }
                    if (existingMessage != null) {
                        val index = message_list.indexOf(existingMessage)
                        message_list[index] = updatedMessage
                        message_adapter.notifyItemChanged(index)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedMessage = snapshot.getValue(message_data::class.java)
                if (removedMessage != null) {
                    val existingMessage = message_list.find { it.userId == removedMessage.userId }
                    if (existingMessage != null) {
                        val index = message_list.indexOf(existingMessage)
                        message_list.removeAt(index)
                        message_adapter.notifyItemRemoved(index)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

        // Check for internet connectivity and sync offline messages
        if (check_network()) {
            sync_offline_messages()
        } else {
            Toast.makeText(this, "No internet connection. Offline mode.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scrollToBottom() {
        message_recycle_view.scrollToPosition(message_adapter.itemCount - 1)
    }

    private fun send_message_func(messageText: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val message = message_data(userId, messageText, System.currentTimeMillis(), null, null, null)
            messages_ref.push().setValue(message)
                .addOnSuccessListener {
                    editTextMessage.text.clear()
                }
                .addOnFailureListener {
                    // Handle message sending failure
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                    // Add message to offline list
                    offline_messages.add(message)
                }
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
                    data?.data?.let { imageUri ->
                        upload_img_to_firebase(imageUri)
                    }
                }
                FILE_PICK_CODE -> {
                    data?.data?.let { fileUri ->
                        upload_file_to_firebase(fileUri)
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        val imageUri = getImageUriFromBitmap(it)
                        upload_img_to_firebase(imageUri)
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

    private fun upload_img_to_firebase(imageUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val imageRef = storage_ref.child("profile_images/${UUID.randomUUID()}")
            val uploadTask = imageRef.putFile(imageUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val message = message_data(
                        userId,
                        "",
                        System.currentTimeMillis(),
                        null,
                        downloadUri.toString(),
                        null
                    )
                    messages_ref.push().setValue(message)
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun upload_file_to_firebase(fileUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val fileRef = storage_ref.child("files/${UUID.randomUUID()}")
            val uploadTask = fileRef.putFile(fileUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val message = message_data(
                        userId,
                        "",
                        System.currentTimeMillis(),
                        null,
                        null,
                        downloadUri.toString()
                    )
                    messages_ref.push().setValue(message)
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload file", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun upload_audio_toFirebase(audioUri: Uri) {

        val userId = auth.currentUser?.uid
        if (userId != null) {
            val audioRef = storage_ref.child("audio/${UUID.randomUUID()}")
            val uploadTask = audioRef.putFile(audioUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                audioRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val message = message_data(
                        userId,
                        "",
                        System.currentTimeMillis(),
                        downloadUri.toString(),
                        null,
                        null
                    )
                    messages_ref.push().setValue(message)
                    Log.d("AudioUpload", "Audio uploaded successfully: $downloadUri")
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload audio", Toast.LENGTH_SHORT).show()
                    Log.e("AudioUpload", "Failed to upload audio")
                }
            }
        }
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
        val messageRef = messages_ref.child(oldMessage.userId ?: "")
        val updatedMessage = oldMessage.copy(messageText = newMessageText)
        messageRef.setValue(updatedMessage)
            .addOnSuccessListener {
                // Update local list and notify adapter
                val index = message_list.indexOf(oldMessage)
                if (index != -1) {
                    message_list[index] = updatedMessage
                    message_adapter.notifyItemChanged(index)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update message", Toast.LENGTH_SHORT).show()
            }
    }

    private fun delete_message(message: message_data) {
        val messageRef = messages_ref.child(message.userId ?: "")
        messageRef.removeValue()
            .addOnSuccessListener {
                // Remove from local list and notify adapter
                val index = message_list.indexOf(message)
                if (index != -1) {
                    message_list.removeAt(index)
                    message_adapter.notifyItemRemoved(index)
                } else {
                    // Message not found in the list
                    // Handle this case if needed
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete message", Toast.LENGTH_SHORT).show()
            }
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

    private fun sync_offline_messages() {
        for (message in offline_messages) {
            send_message_func(message.messageText)
        }
        offline_messages.clear()
    }
}



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContentView(R.layout.chat_2)
//
//
//        val home_btn: ImageButton =findViewById(R.id.home_btn)
//        val home_txt: TextView =findViewById(R.id.home_txt)
//        val search_btn: ImageButton =findViewById(R.id.search_btn)
//        val search_txt: TextView =findViewById(R.id.search_txt)
//        val chat_btn: ImageButton =findViewById(R.id.chat_btn)
//        val chat_txt: TextView =findViewById(R.id.chat_txt)
//        val profile_btn: ImageButton =findViewById(R.id.profile_btn)
//        val profile_txt: TextView =findViewById(R.id.profile_txt)
//        val plus_btn: ImageButton =findViewById(R.id.plus_btn)
//        val back_btn_letsfind: ImageButton =findViewById(R.id.back_btn)
//
//
//
//        val audiocall_btn: ImageButton =findViewById(R.id.audiocall_btn)
//        val videocall_btn: ImageButton =findViewById(R.id.call_btn_1)
//        val photo_btn: ImageButton =findViewById(R.id.photo_btn)
//
//        photo_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, camera_photo_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//        audiocall_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, call_1_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//        videocall_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, call_2_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//
//        home_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
//        }
//
//        home_txt.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
//        }
//
//        search_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//        search_txt.setOnClickListener{
//            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//        chat_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, chats_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//        chat_txt.setOnClickListener{
//            val nextActivityIntent = Intent(this, chats_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//        profile_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//        profile_txt.setOnClickListener{
//            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//        plus_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//        plus_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
//            startActivity(nextActivityIntent)
//        }
//
//        back_btn_letsfind.setOnClickListener{
////            val nextActivityIntent = Intent(this, home_page_activity::class.java)
////            startActivity(nextActivityIntent)
//            onBackPressed()
//            finish()
//        }
//    }
//
//}