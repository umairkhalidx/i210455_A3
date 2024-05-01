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
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class editprofile_activity : AppCompatActivity() {
    private var  mAuth = FirebaseAuth.getInstance();
    lateinit var url :String
    lateinit var userID : String
    lateinit var User : userObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.edit_profile)

        url = getString(R.string.url)
        userID= intent.getStringExtra("userID").toString()

        val back_btn: ImageButton =findViewById(R.id.back_btn)
        val update_profile_btn: Button =findViewById(R.id.update_profile_btn)

        val name_input: TextView =findViewById(R.id.input_name)
        val email_input: TextView =findViewById(R.id.input_email)
        val contact_input: TextView =findViewById(R.id.input_contact)
        val user_img: ImageView = findViewById(R.id.user_image)

        var database = FirebaseDatabase.getInstance()
        val my_ref = database.getReference("users")
        var currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid


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

        User = userObject(userID,"","","","","","","")
        getUserData(userID) {

            if(User.profileImg!=null){
                val imageURL = "${url}ProfileImages/${User.profileImg}"
                Picasso.get().load(imageURL).into(user_img)
            }
            name_input.text=User.name
            email_input.text=User.email
            contact_input.text=User.contact

            update_profile_btn.setOnClickListener{

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

                        if(email!=User.email){
                            Toast.makeText(this,"Enter your Current Email", Toast.LENGTH_LONG).show()
                        }else{
                            val tempUrl = "${url}updateuserdata.php"

                            val stringRequest = object : StringRequest(
                                Request.Method.POST, tempUrl,
                                Response.Listener { response ->
                                    // Handle response
                                    if (response == "User data updated successfully") {
                                        Toast.makeText(this,"Data Updated Successfully",Toast.LENGTH_LONG).show()
                                        onBackPressed()
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to update user data", Toast.LENGTH_LONG).show()
                                    }
                                },
                                Response.ErrorListener { error ->
                                    // Handle error
                                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                                    Log.e("API Error", "Error occurred: ${error.message}")
                                }) {
                                override fun getParams(): MutableMap<String, String> {
                                    val params = HashMap<String, String>()
                                    params["userID"] = User.userID
                                    params["name"] = name
                                    params["email"] = email
                                    params["contact"] = contact
                                    params["country"] = country
                                    params["city"] = city
                                    return params
                                }
                            }
                            Volley.newRequestQueue(this).add(stringRequest)
                        }

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

    private fun getUserData(userID:String,callback: () -> Unit){
        val tempUrl = "${url}getuserdata.php" // Assuming the PHP file to retrieve user data is named getUserData.php

        val stringRequest = object : StringRequest(
            Request.Method.POST, tempUrl,
            Response.Listener { response ->
                // Handle response
                if (response.startsWith("User not found")) {
                    // User not found, handle accordingly
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                } else {
                    // Parse JSON response
                    try {
                        val jsonObject = JSONObject(response)
                        User.name = jsonObject.getString("name")
                        User.email = jsonObject.getString("email")
                        User.city = jsonObject.getString("city")
                        User.country = jsonObject.getString("country")
                        User.contact = jsonObject.getString("contact")
                        User.profileImg = jsonObject.getString("profileImg")
                        User.coverImg = jsonObject.getString("coverImg")
                        callback()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Error Fetching Data", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "Error Fetching Data", Toast.LENGTH_SHORT).show()
                Log.e("API Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["userID"] = userID
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

    }

}
