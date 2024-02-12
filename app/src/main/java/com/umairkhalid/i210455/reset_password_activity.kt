package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.widget.ImageButton
import android.widget.TextView

class reset_password_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.reset_password)

        val reset_btn: TextView =findViewById(R.id.reset_btn)
        val login_btn: TextView =findViewById(R.id.login_btn)
        val back_btn_reset: ImageButton =findViewById(R.id.back_btn_reset)

        val spannableString = SpannableString("Log in")
        // Apply UnderlineSpan to the SpannableString
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Set the SpannableString to the TextView
        login_btn.text = spannableString

        reset_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
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
}