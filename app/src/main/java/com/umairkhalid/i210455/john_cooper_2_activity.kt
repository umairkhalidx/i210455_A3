package com.umairkhalid.i210455

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text

class john_cooper_2_activity : AppCompatActivity() {
    lateinit var userID: String
    lateinit var url: String
    lateinit var mentorID: String
    lateinit var Mentor: mentorData

    private var  mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.john_cooper_2)

        userID = intent.getStringExtra("userID").toString()
        mentorID = intent.getStringExtra("mentorID").toString()
        url = getString(R.string.url)

        val back_btn: ImageButton =findViewById(R.id.back_arrow)
        val feedback_btn: Button =findViewById(R.id.feedback_btn)

        val input_txt = intent.getStringExtra("user_name")

        val review:TextView=findViewById(R.id.txt_type)
        val user_img:ImageView=findViewById(R.id.mentor_img)
        val user_name:TextView=findViewById(R.id.txt_username_review)

        Mentor = mentorData(mentorID, "", "", "", "", "", "", "")
        getMentors(mentorID) {
            user_name.text = Mentor.name
            val imageURL = "${url}MentorImages/${Mentor.profileImg}"
            Picasso.get().load(imageURL).into(user_img)

        }

//        val database = FirebaseDatabase.getInstance()
//        val mentorsRef = database.getReference("mentors")
//
//        val query = mentorsRef.orderByChild("name").equalTo(input_txt.toString())
//
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (mentorSnapshot in dataSnapshot.children) {
//
//                    val name = mentorSnapshot.child("name").getValue(String::class.java)
//                    val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)
//
//                    // Check if all required fields are present
//                    if (name != null && profilePicUrl != null) {
//                        user_name.text=name.toString()
//                        Picasso.get().load(profilePicUrl).into(user_img)
//                    }
//                }
//
//
//                // Notify your adapter that the data has changed
//                // adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//                Toast.makeText(this@john_cooper_2_activity,"Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
//            }
//        })


        back_btn.setOnClickListener{
//            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
//            startActivity(nextActivityIntent)
//            finish()
            onBackPressed()
            finish()
        }

        feedback_btn.setOnClickListener{
            if(review.text!=null){
                if(input_txt!=null){

                    val tempUrl = "${url}addreview.php"

                    val request = object : StringRequest(Request.Method.POST, tempUrl,
                        Response.Listener { response ->
                            Toast.makeText(this,"Feedback Added Successfully", Toast.LENGTH_LONG).show()
//                            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
//                            nextActivityIntent.putExtra("mentorID", mentorID)
//                            nextActivityIntent.putExtra("userID", userID)
//                            startActivity(nextActivityIntent)
//                            finish()
                            onBackPressed()
                            finish()
                                          },
                        Response.ErrorListener { error ->
                            // Handle error
                            Toast.makeText(this,"Please try again", Toast.LENGTH_LONG).show()

                        }) {
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params["userID"] = userID // Assuming userID, name, and review are defined somewhere in your code
                            params["name"] = Mentor.name
                            params["review"] = review.text.toString()
                            return params
                        }
                    }
                    Volley.newRequestQueue(this).add(request)
                }

            }
            else{
                Toast.makeText(this,"Please fill Feedback", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun getMentors(mentorID: String, callback: () -> Unit) {

        val tempURL = "${url}getmentordata_id.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, tempURL,
            Response.Listener { response ->
                // Handle response
                if (response.startsWith("Mentor not found")) {
                    // User not found, handle accordingly
                    Toast.makeText(this, "Mentor not found", Toast.LENGTH_SHORT).show()
                } else {
                    // Parse JSON response
                    try {
                        val jsonObject = JSONObject(response)
                        Mentor.mentorID = jsonObject.getString("mentorID")
                        Mentor.name = jsonObject.getString("name")
                        Mentor.occupation = jsonObject.getString("occupation")
                        Mentor.description = jsonObject.getString("description")
                        Mentor.price = jsonObject.getString("price")
                        Mentor.profileImg = jsonObject.getString("profileImg")
                        Mentor.status = jsonObject.getString("status")
                        Mentor.favourite = jsonObject.getString("favourite")
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
                params["mentorID"] = mentorID
                return params
            }
        }

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

    }
}