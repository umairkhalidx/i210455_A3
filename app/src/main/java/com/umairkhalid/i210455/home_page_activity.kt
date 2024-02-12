package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class home_page_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home_page)

        val home_btn: ImageButton =findViewById(R.id.home_btn)
        val home_txt: TextView =findViewById(R.id.home_txt)
        val search_btn: ImageButton =findViewById(R.id.search_btn)
        val search_txt: TextView =findViewById(R.id.search_txt)
        val chat_btn: ImageButton =findViewById(R.id.chat_btn)
        val chat_txt: TextView =findViewById(R.id.chat_txt)
        val profile_btn: ImageButton =findViewById(R.id.profile_btn)
        val profile_txt: TextView =findViewById(R.id.profile_txt)
        val plus_btn: ImageButton =findViewById(R.id.plus_btn)
        val notifications_btn: ImageButton =findViewById(R.id.notifications_btn)


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

        notifications_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, notifications_activity::class.java)
            startActivity(nextActivityIntent)
        }


        // 1- AdapterView: RecyclerView
        val recyclerView_top : RecyclerView = findViewById(R.id.recyclerview_educator_top)
        recyclerView_top.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        // 2- Data Source: List of  Objects
        var adapter_data_list_top : ArrayList<recycler_educator_data> = ArrayList()

        val v1  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 1","Lead - Technology Officer","Available")
        val v2  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 2","Lead - Technology Officer"," Not Available")
        val v3  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 3","Lead - Technology Officer","Not Available")
        val v4  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 4","Lead - Technology Officer","Available")
        val v5  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 5","Lead - Technology Officer","Available")
        val v6  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 6","Lead - Technology Officer","Not Available")
        val v7  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 7","Lead - Technology Officer","Available")

        adapter_data_list_top.add(v1)
        adapter_data_list_top.add(v2)
        adapter_data_list_top.add(v3)
        adapter_data_list_top.add(v4)
        adapter_data_list_top.add(v5)
        adapter_data_list_top.add(v6)
        adapter_data_list_top.add(v7)

        // 3- Adapter
        val adapter_top = recycler_educator_adapter(adapter_data_list_top)
        recyclerView_top.adapter = adapter_top




        // 1- AdapterView: RecyclerView
        val recyclerView_edu : RecyclerView = findViewById(R.id.recyclerview_educator_edu)
        recyclerView_edu.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        // 2- Data Source: List of  Objects
        var adapter_data_list_edu : ArrayList<recycler_educator_data> = ArrayList()

        val v8  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 1","Lead - Technology Officer","Available")
        val v9  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 2","Lead - Technology Officer"," Not Available")
        val v10  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 3","Lead - Technology Officer","Not Available")
        val v11  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 4","Lead - Technology Officer","Available")
        val v12  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 5","Lead - Technology Officer","Available")
        val v13  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 6","Lead - Technology Officer","Not Available")
        val v14  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 7","Lead - Technology Officer","Available")

        adapter_data_list_edu.add(v8)
        adapter_data_list_edu.add(v9)
        adapter_data_list_edu.add(v10)
        adapter_data_list_edu.add(v11)
        adapter_data_list_edu.add(v12)
        adapter_data_list_edu.add(v13)
        adapter_data_list_edu.add(v14)

        // 3- Adapter
        val adapter_edu = recycler_educator_adapter(adapter_data_list_edu)
        recyclerView_edu.adapter = adapter_edu



        // 1- AdapterView: RecyclerView
        val recyclerView_rec : RecyclerView = findViewById(R.id.recyclerview_educator_rec)
        recyclerView_rec.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        // 2- Data Source: List of  Objects
        var adapter_data_list_rec : ArrayList<recycler_educator_data> = ArrayList()

        val v15  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 1","Lead - Technology Officer","Available")
        val v16  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 2","Lead - Technology Officer"," Not Available")
        val v17  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 3","Lead - Technology Officer","Not Available")
        val v18  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 4","Lead - Technology Officer","Available")
        val v19  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 5","Lead - Technology Officer","Available")
        val v20  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 6","Lead - Technology Officer","Not Available")
        val v21  = recycler_educator_data(R.drawable.rectangle_blank,"Sample 7","Lead - Technology Officer","Available")

        adapter_data_list_rec.add(v15)
        adapter_data_list_rec.add(v9)
        adapter_data_list_rec.add(v10)
        adapter_data_list_rec.add(v11)
        adapter_data_list_rec.add(v12)
        adapter_data_list_rec.add(v13)
        adapter_data_list_rec.add(v14)

        // 3- Adapter
        val adapter_rec = recycler_educator_adapter(adapter_data_list_rec)
        recyclerView_rec.adapter = adapter_rec



    }
}