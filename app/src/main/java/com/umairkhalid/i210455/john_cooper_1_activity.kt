package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class john_cooper_1_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.john_cooper_1)

        val back_btn: ImageButton =findViewById(R.id.back_arrow)
        val dropreview_btn: Button =findViewById(R.id.review_btn)
        val community_btn: Button =findViewById(R.id.community_btn)
        val book_session_btn: Button =findViewById(R.id.book_session_btn)

        back_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        dropreview_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, john_cooper_2_activity::class.java)
            startActivity(nextActivityIntent)
        }

        community_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, chat_2_activity::class.java)
            startActivity(nextActivityIntent)
        }

        book_session_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, book_session_activity::class.java)
            startActivity(nextActivityIntent)
        }
    }
}