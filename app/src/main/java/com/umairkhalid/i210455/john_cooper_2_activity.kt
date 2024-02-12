package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class john_cooper_2_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.john_cooper_2)

        val back_btn: ImageButton =findViewById(R.id.back_arrow)
        val feedback_btn: Button =findViewById(R.id.feedback_btn)

        back_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        feedback_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
            startActivity(nextActivityIntent)
        }
    }
}