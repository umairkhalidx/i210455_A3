package com.umairkhalid.i210455

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class book_session_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.book_your_session)

        val back_btn: ImageButton =findViewById(R.id.back_arrow)
        val book_appoint_btn: Button =findViewById(R.id.book_appoint_btn)
        val message_btn: ImageButton =findViewById(R.id.message_btn)
        val audiocall_btn: ImageButton =findViewById(R.id.audiocall_btn)
        val videocall_btn: ImageButton =findViewById(R.id.videocall_btn)
        val time_btn_1 :Button = findViewById(R.id.time_btn_1)
        val time_btn_2 :Button = findViewById(R.id.time_btn_2)
        val time_btn_3 :Button = findViewById(R.id.time_btn_3)

        var c1 =0
        var c2 =0
        var c3 =0

        time_btn_1.setOnClickListener{
            if(c1==0){
                time_btn_1.setBackgroundColor(Color.parseColor("#0b8fac"))
                c1=1
            }
            else{
                time_btn_1.setBackgroundColor(Color.parseColor("#ddefef"))
                c1=0
            }
        }

        time_btn_2.setOnClickListener{
            if(c2==0){
                time_btn_2.setBackgroundColor(Color.parseColor("#0b8fac"))
                c2=1
            }
            else{
                time_btn_2.setBackgroundColor(Color.parseColor("#ddefef"))
                c2=0
            }
        }

        time_btn_3.setOnClickListener{
            if(c3==0){
                time_btn_3.setBackgroundColor(Color.parseColor("#0b8fac"))
                c3=1
            }
            else{
                time_btn_3.setBackgroundColor(Color.parseColor("#ddefef"))
                c3=0
            }
        }


        back_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        book_appoint_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        message_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, chat_1_activity::class.java)
            startActivity(nextActivityIntent)
        }

        audiocall_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, call_1_activity::class.java)
            startActivity(nextActivityIntent)
        }

        videocall_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, call_2_activity::class.java)
            startActivity(nextActivityIntent)
        }


    }
}