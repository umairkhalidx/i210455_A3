package com.umairkhalid.i210455

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream


class my_profile_activity : AppCompatActivity()  , click_listner{
    lateinit var url :String
    lateinit var userID : String
    lateinit var User : userObject
    var favList = ArrayList<String>()
    var mentorListFav = mutableListOf<mentorData>()
    val reviewsList = ArrayList<reviewData>()



    private lateinit var profile_pic :ImageView
    private lateinit var cover_page:ImageView
    private val PICK_IMAGE_REQUEST = 71
    private var file_path: Uri? = null
    private lateinit var storage_ref: StorageReference
    private lateinit var database_ref: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private var type:Int =0

    private lateinit var imgBitmap : Bitmap
    private lateinit var selectedImageUri :String
    private lateinit var encodedImage:String
    private lateinit var uri:Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile)

        url = getString(R.string.url)
        userID= intent.getStringExtra("userID").toString()

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

//        if (!isNetworkAvailable()) {
//            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show()
//
//            val sharedPref_1 = getSharedPreferences("profile_page_prefs", Context.MODE_PRIVATE)
//            val storedUsername = sharedPref_1.getString("username", "")
//            val storedcity = sharedPref_1.getString("city", "")
//            pic_url = sharedPref_1.getString("profile_pic", "").toString()
//            cover_url = sharedPref_1.getString("cover_pic", "").toString()
//            username.text=storedUsername
//            user_city.text=storedcity
//
//            Picasso.get().load(pic_url).into(profile_pic)
//            Picasso.get().load(cover_url).into(cover_page)
//
//
//            val recyclerView_fav : RecyclerView = findViewById(R.id.recyclerview_favmentor)
//            recyclerView_fav.layoutManager = LinearLayoutManager(this,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//
//            val sharedPref_2 = getSharedPreferences("profile_adapter_fav_prefs", Context.MODE_PRIVATE)
//            val json = sharedPref_2.getString("profile_adapter_fav", "")
//
//            val gson = Gson()
//            val type = object : TypeToken<ArrayList<recycler_educator_data>>() {}.type
//            val adapter_data_list_fav: ArrayList<recycler_educator_data> = gson.fromJson(json, type)
//
//            val adapter_top = recycler_educator_adapter(adapter_data_list_fav,this@my_profile_activity)
//            recyclerView_fav.adapter = adapter_top
//
//            // Notify your adapter that the data has changed
//            adapter_top.notifyDataSetChanged()
//
//
//            // 1- AdapterView: RecyclerView
//            val recyclerView : RecyclerView = findViewById(R.id.recyclerview_myreview)
//            recyclerView.layoutManager = LinearLayoutManager(this,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//
//            val sharedPref_3 = getSharedPreferences("profile_adapter_rev_prefs", Context.MODE_PRIVATE)
//            val json_3 = sharedPref_3.getString("profile_adapter_rev", "")
//
//            val gson_3 = Gson()
//            val type_3 = object : TypeToken<ArrayList<recycler_review_data>>() {}.type
//            val adapter_data_list_rev: ArrayList<recycler_review_data> = gson_3.fromJson(json_3, type_3)
//
//            val adapter_rev = recycler_review_adapter(adapter_data_list_rev)
//            recyclerView.adapter = adapter_rev
//
//            // Notify your adapter that the data has changed
//            adapter_rev.notifyDataSetChanged()
//
//        }

        User = userObject(userID,"","","","","","","")
        getUserData(userID) {
            username.text=User.name
            user_city.text=User.city

            if(User.profileImg!=null){
                val imageURL = "${url}ProfileImages/${User.profileImg}"
                Picasso.get().load(imageURL).into(profile_pic)
            }
            if(User.coverImg!=null){
                val coverURL = "${url}CoverImages/${User.coverImg}"
                Picasso.get().load(coverURL).into(cover_page)
            }


        }

        var database = FirebaseDatabase.getInstance()
        var currentUser = FirebaseAuth.getInstance().currentUser


        booked_session_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, booked_sessions_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
        }

        editprofile_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, editprofile_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
        }


        home_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
            finish()
        }

        home_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
            finish()
        }

        search_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
        }

        search_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
        }

        chat_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
        }

        chat_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
        }

        profile_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
        }

        profile_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            nextActivityIntent.putExtra("userID", User.userID)
            startActivity(nextActivityIntent)
        }

        back_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }

        log_out.setOnClickListener{
            val sharedPreferences = getSharedPreferences("LoginPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("user", "-1")
            editor.apply()
            finish()
            finishAffinity();
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
        }

        getFavouriteMentor(userID) {
            getFavData(favList){
                // 1- AdapterView: RecyclerView
                val recyclerView_fav : RecyclerView = findViewById(R.id.recyclerview_favmentor)
                recyclerView_fav.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

                // 2- Data Source: List of  Objects
                var adapter_data_list_fav : ArrayList<recycler_educator_data> = ArrayList()
                for (mentor in mentorListFav){
                    val mentorData = recycler_educator_data(
                        mentor.mentorID,
                        mentor.profileImg,
                        mentor.name,
                        mentor.occupation,
                        mentor.status,
                        mentor.price, R.drawable.red_heart_btn, 1
                    )

                    adapter_data_list_fav.add(mentorData)
                }

                val adapter_top = recycler_educator_adapter(adapter_data_list_fav,this)
                recyclerView_fav.adapter = adapter_top

            }

        }

        getReviews(userID){
            // 1- AdapterView: RecyclerView
            val recyclerView : RecyclerView = findViewById(R.id.recyclerview_myreview)
            recyclerView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            // 2- Data Source: List of  Objects
            var adapter_data_list_rev : ArrayList<recycler_review_data> = ArrayList()

            for (item in reviewsList){
                val mentorData = recycler_review_data(
                    item.name,
                    item.review
                )
                adapter_data_list_rev.add(mentorData)

            }
            val adapter_rev = recycler_review_adapter(adapter_data_list_rev)
            recyclerView.adapter = adapter_rev

        }

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

    private fun getReviews(userID: String,callback: () -> Unit) {
        val tempUrl = "${url}getreviews.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, tempUrl,
            Response.Listener { response ->
                // Handle response
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val reviewObject = jsonArray.getJSONObject(i)
                        val review = reviewData(
                            reviewObject.getString("userID"),
                            reviewObject.getString("name"),
                            reviewObject.getString("review")
                        )
                        // Add review to the list
                        reviewsList.add(review)
                    }
                    callback()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Handle error
                error.printStackTrace()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["userID"] = userID
                return params
            }
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)
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

    private fun getFavData(favList : ArrayList<String>,callback: () -> Unit ){

        val tempUrl = "${url}getfavmentordata.php"

// Create a StringRequest to send a POST request
        val request = object : StringRequest(Method.POST, tempUrl,
            Response.Listener { response ->
                // Handle response
                try {
                    Log.d("Response", response)

                    // Parse JSON response
                    val jsonObject = JSONObject(response)
                    val mentorsArray = jsonObject.getJSONArray("mentors")

                    // Loop through each mentor object
                    for (i in 0 until mentorsArray.length()) {
                        val mentorObject = mentorsArray.getJSONObject(i)
                        val mentorID = mentorObject.getString("mentorID")
                        if (mentorID in favList) {
                            // If mentorID is in favList, create Mentor object and add it to mentors list
                            val mentor = mentorData(
                                mentorID,
                                mentorObject.getString("name"),
                                mentorObject.getString("occupation"),
                                mentorObject.getString("description"),
                                mentorObject.getString("price"),
                                mentorObject.getString("profileImg"),
                                mentorObject.getString("status"),
                                mentorObject.getString("favourite")
                            )
                            mentorListFav.add(mentor)
                        }
                    }
                    callback()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error Parsing Data", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Handle error
                Log.e("Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                val favListJson = Gson().toJson(favList)
                params["favList"] = favListJson
                return params
            }
        }
        Volley.newRequestQueue(this).add(request)
    }

    private fun getUserData(userID:String,callback: () -> Unit){
        val tempUrl = "${url}getuserdata.php" // Assuming the PHP file to retrieve user data is named getUserData.php

        val stringRequest = object : StringRequest(
            Request.Method.POST, tempUrl,
            Response.Listener { response ->
                // Handle response
                if (response.startsWith("User not found")) {
                    // User not found, handle accordingly
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                } else {
                    // Parse JSON response
                    try {
                        val jsonObject = JSONObject(response)
                        User.name = jsonObject.getString("name")
                        User.email = jsonObject.getString("email")
                        User.city = jsonObject.getString("city")
                        User.country = jsonObject.getString("country")
                        User.contact = jsonObject.getString("contact")
                        User.profileImg = jsonObject.getString("profileImg")
                        User.coverImg = jsonObject.getString("coverImg")
                        callback()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Error Fetching Data", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "Error Fetching Data", Toast.LENGTH_SHORT).show()
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

    }


    private fun imageStore(uri: Uri) {
        var inputStream: InputStream? = null
        try {
            inputStream = contentResolver.openInputStream(uri)
            imgBitmap = BitmapFactory.decodeStream(inputStream)
            val stream = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val imageByte: ByteArray = stream.toByteArray()
            encodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun uploadProfileImg(operation:Int) {
        var tempUrl=""
        if(operation==1){
            tempUrl = "${url}uploadprofileimg.php"
        }else{
            tempUrl = "${url}uploadcoverimg.php"
        }
        val request: StringRequest = object : StringRequest(
            Request.Method.POST,
            tempUrl,
            Response.Listener { response ->

                Log.d("response", response)
            }, Response.ErrorListener { error ->
                Log.d("error", error.toString())
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["userID"] = userID
                params["image"] = encodedImage
                return params
            }
        }
        Volley.newRequestQueue(this).add(request)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            uri = data.data!!

            // Set the selected image to the profileImageView
            if(type==1){

                Picasso.get().load(uri).into(profile_pic)
                imageStore(uri)
                uploadProfileImg(1)

            }
            else if(type==2) {

                Picasso.get().load(uri).into(cover_page)
                imageStore(uri)
                uploadProfileImg(2)

            }
        }
    }
    override fun click_function(txt:String,mentorID: String){
        val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
        nextActivityIntent.putExtra("mentorID", mentorID)
        nextActivityIntent.putExtra("userID", userID)
        nextActivityIntent.putExtra("user_name", txt)
        startActivity(nextActivityIntent)

    }
    override fun change_heart(flag:Int,txt:String,mentorID:String){
TODO()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
