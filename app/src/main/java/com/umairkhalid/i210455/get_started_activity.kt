package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.widget.TextView

class get_started_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.getting_started)
        val sign_up_btn: TextView =findViewById(R.id.sign_up_btn)
        val login_btn: TextView =findViewById(R.id.login_btn)

        val spannableString = SpannableString("Log in")
        // Apply UnderlineSpan to the SpannableString
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Set the SpannableString to the TextView
        login_btn.text = spannableString

        sign_up_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, dialer_activity::class.java)
            startActivity(nextActivityIntent)
        }

        login_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }
    }
}