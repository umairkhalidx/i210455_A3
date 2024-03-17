package com.umairkhalid.i210455

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class dialer_activity : AppCompatActivity() {
    private var  mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialer)

        val userData = intent.getParcelableExtra<user_data>("myData")

        val back_btn_dialer: ImageButton =findViewById(R.id.back_btn_dialer)
        val verify_btn: TextView =findViewById(R.id.verify_btn)

        back_btn_dialer.setOnClickListener{
            val nextActivityIntent = Intent(this, get_started_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

//        verify_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, login_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
//        }

        verify_btn.setOnClickListener{
            if (userData != null) {
                signup_func(userData)
            }
        }

    }

    fun signup_func(userData: user_data){
        mAuth.createUserWithEmailAndPassword(userData.email, userData.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "createUserWithEmail:success")

                    val database = FirebaseDatabase.getInstance()
                    var my_ref = database.getReference("users")
                    val curr = mAuth.currentUser
                    val id= curr?.uid.toString()

                    my_ref.child(id).setValue(null)
                    my_ref.child(id).child("name").setValue(userData.name)
                    my_ref.child(id).child("email").setValue(userData.email)
//                    my_ref.child(id).child("password").setValue(userData.password)
                    my_ref.child(id).child("country").setValue(userData.country)
                    my_ref.child(id).child("city").setValue(userData.city)
                    my_ref.child(id).child("contact").setValue(userData.contact)

                    val new_email = userData.email.replace(".", ",")
                    my_ref = database.getReference("credentials")
                    my_ref.child(new_email).setValue(null)
                    my_ref.child(new_email).child("email").setValue(userData.email)
                    my_ref.child(new_email).child("password").setValue(userData.password)

                    Toast.makeText(this,"Signup Successful",Toast.LENGTH_LONG).show()
                    var secondActivityIntent = Intent(this, my_profile_activity::class.java)
                    startActivity(secondActivityIntent)
                    finish()

                } else {
                    Toast.makeText(this,"Error While Signing Up",Toast.LENGTH_LONG).show()
                    var secondActivityIntent = Intent(this, get_started_activity::class.java)
                    startActivity(secondActivityIntent)
                    finish()
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                }
            }
    }
}