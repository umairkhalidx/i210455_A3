package com.umairkhalid.i210455

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException

class booked_sessions_activity : AppCompatActivity() , click_listner{
    lateinit var userID: String
    lateinit var url: String
    var sessionsList = mutableListOf<bookedSessionData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.booked_sessions)

        userID = intent.getStringExtra("userID").toString()
        url = getString(R.string.url)

        val back_btn: ImageButton =findViewById(R.id.back_btn)

        back_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }
//        if (!isNetworkAvailable()) {
//            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show()
//
//            val recyclerView_booked : RecyclerView = findViewById(R.id.recycleview_booked_sessions)
//            recyclerView_booked.layoutManager = LinearLayoutManager(this,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//
//            // Initialize SharedPreferences
//            val sharedPref = getSharedPreferences("booked_prefs", Context.MODE_PRIVATE)
//            val json = sharedPref.getString("booked_list", "")
//
//            val gson = Gson()
//            val type = object : TypeToken<ArrayList<recycler_booked_data>>() {}.type
//            val adapter_data_list_booked: ArrayList<recycler_booked_data> = gson.fromJson(json, type)
//
//
//            val adapter_rev = recycler_booked_adapter(adapter_data_list_booked,this@booked_sessions_activity)
//            recyclerView_booked.adapter = adapter_rev
//
//            // Notify your adapter that the data has changed
//            adapter_rev.notifyDataSetChanged()
//
//
//        }

        getsessions(userID){

            val recyclerView_booked : RecyclerView = findViewById(R.id.recycleview_booked_sessions)
            recyclerView_booked.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false
            )

            var adapter_data_list_booked : ArrayList<recycler_booked_data> = ArrayList()

            for(session in sessionsList){
                val mentorData = recycler_booked_data(
                    session.mentorID,
                    session.profileImg,
                    session.name,
                    session.occupation,
                    session.date,
                    session.time
                )
                adapter_data_list_booked.add(mentorData)

            }

            val adapter_rev = recycler_booked_adapter(adapter_data_list_booked,this)
            recyclerView_booked.adapter = adapter_rev

        }

    }

    private fun getsessions(userID:String,callback: () -> Unit){
        val tempUrl = "${url}getbookedsessions.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, tempUrl,
            Response.Listener { response ->
                // Handle response
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val sessionObject = jsonArray.getJSONObject(i)
                        val session = bookedSessionData(
                            sessionObject.getString("userID"),
                            sessionObject.getString("mentorID"),
                            sessionObject.getString("name"),
                            sessionObject.getString("occupation"),
                            sessionObject.getString("date"),
                            sessionObject.getString("time"),
                            sessionObject.getString("profileImg")
                        )
                        sessionsList.add(session)
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