package com.umairkhalid.i210455

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject

class chats_activity : AppCompatActivity() , click_listner,click_listner_community{
    lateinit var userID: String
    lateinit var url: String
    var messagementorIDs = mutableListOf<String>()
    var communitymentorIDs = mutableListOf<String>()
    lateinit var MessageMentor: mentorData
    lateinit var CommunityMentor: mentorData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.chats)
        userID = intent.getStringExtra("userID").toString()
        url = getString(R.string.url)

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
            nextActivityIntent.putExtra("userID",userID)
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


//        if (!isNetworkAvailable()) {
//            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()
//
//            // 1- AdapterView: RecyclerView
//            val recyclerView : RecyclerView = findViewById(R.id.chats_recycleview)
//            recyclerView.layoutManager = LinearLayoutManager(this,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//
//            val sharedPref_1 = getSharedPreferences("chats_prefs", Context.MODE_PRIVATE)
//            val json_1 = sharedPref_1.getString("chats_list", "")
//
//            val gson_1 = Gson()
//            val type_1 = object : TypeToken<ArrayList<chats_recycler_data>>() {}.type
//            val adapter_data_list: ArrayList<chats_recycler_data> = gson_1.fromJson(json_1, type_1)
//
//
//            val adapter = chats_recycler_adapter(adapter_data_list,this@chats_activity)
//            recyclerView.adapter = adapter
//
//            // Notify your adapter that the data has changed
//            adapter.notifyDataSetChanged()
//
//
//            // 1- AdapterView: RecyclerView
//            val recyclerView_community : RecyclerView = findViewById(R.id.chats_recyclerview_community)
//            recyclerView_community.layoutManager = LinearLayoutManager(this,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//
//            val sharedPref_2 = getSharedPreferences("chats_community_prefs", Context.MODE_PRIVATE)
//            val json_2 = sharedPref_2.getString("community_list", "")
//
//            val gson_2 = Gson()
//            val type_2 = object : TypeToken<ArrayList<chats_recycler_community_data>>() {}.type
//            val adapter_data_list_community: ArrayList<chats_recycler_community_data> = gson_2.fromJson(json_2, type_2)
//
//
//
//            val adapter_3 = chats_recycler_community_adapter(adapter_data_list_community,this@chats_activity)
//            recyclerView_community.adapter = adapter_3
//
//            // Notify your adapter that the data has changed
//            adapter_3.notifyDataSetChanged()
//
//        }

        getmessagementors(userID){
            val recyclerView : RecyclerView = findViewById(R.id.chats_recycleview)
            recyclerView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false
            )

            var adapter_data_list : ArrayList<chats_recycler_data> = ArrayList()
            MessageMentor = mentorData("", "", "", "", "", "", "", "")

            for( mentor in messagementorIDs){
                getMessageMentors(mentor.toString()){
                    val mentorData = chats_recycler_data(
                        MessageMentor.mentorID,
                        MessageMentor.profileImg,
                        MessageMentor.name
                    )
                    adapter_data_list.add(mentorData)
                    val adapter = chats_recycler_adapter(adapter_data_list,this)
                    recyclerView.adapter = adapter
                }
            }

        }

            // 1- AdapterView: RecyclerView



        // 2- Data Source: List of  Objects


        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid.toString()
        val messagesRef = database.reference.child("users").child(userId).child("messages")


//        messagesRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (conversationSnapshot in snapshot.children) {
//                    for (messageSnapshot in conversationSnapshot.children) {
//                        val temp_user_Id = messageSnapshot.child("userId").getValue(String::class.java).toString()
//                        val conversationName = conversationSnapshot.key
//                        val mentorName = conversationName?.substringAfter("_")
//
//                        if (temp_user_Id == userId) {
//
//                            val database_2 = FirebaseDatabase.getInstance()
//                            val mentorsRef = database_2.getReference("mentors")
//
//                            val query = mentorsRef.orderByChild("name").equalTo(mentorName)
//
//                            query.addListenerForSingleValueEvent(object : ValueEventListener {
//                                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                    for (mentorSnapshot in dataSnapshot.children) {
//                                        val name = mentorSnapshot.child("name").getValue(String::class.java)
//                                        val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)
//
////                                        if (name != null && adapter_data_list.none { it.title == name }) {
////                                            val mentorData = chats_recycler_data(profilePicUrl, name)
////                                            adapter_data_list.add(mentorData)
////                                        }
//
//                                        // Check if all required fields are present
//                                        if (name != null) {
//                                            val mentorData = chats_recycler_data(
//                                                profilePicUrl,
//                                                name
//                                            )
//                                            var exists = false
//
//                                            for (data in adapter_data_list) {
//                                                if (data.title == name) {
//                                                    exists = true
//                                                    break
//                                                }
//                                            }
//
//                                            if (!exists) {
//                                                adapter_data_list.add(mentorData)
//                                            }
//                                        }
//                                    }
//                                    val adapter = chats_recycler_adapter(adapter_data_list,this@chats_activity)
//                                    recyclerView.adapter = adapter
//
//                                    // Notify your adapter that the data has changed
//                                     adapter.notifyDataSetChanged()
//
//                                    val gson = Gson()
//                                    val json = gson.toJson(adapter_data_list)
//                                    val sharedPref = getSharedPreferences("chats_prefs", Context.MODE_PRIVATE)
//                                    val editor = sharedPref.edit()
//                                    editor.putString("chats_list", json)
//                                    editor.apply()
//
//                                }
//
//                                override fun onCancelled(databaseError: DatabaseError) {
//                                    // Handle error
//                                    Toast.makeText(this@chats_activity,"Unable to Fetch Mentor Data",Toast.LENGTH_LONG).show()
//                                }
//                            })
//                            break;
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle errors here
//            }
//        })

//        val v1  = chats_recycler_data(R.drawable.rectangle_blank,"John Cooper")
//        val v2  = chats_recycler_data(R.drawable.rectangle_blank,"Jack Watson")
//        val v3  = chats_recycler_data(R.drawable.rectangle_blank,"Emma Philips")
//
////        val v1  = chats_recycler_data(R.drawable.rectangle_blank,"John Cooper","1 New Message")
////        val v2  = chats_recycler_data(R.drawable.rectangle_blank,"Jack Watson","No New Message")
////        val v3  = chats_recycler_data(R.drawable.rectangle_blank,"Emma Philips","No New Message")
//
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//
//        // 3- Adapter
//        val adapter = chats_recycler_adapter(adapter_data_list,this)
//        recyclerView.adapter = adapter



        getcommunitymentors(){
            // 1- AdapterView: RecyclerView
            val recyclerView_community : RecyclerView = findViewById(R.id.chats_recyclerview_community)
            recyclerView_community.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            // 2- Data Source: List of  Objects
            var adapter_data_list_community : ArrayList<chats_recycler_community_data> = ArrayList()

            CommunityMentor = mentorData("", "", "", "", "", "", "", "")

            for( mentor in communitymentorIDs){
                getCommunityMentors(mentor.toString()){
                    val mentorData = chats_recycler_community_data(
                        CommunityMentor.mentorID,
                        CommunityMentor.profileImg,
                        CommunityMentor.name
                    )
                    adapter_data_list_community.add(mentorData)
                    val adapter_2 = chats_recycler_community_adapter(adapter_data_list_community,this@chats_activity)
                    recyclerView_community.adapter = adapter_2
                }
            }
        }



        val database_2 = FirebaseDatabase.getInstance()
        val messagesRef_2 = database_2.reference.child("community")
        val curr = FirebaseAuth.getInstance().currentUser
        val userId_2 = curr?.uid.toString()

//        messagesRef_2.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (conversationSnapshot in snapshot.children) {
//                    for (messageSnapshot in conversationSnapshot.children) {
//                        val temp_user_Id_2 = messageSnapshot.child("userId").getValue(String::class.java).toString()
//                        val conversationName = conversationSnapshot.key
//                        val mentorName = conversationName?.substringAfter("_")
//
//                        if (temp_user_Id_2 == userId_2) {
//
//                            val database_2 = FirebaseDatabase.getInstance()
//                            val mentorsRef = database_2.getReference("mentors")
//
//                            val query = mentorsRef.orderByChild("name").equalTo(mentorName)
//
//                            query.addListenerForSingleValueEvent(object : ValueEventListener {
//                                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                    for (mentorSnapshot in dataSnapshot.children) {
//                                        val name = mentorSnapshot.child("name").getValue(String::class.java)
//                                        val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)
//
////                                        if (name != null && adapter_data_list.none { it.title == name }) {
////                                            val mentorData = chats_recycler_data(profilePicUrl, name)
////                                            adapter_data_list.add(mentorData)
////                                        }
//
//                                        // Check if all required fields are present
//                                        if (name != null) {
//                                            val mentorData = chats_recycler_community_data(
//                                                profilePicUrl,
//                                                name
//                                            )
//                                            var exists = false
//
//                                            for (data in adapter_data_list_community) {
//                                                if (data.title == name) {
//                                                    exists = true
//                                                    break
//                                                }
//                                            }
//
//                                            if (!exists) {
//                                                adapter_data_list_community.add(mentorData)
//                                            }
//                                        }
//                                    }
//                                    val adapter_2 = chats_recycler_community_adapter(adapter_data_list_community,this@chats_activity)
//                                    recyclerView_community.adapter = adapter_2
//
//                                    // Notify your adapter that the data has changed
//                                    adapter_2.notifyDataSetChanged()
//
//                                    val gson = Gson()
//                                    val json = gson.toJson(adapter_data_list_community)
//                                    val sharedPref = getSharedPreferences("chats_community_prefs", Context.MODE_PRIVATE)
//                                    val editor = sharedPref.edit()
//                                    editor.putString("community_list", json)
//                                    editor.apply()
//                                }
//
//                                override fun onCancelled(databaseError: DatabaseError) {
//                                    // Handle error
//                                    Toast.makeText(this@chats_activity,"Unable to Community Data",Toast.LENGTH_LONG).show()
//                                }
//                            })
//                            break;
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle errors here
//            }
//        })

    }

    private fun getmessagementors(userID :String,callback: () -> Unit){
        val tempUrl = "${url}getmessagementors.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, tempUrl,
            Response.Listener { response ->
                try {
                    // Parse the JSON response
                    val jsonObject = JSONObject(response)
                    val mentorIDsArray = jsonObject.getJSONArray("mentorIDs")

                    // Extract mentorIDs from the JSON array

                    for (i in 0 until mentorIDsArray.length()) {
                        messagementorIDs.add(mentorIDsArray.getString(i))
                    }
                    callback()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle error
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

    private fun getcommunitymentors(callback: () -> Unit){
        val tempUrl = "${url}getcommunitymentors.php"
        // Create a JSON object request
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, tempUrl, null,
            Response.Listener { response ->
                // Parse the JSON response
                val mentorIDs = response.getJSONArray("mentorIDs")
                for (i in 0 until mentorIDs.length()) {
                    communitymentorIDs.add(mentorIDs.getString(i))
                }
                callback()
            },
            Response.ErrorListener { error ->
                // Handle error
                error.printStackTrace()
                callback()
            }
        )

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    private fun getMessageMentors(mentorID: String, callback: () -> Unit) {

        val tempURL = "${url}getmentordata_id.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, tempURL,
            Response.Listener { response ->
                // Handle response
                if (response.startsWith("Mentor not found")) {
                    // User not found, handle accordingly
                    Toast.makeText(this, "Mentor not found", Toast.LENGTH_SHORT).show()
                } else {
                    // Parse JSON response
                    try {
                        val jsonObject = JSONObject(response)
                        MessageMentor.mentorID = jsonObject.getString("mentorID")
                        MessageMentor.name = jsonObject.getString("name")
                        MessageMentor.occupation = jsonObject.getString("occupation")
                        MessageMentor.description = jsonObject.getString("description")
                        MessageMentor.price = jsonObject.getString("price")
                        MessageMentor.profileImg = jsonObject.getString("profileImg")
                        MessageMentor.status = jsonObject.getString("status")
                        MessageMentor.favourite = jsonObject.getString("favourite")
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
                params["mentorID"] = mentorID
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

    }

    private fun getCommunityMentors(mentorID: String, callback: () -> Unit) {

        val tempURL = "${url}getmentordata_id.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, tempURL,
            Response.Listener { response ->
                // Handle response
                if (response.startsWith("Mentor not found")) {
                    // User not found, handle accordingly
                    Toast.makeText(this, "Mentor not found", Toast.LENGTH_SHORT).show()
                } else {
                    // Parse JSON response
                    try {
                        val jsonObject = JSONObject(response)
                        CommunityMentor.mentorID = jsonObject.getString("mentorID")
                        CommunityMentor.name = jsonObject.getString("name")
                        CommunityMentor.occupation = jsonObject.getString("occupation")
                        CommunityMentor.description = jsonObject.getString("description")
                        CommunityMentor.price = jsonObject.getString("price")
                        CommunityMentor.profileImg = jsonObject.getString("profileImg")
                        CommunityMentor.status = jsonObject.getString("status")
                        CommunityMentor.favourite = jsonObject.getString("favourite")
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
                params["mentorID"] = mentorID
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

    }

    override fun click_function(txt:String,mentorID: String){
        val nextActivityIntent = Intent(this, chat_1_activity::class.java)
        nextActivityIntent.putExtra("mentorID", mentorID)
        nextActivityIntent.putExtra("userID", userID)
        nextActivityIntent.putExtra("MENTOR_NAME", txt)
        startActivity(nextActivityIntent)

    }

    override fun change_heart(flag:Int,txt:String,mentorID:String) {
        TODO("Not yet implemented")
    }

    override fun chat_community_click_function(txt:String,mentorID:String){
        val nextActivityIntent = Intent(this, chat_2_activity::class.java)
        nextActivityIntent.putExtra("userID", userID)
        nextActivityIntent.putExtra("mentorID", mentorID)
        nextActivityIntent.putExtra("MENTOR_NAME", txt)
        startActivity(nextActivityIntent)

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}