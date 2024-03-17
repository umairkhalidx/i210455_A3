package com.umairkhalid.i210455

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
import com.google.firebase.auth.FirebaseAuth

class login_activity : AppCompatActivity() {
    private var  mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

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
            sign_in_fun(email,pass)
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
    fun sign_in_fun(email:String,pass:String){

        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
//                    val user = mAuth.currentUser
                    val nextActivityIntent = Intent(this, home_page_activity::class.java)
                    startActivity(nextActivityIntent)
                    finish()
                } else {

                    Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}