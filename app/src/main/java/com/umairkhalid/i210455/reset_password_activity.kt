package com.umairkhalid.i210455

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class reset_password_activity : AppCompatActivity() {
    private var  mAuth = FirebaseAuth.getInstance();
    var flag:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.reset_password)
        val userEmail = intent.getStringExtra("user_email")
        val oldpass = intent.getStringExtra("user_pass")

        val reset_btn: TextView =findViewById(R.id.reset_btn)
        val login_btn: TextView =findViewById(R.id.login_btn)
        val back_btn_reset: ImageButton =findViewById(R.id.back_btn_reset)
        val newpass :EditText=findViewById(R.id.reset_newpass_txt)
        val repass :EditText=findViewById(R.id.reset_repass_txt)

        val spannableString = SpannableString("Log in")
        // Apply UnderlineSpan to the SpannableString
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Set the SpannableString to the TextView
        login_btn.text = spannableString


        reset_btn.setOnClickListener{

            val new_txt=newpass.text.toString().trim()
            val re_txt=repass.text.toString().trim()

            if (new_txt.isNotEmpty() && re_txt.isNotEmpty() )
            {
                if(new_txt.length < 8  && re_txt.length < 8   ){
                    Toast.makeText(this,"Password too small",Toast.LENGTH_LONG).show()
                }
                else if(new_txt != re_txt ){
                    Toast.makeText(this,"Passwords Do not Match",Toast.LENGTH_LONG).show()
                }
                else{
                    val email=userEmail.toString()
                    val oldpassword=oldpass.toString()
                    resetPassword(email,new_txt,oldpassword)
                }

            }else{
                Toast.makeText(this,"Please fill in all fields",Toast.LENGTH_LONG).show()
            }

        }

        login_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        back_btn_reset.setOnClickListener{
            val nextActivityIntent = Intent(this, forgot_password_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }


////////////////////////////////////////////////////////////////////////////
        sign_in_fun(userEmail.toString(),oldpass.toString());       //Important Step
///////////////////////////////////////////////////////////////////////////


    }
    fun resetPassword(email: String, newPassword: String,oldpassword:String) {

        if(flag==false){
            Toast.makeText(this, "Unable to Change Passsword Try Again", Toast.LENGTH_SHORT).show()
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        val credential: AuthCredential = EmailAuthProvider.getCredential(email, oldpassword)
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        user?.reauthenticate(credential)?.addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    user.updatePassword(newPassword).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val database = FirebaseDatabase.getInstance()
                            val my_ref = database.getReference("credentials")
                            val new_email = email.replace(".", ",")
                            my_ref.child(new_email).child("password").setValue(newPassword)
                            FirebaseAuth.getInstance().signOut()

                            Toast.makeText(this, "Password Updated Successfully", Toast.LENGTH_LONG).show()
                            val nextActivityIntent = Intent(this, login_activity::class.java)
                            startActivity(nextActivityIntent)
                            finish()
                        } else
                            Toast.makeText(this, "Please Try Again", Toast.LENGTH_LONG).show()
                    }
                }
                else -> {
                    Toast.makeText(this, "General Processing Error", Toast.LENGTH_LONG).show()
                }

            }
        }


        //Sends a link on EMail to reset password

//        mAuth.sendPasswordResetEmail(email)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    mAuth.confirmPasswordReset("password_reset_code", newPassword)
//                        .addOnCompleteListener { resetTask ->
//                            if (resetTask.isSuccessful) {
//                                Toast.makeText(this, "Password Updated Successfully", Toast.LENGTH_LONG).show()
//                                val nextActivityIntent = Intent(this, login_activity::class.java)
//                                startActivity(nextActivityIntent)
//                                finish()
//
//                            } else {
//                                Toast.makeText(this, "Please Try Again", Toast.LENGTH_LONG).show()
//                            }
//                        }
//                } else {
//                    Toast.makeText(this,"Please Try Again",Toast.LENGTH_LONG).show()
//                }
//            }
    }
    fun sign_in_fun(email:String,pass:String){
        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    flag=true
                    Log.d("TAG", "signInWithEmail:success")
                } else {
                    flag=false
                    Log.d("TAG", "signInWithEmail:fail")
                }
            }
    }
}


//fun resetPassword(email: String, newPassword: String,oldpassword:String) {
//
//    mAuth.signInWithEmailAndPassword(email, oldpassword)
//        .addOnCompleteListener(this) { task ->
//            if (task.isSuccessful) {
//
//                var credential: AuthCredential
//                var user: FirebaseUser?
//                while(true){
//                    credential = EmailAuthProvider.getCredential(email, oldpassword)
//                    user = FirebaseAuth.getInstance().currentUser
//                    val temp= user?.uid?.toString()
//
//                    if(temp!=null){
//                        break;
//                    }
//                }
//                user?.reauthenticate(credential)?.addOnCompleteListener { task ->
//                    when {
//                        task.isSuccessful -> {
//                            user.updatePassword(newPassword).addOnCompleteListener {
//                                if (it.isSuccessful) {
//
//                                    val database = FirebaseDatabase.getInstance()
//                                    val my_ref = database.getReference("credentials")
//                                    val new_email = email.replace(".", ",")
//                                    my_ref.child(new_email).child("password")
//                                        .setValue(newPassword)
//                                    FirebaseAuth.getInstance().signOut()
//
//                                    Toast.makeText(
//                                        this,
//                                        "Password Updated Successfully",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//
//                                    val nextActivityIntent =
//                                        Intent(this, login_activity::class.java)
//                                    startActivity(nextActivityIntent)
//                                    finish()
//                                } else
//                                    Toast.makeText(this, "Please Try Again", Toast.LENGTH_LONG)
//                                        .show()
//                            }
//                        }
//
//                        else -> {
//                            Toast.makeText(this, "General Processing Error", Toast.LENGTH_LONG)
//                                .show()
//                        }
//
//                    }
//                }
//
//            } else {
//                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
//                val nextActivityIntent = Intent(this, login_activity::class.java)
//                startActivity(nextActivityIntent)
//                finish()
//            }
//        }
//
//
//}