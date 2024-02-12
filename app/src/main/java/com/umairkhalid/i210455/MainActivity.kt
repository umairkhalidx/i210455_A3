package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val txt_mentor: TextView=findViewById(R.id.txt_mentor)
        val txt_me: TextView=findViewById(R.id.txt_me)
        val connect_btn: TextView=findViewById(R.id.connect_ask_learn)

        txt_mentor.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        txt_me.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }
        connect_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }


    }
}