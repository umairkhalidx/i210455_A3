package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

class lets_find_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.lets_find)

        val home_btn: ImageButton =findViewById(R.id.home_btn)
        val home_txt: TextView =findViewById(R.id.home_txt)
        val search_btn: ImageButton =findViewById(R.id.search_btn)
        val search_txt: TextView =findViewById(R.id.search_txt)
        val chat_btn: ImageButton =findViewById(R.id.chat_btn)
        val chat_txt: TextView =findViewById(R.id.chat_txt)
        val profile_btn: ImageButton =findViewById(R.id.profile_btn)
        val profile_txt: TextView =findViewById(R.id.profile_txt)
        val plus_btn: ImageButton =findViewById(R.id.plus_btn)
        val back_btn_letsfind: ImageButton =findViewById(R.id.back_btn)


        val searchbar_btn: ImageButton =findViewById(R.id.searchbar_btn)
        val category_btn_1: TextView =findViewById(R.id.textView_1)
        val category_btn_2: TextView =findViewById(R.id.textView_2)
        val category_btn_3: TextView =findViewById(R.id.textView_3)
        val search_result_1: TextView =findViewById(R.id.search_result_1)
        val search_result_2: TextView =findViewById(R.id.search_result_2)
        val search_result_3: TextView =findViewById(R.id.search_result_3)
        val frwd_btn_1: ImageButton =findViewById(R.id.frwd_btn_1)
        val frwd_btn_2: ImageButton =findViewById(R.id.frwd_btn_2)
        val frwd_btn_3: ImageButton =findViewById(R.id.frwd_btn_3)

        searchbar_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            startActivity(nextActivityIntent)
        }

        category_btn_1.setOnClickListener{
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            startActivity(nextActivityIntent)
        }

        category_btn_2.setOnClickListener{
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            startActivity(nextActivityIntent)
        }

        category_btn_3.setOnClickListener{
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            startActivity(nextActivityIntent)
        }

        frwd_btn_1.setOnClickListener{
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            startActivity(nextActivityIntent)
        }

        frwd_btn_2.setOnClickListener{
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            startActivity(nextActivityIntent)
        }

        frwd_btn_3.setOnClickListener{
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            startActivity(nextActivityIntent)
        }

        search_result_1.setOnClickListener{
            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
            startActivity(nextActivityIntent)
        }

        search_result_2.setOnClickListener{
            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
            startActivity(nextActivityIntent)
        }

        search_result_3.setOnClickListener{
            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
            startActivity(nextActivityIntent)
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

        back_btn_letsfind.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }

    }
}