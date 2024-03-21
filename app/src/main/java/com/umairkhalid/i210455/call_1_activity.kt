package com.umairkhalid.i210455

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.umairkhalid.i210455.databinding.Call1Binding
import com.umairkhalid.i210455.databinding.Call2Binding
class call_1_activity : AppCompatActivity() {

    private lateinit var binding: Call1Binding
    private val appId = "a643363f4acc4cb693dfac855ba9b2ed"
    private val token =
        "007eJxTYChNjZw3dZqPvvTdze4nztXWzm5aF/NKiq/mYo+xRTVXYY8CQ6KZibGxmXGaSWJysklykpmlcUpaYrKFqWlSomWSUWrK+i8/UxsCGRkss0xYGRkgEMTnYMhNzSvJL8pNZWAAANb6Iaw="

    //    private var token : String? = null
    var appCertificate = "a3dacd220bc3472c8d0297c801046b2c"
    var expirationTimeInSeconds = 3600
    private val channelName = "mentorme"
    private val uid = 0
    private var isJoined = false
    private var agoraEngine: RtcEngine? = null

    private val PERMISSION_REQ_ID = 10
    private val REQUESTED_PERMISSIONS = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

    private var isMuted = false
    private var isSpeakerOn = true // Assuming speaker is on by default


    private fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            REQUESTED_PERMISSIONS[0]
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupAudioSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            agoraEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
        } catch (e: Exception) {
            showMessage(e.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Call1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

        binding.endcallBtn.setOnClickListener {
            leaveChannel()
//            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
//            startActivity(nextActivityIntent)
            onBackPressed()
            finish()
        }

        binding.muteBtn.setOnClickListener{
            if(isMuted==true){
                isMuted=false
                agoraEngine?.muteLocalAudioStream(isMuted)
                binding.muteBtn.setImageResource(R.drawable.mic_off)
                showMessage("Microphone unmuted")
            }else{
                isMuted=true
                agoraEngine?.muteLocalAudioStream(isMuted)
                binding.muteBtn.setImageResource(R.drawable.mic_on)
                showMessage("Microphone muted")
            }
        }
        binding.speakerBtn.setOnClickListener{
            if(isSpeakerOn==true){
                isSpeakerOn=false
                agoraEngine?.setEnableSpeakerphone(isSpeakerOn)
                binding.speakerBtn.setImageResource(R.drawable.volume_off)
                showMessage("Speaker off")
            }else{
                isSpeakerOn=true
                agoraEngine?.setEnableSpeakerphone(isSpeakerOn)
                binding.speakerBtn.setImageResource(R.drawable.volume_up)
                showMessage("Speaker on")
            }
        }

        val input_txt = intent.getStringExtra("MENTOR_NAME")
        val database = FirebaseDatabase.getInstance()
        val mentorsRef = database.getReference("mentors")
        val query = mentorsRef.orderByChild("name").equalTo(input_txt.toString())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (mentorSnapshot in dataSnapshot.children) {
                    val name = mentorSnapshot.child("name").getValue(String::class.java)
                    val profilePicUrl = mentorSnapshot.child("profile_pic").getValue(String::class.java)

                    // Check if all required fields are present
                    if (name != null && profilePicUrl != null) {
                        binding.userNameTxt.text = name
                        Picasso.get().load(profilePicUrl).into(binding.userImage)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@call_1_activity, "Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
            }
        })


        var database_2 = FirebaseDatabase.getInstance()
        val my_ref_2 = database_2.getReference("users")
        var currentUser_2 = FirebaseAuth.getInstance().currentUser
        val userId_2 = currentUser_2?.uid

        if(userId_2!=null){

            my_ref_2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pic_url= dataSnapshot.child(userId_2).child("picture").value.toString()

                    if(pic_url!=null){
                        Picasso.get().load(pic_url).into(binding.userImageView)
                    }

                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.d("TAG", "Unable to retrieve Data")

                }
            })
        }



        setupAudioSDKEngine()
        joinChannel()
    }

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            showMessage("Remote user joined $uid")
        }

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            isJoined = true
            showMessage("Joined Channel $channel")
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            showMessage("Remote user offline $uid $reason")
        }
    }

    fun joinChannel() {
        if (checkSelfPermission()) {
            agoraEngine?.joinChannel(token, channelName, null, uid)
            showMessage("Calling")
        } else {
            showMessage("Permissions not granted")
        }
    }

    fun leaveChannel() {
        if (!isJoined) {
            showMessage("Join a call first")
        } else {
            agoraEngine?.leaveChannel()
//            showMessage("You left the channel")
            showMessage("Call Ended")
            isJoined = false
        }
    }
}
//




//class call_1_activity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContentView(R.layout.call_1)
////        setContentView(binding.root)
//
//        val user_img: ImageView =findViewById(R.id.user_image)
//        val user_name: TextView =findViewById(R.id.user_name_txt)
//
//        val input_txt = intent.getStringExtra("MENTOR_NAME")
//
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
//
//                    }
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//                Toast.makeText(this@call_1_activity,"Unable to Fetch Mentor Data", Toast.LENGTH_LONG).show()
//            }
//        })
//
//
//        val endcall_btn: ImageButton =findViewById(R.id.endcall_btn)
//
//        endcall_btn.setOnClickListener{
////            val nextActivityIntent = Intent(this, john_cooper_1_activity::class.java)
////            startActivity(nextActivityIntent)
//            onBackPressed()
//            finish()
//        }
//
//    }
//}