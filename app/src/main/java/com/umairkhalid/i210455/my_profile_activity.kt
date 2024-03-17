package com.umairkhalid.i210455

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class my_profile_activity : AppCompatActivity()  , click_listner{
    private var  mAuth = FirebaseAuth.getInstance();

    private lateinit var profile_pic :ImageView
    private lateinit var cover_page:ImageView
    private val PICK_IMAGE_REQUEST = 71
    private var file_path: Uri? = null
    private lateinit var storage_ref: StorageReference
    private lateinit var database_ref: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private var type:Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile)

//        val toolbar = findViewById<Toolbar>(R.id.profile_toolbar)
//        setSupportActionBar(toolbar)

        // Customize the action bar as needed
//        supportActionBar?.apply {
//            // Set title
//            title = "Custom Action Bar"
//            // Set subtitle
//            subtitle = "Subtitle"
//            // Set whether to display the home/up button
//            setDisplayHomeAsUpEnabled(true)
//            // Set whether to display the title
//            setDisplayShowTitleEnabled(true)
//            // Set whether to display the logo
//            setDisplayUseLogoEnabled(false)
//            // Set whether to show custom view
//            setDisplayShowCustomEnabled(false)
//        }
//
//        // Set the title of the toolbar
//        supportActionBar?.title = "New Title"

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
        val log_out:Button=findViewById(R.id.profile_logout_btn)


        val editprofile_btn: ImageButton =findViewById(R.id.edit_profile)
        val booked_session_btn: Button =findViewById(R.id.booked_sessions)

        val username:TextView=findViewById(R.id.profile_username)
        val user_city:TextView=findViewById(R.id.profile_city)
        profile_pic = findViewById<ImageView>(R.id.profile_dp)
        cover_page=findViewById(R.id.profile_cover)
        var pic_url:String=""
        var cover_url:String=""

        var database = FirebaseDatabase.getInstance()
        val my_ref = database.getReference("users")
        var currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if(userId!=null){

            my_ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val name = dataSnapshot.child(userId).child("name").value.toString()
                    val city = dataSnapshot.child(userId).child("city").value.toString()
                    pic_url= dataSnapshot.child(userId).child("picture").value.toString()
                    cover_url = dataSnapshot.child(userId).child("cover").value.toString()
                    username.text=name
                    user_city.text=city

                    if(pic_url!=null){
                        Picasso.get().load(pic_url).into(profile_pic)
                    }
                    if(cover_url!=null){
                        Picasso.get().load(cover_url).into(cover_page)
                    }

                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.d("TAG", "Unable to retrieve Data")

                }
            })
        }


        booked_session_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, booked_sessions_activity::class.java)
            startActivity(nextActivityIntent)
        }

        editprofile_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, editprofile_activity::class.java)
            startActivity(nextActivityIntent)
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

        log_out.setOnClickListener{
            mAuth.signOut()
            finish()
            finishAffinity();
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
        }

        // 1- AdapterView: RecyclerView
        val recyclerView_fav : RecyclerView = findViewById(R.id.recyclerview_favmentor)
        recyclerView_fav.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // 2- Data Source: List of  Objects
        var adapter_data_list_fav : ArrayList<recycler_educator_data> = ArrayList()

        database = FirebaseDatabase.getInstance()
        val mentorsRef = database.getReference("mentors")

        val query = mentorsRef.orderByChild("favourite").equalTo("True")

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
                        val mentorData = recycler_educator_data(
                            profilePicUrl,
                            name,
                            occupation,
                            status,
                            price
                        )
                        adapter_data_list_fav.add(mentorData)
                    }
                }

                val adapter_top = recycler_educator_adapter(adapter_data_list_fav,this@my_profile_activity)
                recyclerView_fav.adapter = adapter_top

                // Notify your adapter that the data has changed
                adapter_top.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@my_profile_activity, "Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()

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
//        adapter_data_list_fav.add(v1)
//        adapter_data_list_fav.add(v2)
//        adapter_data_list_fav.add(v3)
//        adapter_data_list_fav.add(v4)
//        adapter_data_list_fav.add(v5)
//        adapter_data_list_fav.add(v6)
//        adapter_data_list_fav.add(v7)
//
//        // 3- Adapter
//        val adapter_fav = recycler_educator_adapter(adapter_data_list_fav)
//        recyclerView_fav.adapter = adapter_fav




        // 1- AdapterView: RecyclerView
        val recyclerView : RecyclerView = findViewById(R.id.recyclerview_myreview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        // 2- Data Source: List of  Objects
        var adapter_data_list_rev : ArrayList<recycler_review_data> = ArrayList()

        currentUser = FirebaseAuth.getInstance().currentUser
        val id = currentUser?.uid

        if(id!=null){

            database = FirebaseDatabase.getInstance()
            val mentorsRef = database.getReference("users").child(id).child("reviews") // Replace with your actual reference

            mentorsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (mentorSnapshot in dataSnapshot.children) {
                        val name = mentorSnapshot.child("mentor_name").getValue(String::class.java)
                        val rev = mentorSnapshot.child("mentor_review").getValue(String::class.java)

                        // Check if all required fields are present
                        if (name != null && rev != null ) {
                            val mentorData = recycler_review_data(
                                name,
                                rev
                            )
                            adapter_data_list_rev.add(mentorData)
                        }
                    }

                    val adapter_rev = recycler_review_adapter(adapter_data_list_rev)
                     recyclerView.adapter = adapter_rev

                    // Notify your adapter that the data has changed
                    adapter_rev.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })


        }

//        val v8  = recycler_review_data("John Cooper","John provided excellent Prototyping Techniques and insights. I highly recommend him!")
//        val v9  = recycler_review_data("Emma Phillips","Her tips were valuable. Would love to connect again.")
//        val v10  = recycler_review_data("Jane","Impressive Session")
//        val v11  = recycler_review_data("Umair","SUIII")
//
//        adapter_data_list_rev.add(v8)
//        adapter_data_list_rev.add(v9)
//        adapter_data_list_rev.add(v10)
//        adapter_data_list_rev.add(v11)
//
//        // 3- Adapter
//        val adapter_rev = recycler_review_adapter(adapter_data_list_rev)
//        recyclerView.adapter = adapter_rev

        storage_ref = FirebaseStorage.getInstance().reference
        database_ref = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        profile_pic.setOnClickListener {
            type=1
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        cover_page.setOnClickListener{
            type=2
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)

        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            file_path = data.data
            // Set the selected image to the profileImageView
            if(type==1){

                Picasso.get().load(file_path).into(profile_pic)

                val my_database = FirebaseDatabase.getInstance()
                var my_ref = my_database.getReference("users")
                val currUser = mAuth.currentUser
                val userId= currUser?.uid.toString()

                val imageURL = file_path.toString()
                my_ref.child(userId).child("picture").setValue(imageURL)
                my_ref.child(userId).child("picture_gal").setValue(imageURL)


                val storageRef = FirebaseStorage.getInstance().reference

                val profileImageRef = storageRef.child("profile_images/$userId.jpg")
                val uploadTask = profileImageRef.putFile(file_path!!)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully, now get the download URL
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save download URL to Firebase Realtime Database
                        val image_url = uri.toString()
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("users/$userId/picture")

                        myRef.setValue(image_url).addOnSuccessListener {
                            Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
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

            }
            else if(type==2){

                Picasso.get().load(file_path).into(cover_page)

                val my_database = FirebaseDatabase.getInstance()
                var my_ref = my_database.getReference("users")
                val currUser = mAuth.currentUser
                val userId= currUser?.uid.toString()

                val imageURL = file_path.toString()
                my_ref.child(userId).child("cover").setValue(imageURL)
                my_ref.child(userId).child("cover_gal").setValue(imageURL)


                val storageRef = FirebaseStorage.getInstance().reference

                val profileImageRef = storageRef.child("cover_images/$userId.jpg")
                val uploadTask = profileImageRef.putFile(file_path!!)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully, now get the download URL
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save download URL to Firebase Realtime Database
                        val image_url = uri.toString()
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("users/$userId/cover")

                        myRef.setValue(image_url).addOnSuccessListener {
                            Toast.makeText(this, "Cover updated!", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener { e ->
//                            Toast.makeText(this, "Failed to update profile picture", Toast.LENGTH_SHORT).show()
                                Log.d("TAG", "Failed To Upload Cover Image")

                            }
                    }
                }.addOnFailureListener { e ->
//                Toast.makeText(this, "Failed To Upload", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Failed To Upload Cover Image")

                }

            }
        }
    }
    override fun click_function(txt:String){
        val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
        nextActivityIntent.putExtra("user_name", txt)
        startActivity(nextActivityIntent)

    }

}



//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.profile_actionbar_menu, menu)
//        return true
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_logout -> {
//                Toast.makeText(this,"Logout", Toast.LENGTH_LONG).show()
//                // Handle logout action
//                return true
//            }
////            R.id.action_back_btn -> {
////                Toast.makeText(this,"Back Btn",Toast.LENGTH_LONG).show()
////
////                // Handle back arrow click (if needed)
//////                onBackPressed() // Or perform any other action you desire
////                return true
////            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
