package com.umairkhalid.i210455

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class add_new_mentor_activity : AppCompatActivity() {
    private var  mAuth = FirebaseAuth.getInstance();

    private val PICK_VIDEO_REQUEST = 101
    private val PICK_IMAGE_REQUEST = 71
    private var img_path: Uri? = null
    private var vid_path: Uri? = null
    private lateinit var storage_ref: StorageReference
    private lateinit var database_ref: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private var type:Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_new_mentor)

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
}