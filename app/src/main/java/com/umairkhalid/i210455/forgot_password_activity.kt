package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class forgot_password_activity : AppCompatActivity() {
    private var  mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.forgot_password)

        val send_btn: TextView =findViewById(R.id.send_btn)
        val login_btn: TextView =findViewById(R.id.login_btn)
        val back_btn_forgot: ImageButton =findViewById(R.id.back_btn_forgot)
        val email_txt:TextView=findViewById(R.id.forgot_email_txt)

        val spannableString = SpannableString("Log in")
        // Apply UnderlineSpan to the SpannableString
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Set the SpannableString to the TextView
        login_btn.text = spannableString

        send_btn.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val my_ref = database.getReference("credentials")
            val email = email_txt.text.toString()
//            val new_email = email.replace(".", ",")


            my_ref.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Email exists in the database
                            val password = dataSnapshot.children.firstOrNull()?.child("password")?.value.toString()
                            newactivity_call(email,password)
//                        Toast.makeText(this@forgot_password_activity, "Email found.", Toast.LENGTH_SHORT).show()

                        } else {
                            // Email does not exist in the database
                            Toast.makeText(this@forgot_password_activity, "Email not found.", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Error occurred while fetching data
                        Toast.makeText(
                            this@forgot_password_activity,
                            "General Processing Error",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                })

        }


//            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val result = task.result?.signInMethods
//                    Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
//
//                    if (result.isNullOrEmpty()) {
//                        Toast.makeText(this, "Email not found.", Toast.LENGTH_SHORT).show()
//                    }
//                    else {
//                        val user: FirebaseUser? = mAuth.currentUser
//                        val userId = user?.uid
//                        if (userId != null) {
//                            Toast.makeText(this, "User ID: $userId", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//                else {
//                    Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
//
//                }
//            }

        login_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        back_btn_forgot.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }
    }

    fun newactivity_call(email:String,password:String){

        val nextActivityIntent = Intent(this, reset_password_activity::class.java)
        nextActivityIntent.putExtra("user_email", email)
        nextActivityIntent.putExtra("user_pass", password)
        startActivity(nextActivityIntent)
        finish()
    }

}