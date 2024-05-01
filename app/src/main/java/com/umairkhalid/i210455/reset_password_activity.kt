package com.umairkhalid.i210455

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class reset_password_activity : AppCompatActivity() {
    lateinit var url :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password)

        url = getString(R.string.url)
        val userEmail = intent.getStringExtra("user_email")

        val reset_btn: TextView =findViewById(R.id.reset_btn)
        val login_btn: TextView =findViewById(R.id.login_btn)
        val back_btn_reset: ImageButton =findViewById(R.id.back_btn_reset)
        val newpass :EditText=findViewById(R.id.reset_newpass_txt)
        val repass :EditText=findViewById(R.id.reset_repass_txt)

        val spannableString = SpannableString("Log in")
        // Apply UnderlineSpan to the SpannableString
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Set the SpannableString to the TextView
        login_btn.text = spannableString


        reset_btn.setOnClickListener{

            val new_txt=newpass.text.toString().trim()
            val re_txt=repass.text.toString().trim()

            if (new_txt.isNotEmpty() && re_txt.isNotEmpty() )
            {
                if(new_txt.length < 8  && re_txt.length < 8   ){
                    Toast.makeText(this,"Password too small",Toast.LENGTH_LONG).show()
                }
                else if(new_txt != re_txt ){
                    Toast.makeText(this,"Passwords Do not Match",Toast.LENGTH_LONG).show()
                }
                else{
                    val email=userEmail.toString()
                    resetPassword(email,new_txt)
                }

            }else{
                Toast.makeText(this,"Please fill in all fields",Toast.LENGTH_LONG).show()
            }

        }

        login_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        back_btn_reset.setOnClickListener{
            val nextActivityIntent = Intent(this, forgot_password_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

    }
    fun resetPassword(email: String, newPassword: String) {
        // Make HTTP request to PHP script to reset password
        val tempUrl = "${url}resetpassword.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, tempUrl,
            Response.Listener { response ->
                // Handle response
                Toast.makeText(this, "Reset Successful", Toast.LENGTH_SHORT).show()
                val nextActivityIntent = Intent(this, login_activity::class.java)
                startActivity(nextActivityIntent)
                finish()
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                Log.e("API Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                params["newPassword"] = newPassword
                return params
            }
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)
    }

}