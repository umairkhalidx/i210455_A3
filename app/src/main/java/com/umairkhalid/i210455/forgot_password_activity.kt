package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class forgot_password_activity : AppCompatActivity() {
    lateinit var url :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)

        url = getString(R.string.url)

        val send_btn: TextView =findViewById(R.id.send_btn)
        val login_btn: TextView =findViewById(R.id.login_btn)
        val back_btn_forgot: ImageButton =findViewById(R.id.back_btn_forgot)
        val email_txt:TextView=findViewById(R.id.forgot_email_txt)

        val spannableString = SpannableString("Log in")
        // Apply UnderlineSpan to the SpannableString
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Set the SpannableString to the TextView
        login_btn.text = spannableString

        send_btn.setOnClickListener {
            val givenEmail = email_txt.text.toString()
            if(givenEmail.isNotEmpty()){
                // Make HTTP request to PHP script to retrieve email and password
                val tempUrl = "${url}getusercredentials.php" // Assuming the PHP file to retrieve user details is named getUserDetails.php
                val stringRequest = object : StringRequest(
                    Request.Method.POST, tempUrl,
                    Response.Listener { response ->
                        if (response.startsWith("Email")) {
                            // User found, response contains email and password
                            val parts = response.split(", ")
                            val email = parts[0].split(": ")[1]
//                        val password = parts[1].split(": ")[1]

                            // Call newactivity_call function with retrieved email and password
                            newactivity_call(email)
                        } else {
                            // User not found or error occurred
                            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        // Handle error
                        Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                        Log.e("API Error", "Error occurred: ${error.message}")
                    }) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["email"] = givenEmail
                        return params
                    }
                }
                // Add the request to the RequestQueue
                Volley.newRequestQueue(this).add(stringRequest)

            }
            else{
                Toast.makeText(this,"Please Enter an Email",Toast.LENGTH_LONG).show()
            }
        }

        login_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        back_btn_forgot.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }
    }

    fun newactivity_call(email:String){

        val nextActivityIntent = Intent(this, reset_password_activity::class.java)
        nextActivityIntent.putExtra("user_email", email)
        startActivity(nextActivityIntent)
        finish()
    }

}