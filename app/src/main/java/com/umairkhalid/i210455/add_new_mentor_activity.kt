package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class add_new_mentor_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_new_mentor)

        val home_btn: ImageButton =findViewById(R.id.home_btn)
        val home_txt: TextView =findViewById(R.id.home_txt)
        val search_btn: ImageButton =findViewById(R.id.search_btn)
        val search_txt: TextView =findViewById(R.id.search_txt)
        val chat_btn: ImageButton =findViewById(R.id.chat_btn)
        val chat_txt: TextView =findViewById(R.id.chat_txt)
        val profile_btn: ImageButton =findViewById(R.id.profile_btn)
        val profile_txt: TextView =findViewById(R.id.profile_txt)
        val plus_btn: ImageButton =findViewById(R.id.plus_btn)
        val back_btn: ImageButton =findViewById(R.id.back_btn)

        val upload_btn : Button =findViewById(R.id.upload_btn)
        val video_text : TextView =findViewById(R.id.video_text)
        val camera_text : TextView =findViewById(R.id.camera_text)
        val video_btn : ImageButton =findViewById(R.id.video_image)
        val camera_btn: ImageButton =findViewById(R.id.camera_image)

        upload_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        video_text.setOnClickListener{
            val nextActivityIntent = Intent(this, camera_video_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        video_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, camera_video_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        camera_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, camera_photo_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        camera_text.setOnClickListener{
            val nextActivityIntent = Intent(this, camera_photo_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }







        home_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        home_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        search_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            startActivity(nextActivityIntent)
        }

        search_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            startActivity(nextActivityIntent)
        }

        chat_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            startActivity(nextActivityIntent)
        }

        chat_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            startActivity(nextActivityIntent)
        }

        profile_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            startActivity(nextActivityIntent)
        }

        profile_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            startActivity(nextActivityIntent)
        }

        back_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }

    }
}