package com.umairkhalid.i210455

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth

class login_activity : AppCompatActivity() {

    lateinit var url :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("LoginPref", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user", null)

        if (user_id != null && user_id!="-1") {
            val intent = Intent(this, home_page_activity::class.java)
            intent.putExtra("userID", user_id.toString())
            startActivity(intent)
            finish()
        }
        url = getString(R.string.url)

        setContentView(R.layout.login)

        val login_btn: TextView =findViewById(R.id.login_btn)
        val forgot_password_btn: TextView =findViewById(R.id.forgot_password_btn)
        val signUpTextView: TextView = findViewById(R.id.sign_up_btn)
        val pass_txt=findViewById<TextView>(R.id.login_pass_txt)
        val email_txt=findViewById<EditText>(R.id.login_email_txt)


        // Create a SpannableString with the text "Sign Up"
        val spannableString = SpannableString("Sign Up")
        // Apply UnderlineSpan to the SpannableString
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Set the SpannableString to the TextView
        signUpTextView.text = spannableString

        login_btn.setOnClickListener{
            val email=email_txt.text.toString()
            val pass=pass_txt.text.toString()
            SignIn(email,pass)
        }

        forgot_password_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, forgot_password_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        signUpTextView.setOnClickListener{
            val nextActivityIntent = Intent(this, get_started_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

    }

    fun SignIn(email:String,pass:String){

        val tempUrl = "${url}login.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, tempUrl,
            Response.Listener { response ->
                // Handle response
                if (response == "User not found") {
                    // User not found, handle accordingly
                    // For example, show an error message
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                } else {
                    // User found, response contains userID
                    // You can now proceed with further actions, such as navigating to another screen
                    val userID = response
                    // Example: Navigate to home screen with userID
                    val sharedPreferences = getSharedPreferences("LoginPref", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("user", userID)
                    editor.apply()

                    val intent = Intent(this, home_page_activity::class.java)
                    intent.putExtra("userID", userID)
                    startActivity(intent)
                    finish()
                }
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                Log.e("API Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                params["password"] = pass
                return params
            }
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

    }
}