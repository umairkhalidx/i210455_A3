package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class dialer_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialer)

        val back_btn_dialer: ImageButton =findViewById(R.id.back_btn_dialer)
        val verify_btn: TextView =findViewById(R.id.verify_btn)

        back_btn_dialer.setOnClickListener{
            val nextActivityIntent = Intent(this, get_started_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        verify_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }


    }
}