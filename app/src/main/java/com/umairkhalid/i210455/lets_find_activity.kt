package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class lets_find_activity : AppCompatActivity() {
    lateinit var userID: String
    lateinit var url: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.lets_find)

        userID = intent.getStringExtra("userID").toString()
        url = getString(R.string.url)

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
        val search_bar :EditText=findViewById(R.id.searchbar)
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
            val txt=search_bar.text.toString()
            if (txt==null){
                Toast.makeText(this,"Add Suitable Value",Toast.LENGTH_LONG).show()
            }else{
                val nextActivityIntent = Intent(this, search_results_activity::class.java)
                nextActivityIntent.putExtra("search_txt", txt)
                startActivity(nextActivityIntent)
//                finish()
            }

//            val nextActivityIntent = Intent(this, search_results_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        category_btn_1.setOnClickListener{
            val txt:String ="Entrepreneurship"
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            nextActivityIntent.putExtra("search_txt", txt)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
//            finish()
//            val nextActivityIntent = Intent(this, search_results_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        category_btn_2.setOnClickListener{
            val txt:String ="Personal"
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            nextActivityIntent.putExtra("search_txt", txt)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
//            finish()
//            val nextActivityIntent = Intent(this, search_results_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        category_btn_3.setOnClickListener{
            val txt:String ="Education"
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            nextActivityIntent.putExtra("search_txt", txt)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
//            finish()
//            val nextActivityIntent = Intent(this, search_results_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        frwd_btn_1.setOnClickListener{
            val txt:String ="Entrepreneurship"
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            nextActivityIntent.putExtra("search_txt", txt)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
//            finish()
//            val nextActivityIntent = Intent(this, search_results_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        frwd_btn_2.setOnClickListener{
            val txt:String ="Personal"
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            nextActivityIntent.putExtra("search_txt", txt)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
//            finish()
//            val nextActivityIntent = Intent(this, search_results_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        frwd_btn_3.setOnClickListener{
            val txt:String ="Education"
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            nextActivityIntent.putExtra("search_txt", txt)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
//            finish()
//            val nextActivityIntent = Intent(this, search_results_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        search_result_1.setOnClickListener{
            val txt:String ="Entrepreneurship"
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            nextActivityIntent.putExtra("search_txt", txt)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
//            finish()
//            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        search_result_2.setOnClickListener{
            val txt:String ="Personal"
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            nextActivityIntent.putExtra("search_txt", txt)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
//            finish()
//            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
//            startActivity(nextActivityIntent)
        }

        search_result_3.setOnClickListener{
            val txt:String ="Education"
            val nextActivityIntent = Intent(this, search_results_activity::class.java)
            nextActivityIntent.putExtra("search_txt", txt)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
//            finish()
//            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
//            startActivity(nextActivityIntent)
        }







        home_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
            finish()
        }

        home_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, home_page_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
            finish()
        }

        search_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        search_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, lets_find_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        chat_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        chat_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, chats_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        profile_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        profile_txt.setOnClickListener{
            val nextActivityIntent = Intent(this, my_profile_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
            startActivity(nextActivityIntent)
        }

        plus_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, add_new_mentor_activity::class.java)
            nextActivityIntent.putExtra("userID", userID)
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