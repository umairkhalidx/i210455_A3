package com.umairkhalid.i210455

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class call_2_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.call_2)

        val endcall_btn: ImageButton =findViewById(R.id.endcall_btn)

        endcall_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }
    }
}