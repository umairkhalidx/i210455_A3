package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class editprofile_activity : AppCompatActivity() {
    private var  mAuth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.edit_profile)

        val back_btn: ImageButton =findViewById(R.id.back_btn)
        val update_profile_btn: Button =findViewById(R.id.update_profile_btn)

        val name_input: TextView =findViewById(R.id.input_name)
        val email_input: TextView =findViewById(R.id.input_email)
        val contact_input: TextView =findViewById(R.id.input_contact)

        val city_spinner: Spinner = findViewById(R.id.input_city)
        city_spinner.prompt = "Select City"
        val cities = arrayOf("Select City","Rawalpindi", "Islamabad", "Lahore")

        val country_spinner: Spinner = findViewById(R.id.input_country)
        country_spinner.prompt = "Select Country"
        val countries = arrayOf("Select Country","Pakistan")

        val city_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        city_spinner.adapter = city_adapter

        city_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Set the selected item text to Spinner's prompt
                city_spinner.prompt = cities[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                city_spinner.prompt = "Select City"
            }
        }

        val country_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        country_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        country_spinner.adapter = country_adapter

        country_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Set the selected item text to Spinner's prompt
                country_spinner.prompt = countries[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                country_spinner.prompt = "Select Country"
            }
        }

        update_profile_btn.setOnClickListener{
            //            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)

            val name=name_input.text.toString().trim()
            val email=email_input.text.toString().trim()
            val contact=contact_input.text.toString().trim()
            val country= country_spinner.selectedItem.toString().trim()
            val city= city_spinner.selectedItem.toString().trim()


            if (name.isNotEmpty() && email.isNotEmpty() && country.isNotEmpty() && contact.isNotEmpty() && city.isNotEmpty() )
            {
                val emailPattern = "[a-zA-Z0-9._-]+@gmail.com"

                if(!email.matches(emailPattern.toRegex())){
                    Toast.makeText(this,"Invalid Email", Toast.LENGTH_LONG).show()
                }
                else if(contact.length.toInt() != 11 || !contact.matches("^[0-9]+\$".toRegex())){
                    Toast.makeText(this,"Invalid Contact Info", Toast.LENGTH_LONG).show()
                }
                else if(country=="Select Country"){
                    Toast.makeText(this,"Invalid Country", Toast.LENGTH_LONG).show()
                }
                else if(city=="Select City"){
                    Toast.makeText(this,"Invalid City", Toast.LENGTH_LONG).show()
                }
                else{

                    val database = FirebaseDatabase.getInstance()
                    var my_ref = database.getReference("users")

                    val curr = mAuth.currentUser
                    val id= curr?.uid.toString()
                    val user_email = curr?.email.toString()

                    if(email!=user_email){
                        Toast.makeText(this,"Enter your Current Email", Toast.LENGTH_LONG).show()
                    }else{

                        my_ref.child(id).child("name").setValue(name)
                        my_ref.child(id).child("email").setValue(email)
//                        my_ref.child(id).child("password").setValue(userData.password)
                        my_ref.child(id).child("country").setValue(country)
                        my_ref.child(id).child("city").setValue(city)
                        my_ref.child(id).child("contact").setValue(contact)


                        Toast.makeText(this,"Data Updated Successfully",Toast.LENGTH_LONG).show()
                        onBackPressed()
                        finish()
                    }

                }

            }
        }

        back_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, home_page_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()

        }
    }

}




//
//val temp_ref = database.getReference("credentials")
//
//temp_ref.orderByChild("email").equalTo(user_email)
//.addListenerForSingleValueEvent(object : ValueEventListener {
//    override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//        if (dataSnapshot.exists()) {
//            // Email exists in the database
//            val password = dataSnapshot.children.firstOrNull()?.child("password")?.value.toString()
//
//            sign_in_fun(user_email,email,password)
//        } else {
//            // Email does not exist in the database
//            Toast.makeText(this@editprofile_activity,"Please Try Again1",Toast.LENGTH_LONG).show()
//
//        }
//    }
//
//    override fun onCancelled(databaseError: DatabaseError) {
//        // Error occurred while fetching data
//        Toast.makeText(this@editprofile_activity,"Please Try Again2",Toast.LENGTH_LONG).show()
//
//    }
//})
//
//val new_ref = database.getReference("credentials")
//val old_email=user_email.replace(".", ",")
//
//new_ref.orderByKey().equalTo(old_email).addListenerForSingleValueEvent(object : ValueEventListener {
//    override fun onDataChange(dataSnapshot: DataSnapshot) {
//        if (dataSnapshot.exists()) {
//            for (childSnapshot in dataSnapshot.children) {
//                val originalKey = childSnapshot.key
//                val newKey = email.replace(".", ",")
//                // Get the data under the original key
//                val data = childSnapshot.value
//                new_ref.child(originalKey!!).removeValue()
//                new_ref.child(newKey).setValue(data)
//                new_ref.child(newKey).child("email").setValue(email)
//            }
//        } else {
//            //                            Toast.makeText(this@editprofile_activity,"Please Try Again",Toast.LENGTH_LONG).show()
//            Log.d("TAG", "Error")
//
//        }
//    }
//
//    override fun onCancelled(databaseError: DatabaseError) {
//        //                        Toast.makeText(this@editprofile_activity,"Please Try Again",Toast.LENGTH_LONG).show()
//        Log.d("TAG", "Error")
//
//    }
//})
//
//
//
//fun sign_in_fun(email: String, new_email: String, pass: String) {
//    val auth = FirebaseAuth.getInstance()
//
//    auth.signInWithEmailAndPassword(email, pass)
//        .addOnCompleteListener(this) { signInTask ->
//            if (signInTask.isSuccessful) {
//                // Sign in successful, now update email
//                val currentUser = auth.currentUser
//                if (currentUser != null) {
//                    currentUser.updateEmail(new_email)
//                        .addOnCompleteListener { updateEmailTask ->
//                            if (updateEmailTask.isSuccessful) {
//                                // Email updated successfully
//                                Toast.makeText(this@editprofile_activity, "Email updated successfully", Toast.LENGTH_LONG).show()
//                            } else {
//                                // Failed to update email
//                                Toast.makeText(this@editprofile_activity, "Failed to update email. ${updateEmailTask.exception?.message}", Toast.LENGTH_LONG).show()
//                                Log.e("EditProfileActivity", "Failed to update email", updateEmailTask.exception)
//                            }
//                        }
//                } else {
//                    // Current user is null
//                    Toast.makeText(this@editprofile_activity, "Current user is null", Toast.LENGTH_LONG).show()
//                    Log.e("EditProfileActivity", "Current user is null")
//                }
//            } else {
//                // Sign in failed
//                Toast.makeText(this@editprofile_activity, "Sign in failed. ${signInTask.exception?.message}", Toast.LENGTH_LONG).show()
//                Log.e("EditProfileActivity", "Sign in failed", signInTask.exception)
//            }
//        }
//}