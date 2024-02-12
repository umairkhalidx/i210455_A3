package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class my_profile_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.my_profile)

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


        val editprofile_btn: ImageButton =findViewById(R.id.edit_profile)
        val booked_session_btn: Button =findViewById(R.id.booked_sessions)


        booked_session_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, booked_sessions_activity::class.java)
            startActivity(nextActivityIntent)
        }

        editprofile_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, editprofile_activity::class.java)
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

        back_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }

        // 1- AdapterView: RecyclerView
        val recyclerView_fav : RecyclerView = findViewById(R.id.recyclerview_favmentor)
        recyclerView_fav.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // 2- Data Source: List of  Objects
        var adapter_data_list_fav : ArrayList<recycler_educator_data> = ArrayList()

        val v1  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 1","Lead - Technology Officer","Available")
        val v2  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 2","Lead - Technology Officer"," Not Available")
        val v3  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 3","Lead - Technology Officer","Not Available")
        val v4  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 4","Lead - Technology Officer","Available")
        val v5  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 5","Lead - Technology Officer","Available")
        val v6  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 6","Lead - Technology Officer","Not Available")
        val v7  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 7","Lead - Technology Officer","Available")

        adapter_data_list_fav.add(v1)
        adapter_data_list_fav.add(v2)
        adapter_data_list_fav.add(v3)
        adapter_data_list_fav.add(v4)
        adapter_data_list_fav.add(v5)
        adapter_data_list_fav.add(v6)
        adapter_data_list_fav.add(v7)

        // 3- Adapter
        val adapter_fav = recycler_educator_adapter(adapter_data_list_fav)
        recyclerView_fav.adapter = adapter_fav




        // 1- AdapterView: RecyclerView
        val recyclerView : RecyclerView = findViewById(R.id.recyclerview_myreview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        // 2- Data Source: List of  Objects
        var adapter_data_list_rev : ArrayList<recycler_review_data> = ArrayList()

        val v8  = recycler_review_data("John Cooper","John provided excellent Prototyping Techniques and insights. I highly recommend him!")
        val v9  = recycler_review_data("Emma Phillips","Her tips were valuable. Would love to connect again.")
        val v10  = recycler_review_data("Jane","Impressive Session")
        val v11  = recycler_review_data("Umair","SUIII")

        adapter_data_list_rev.add(v8)
        adapter_data_list_rev.add(v9)
        adapter_data_list_rev.add(v10)
        adapter_data_list_rev.add(v11)

        // 3- Adapter
        val adapter_rev = recycler_review_adapter(adapter_data_list_rev)
        recyclerView.adapter = adapter_rev


    }
}