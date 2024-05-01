package com.umairkhalid.i210455

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class dialer_activity : AppCompatActivity() {
    lateinit var url :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialer)
        url = getString(R.string.url)

        val userData = intent.getParcelableExtra<user_data>("myData")

        val back_btn_dialer: ImageButton =findViewById(R.id.back_btn_dialer)
        val verify_btn: TextView =findViewById(R.id.verify_btn)

        back_btn_dialer.setOnClickListener{
            val nextActivityIntent = Intent(this, get_started_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

//        verify_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, login_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
//        }

        verify_btn.setOnClickListener{
            if (userData != null) {
                SignUp(userData)
            }
        }

    }

    fun SignUp(userData: user_data){

        var tempUrl=url
        tempUrl=tempUrl+"insertcredentials.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, tempUrl,
            Response.Listener { response ->
                getUserID(userData.email) { userID ->
                    if(userID==""){
                        Toast.makeText(this,"Please Try Again",Toast.LENGTH_LONG).show()
                    }else{
                    CreateUser(userData, userID)
                    }
                    // Continue your logic here using the userID
                    // For example, you can call CreateUser(userData) with the retrieved userID

                }

            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this,"Please Try Again",Toast.LENGTH_LONG).show()
                Log.e("API Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()

                params["email"] = userData.email
                params["password"] = userData.password
                return params
            }
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

    }

    fun CreateUser(userData: user_data,userID:String){
        var tempUrl=url
        tempUrl=tempUrl+"insertuser.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, tempUrl,
            Response.Listener { response ->

                Toast.makeText(this,"Signup Successful",Toast.LENGTH_LONG).show()
                var secondActivityIntent = Intent(this, my_profile_activity::class.java)
                secondActivityIntent.putExtra("userID", userID)
                startActivity(secondActivityIntent)
                finish()
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this,"Error While Signing Up",Toast.LENGTH_LONG).show()
                var secondActivityIntent = Intent(this, get_started_activity::class.java)
                startActivity(secondActivityIntent)
                finish()
                Log.e("API Error", "Error occurred while fetching userID: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["userID"] = userID
                params["email"] = userData.email
                params["name"] = userData.name
                params["city"] = userData.city
                params["country"] = userData.country
                params["contact"] = userData.contact
                params["profileImg"] = ""
                params["coverImg"] = ""

                return params
            }
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun getUserID(email: String, callback: (String) -> Unit) {
        var tempUrl=url
        tempUrl=tempUrl+"getuserid.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, tempUrl,
            Response.Listener { response ->
                callback(response)
            },
            Response.ErrorListener { error ->
                // Handle error
                Log.e("API Error", "Error occurred while fetching userID: ${error.message}")
                callback("") // Call callback with empty string in case of error
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                return params
            }
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)
    }
}