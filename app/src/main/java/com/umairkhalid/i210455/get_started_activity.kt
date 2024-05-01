package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth

class get_started_activity : AppCompatActivity() {
    lateinit var url :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        url = getString(R.string.url)

        setContentView(R.layout.getting_started)
        val sign_up_btn: TextView =findViewById(R.id.sign_up_btn)
        val login_btn: TextView =findViewById(R.id.login_btn)
        val name_txt:TextView=findViewById(R.id.signup_name_txt)
        val email_txt:TextView=findViewById(R.id.signup_email_txt)
        val contact_txt:TextView=findViewById(R.id.signup_contact_txt)
        val pass_txt:TextView=findViewById(R.id.signup_pass_txt)

        val city_spinner: Spinner = findViewById(R.id.signup_city_spinner)
        city_spinner.prompt = "Select City"
        val cities = arrayOf("Select City","Rawalpindi", "Islamabad", "Lahore")

        val country_spinner: Spinner = findViewById(R.id.signup_country_spinner)
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

        val spannableString = SpannableString("Log in")
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        login_btn.text = spannableString

        sign_up_btn.setOnClickListener{

            val name=name_txt.text.toString().trim()
            val email=email_txt.text.toString().trim()
            val pass=pass_txt.text.toString().trim()
            val contact=contact_txt.text.toString().trim()
            val country= country_spinner.selectedItem.toString().trim()
            val city= city_spinner.selectedItem.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && country.isNotEmpty() && contact.isNotEmpty() && city.isNotEmpty() )
            {
                val emailPattern = "[a-zA-Z0-9._-]+@gmail.com"

                if(pass.length < 8 ){
                    Toast.makeText(this,"Password too small",Toast.LENGTH_LONG).show()
                }
                else if(!email.matches(emailPattern.toRegex())){
                    Toast.makeText(this,"Invalid Email",Toast.LENGTH_LONG).show()
                }
                else if(contact.length.toInt() != 11 || !contact.matches("^[0-9]+\$".toRegex())){
                    Toast.makeText(this,"Invalid Contact Info",Toast.LENGTH_LONG).show()
                }
                else if(country=="Select Country"){
                    Toast.makeText(this,"Invalid Country",Toast.LENGTH_LONG).show()
                }
                else if(city=="Select City"){
                    Toast.makeText(this,"Invalid City",Toast.LENGTH_LONG).show()
                }
                else{
                    val userdata=user_data(name,email,contact,country,city,pass)
                    val tempUrl = "${url}checkuser.php"
                    val stringRequest = object : StringRequest(
                        Request.Method.POST, tempUrl,
                        Response.Listener { response ->
                            // Handle response
                            if (response == "User not found") {
                                SignUp(userdata)
                            } else {
                                val userID = response
                                Toast.makeText(this,"User Already Exists",Toast.LENGTH_LONG).show()
                            }
                        },
                        Response.ErrorListener { error ->
                            // Handle error
                            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                            Log.e("API Error", "Error occurred: ${error.message}")
                        }) {
                        override fun getParams(): MutableMap<String, String> {
                            val params = HashMap<String, String>()
                            params["email"] = email
                            return params
                        }
                    }
                    // Add the request to the RequestQueue
                    Volley.newRequestQueue(this).add(stringRequest)
                }

            }
            else{
                Toast.makeText(this,"Please fill in all fields",Toast.LENGTH_LONG).show()
            }
        }

        login_btn.setOnClickListener{
            val nextActivityIntent = Intent(this, login_activity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }
    }
    fun SignUp(userData: user_data) {
        val intent = Intent(this, dialer_activity::class.java)
        intent.putExtra("myData", userData)
        startActivity(intent)
    }
}