package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.widget.TextView

class login_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login)

        val login_btn: TextView =findViewById(R.id.login_btn)
        val forgot_password_btn: TextView =findViewById(R.id.forgot_password_btn)
        val signUpTextView: TextView = findViewById(R.id.sign_up_btn)

        // Create a SpannableString with the text "Sign Up"
        val spannableString = SpannableString("Sign Up")
        // Apply UnderlineSpan to the SpannableString
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Set the SpannableString to the TextView
        signUpTextView.text = spannableString

        login_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
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
}