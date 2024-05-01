package com.umairkhalid.i210455

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.umairkhalid.i210455.databinding.Call2Binding
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas


class call_2_activity : AppCompatActivity() {

    lateinit var userID: String
    lateinit var url: String
    lateinit var mentorID: String

    private lateinit var binding: Call2Binding

    private val appId = "a643363f4acc4cb693dfac855ba9b2ed"


    var appCertificate = "a3dacd220bc3472c8d0297c801046b2c"
    var expirationTimeInSeconds = 3600
    private val channelName = "mentorme"

//    private var token : String? = null
    private val token =
        "007eJxTYChNjZw3dZqPvvTdze4nztXWzm5aF/NKiq/mYo+xRTVXYY8CQ6KZibGxmXGaSWJysklykpmlcUpaYrKFqWlSomWSUWrK+i8/UxsCGRkss0xYGRkgEMTnYMhNzSvJL8pNZWAAANb6Iaw="

    private val uid = 0
    private var isJoined = false

    private var agoraEngine: RtcEngine? = null

    private var localSurfaceView: SurfaceView? = null

    private var remoteSurfaceView: SurfaceView? = null


    private val PERMISSION_REQ_ID = 10
    private val REQUESTED_PERMISSIONS = arrayOf<String>(
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.CAMERA,
    )

    private var isMuted = false
    private var isCameraOff = false


    private fun checkSelfPermission(): Boolean {
        return !(ContextCompat.checkSelfPermission(
            this,
            REQUESTED_PERMISSIONS[0]
        ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    REQUESTED_PERMISSIONS[1]
                ) != PackageManager.PERMISSION_GRANTED)
    }

    fun showMessage(message: String?) {
        runOnUiThread {
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setupVideoSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            // By default, the video module is disabled, call enableVideo to enable it.
            agoraEngine!!.enableVideo()
        } catch (e: Exception) {
            showMessage(e.toString())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Call2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        userID = intent.getStringExtra("userID").toString()
        mentorID = intent.getStringExtra("mentorID").toString()
        url = getString(R.string.url)



//        val tokenBuilder = RtcTokenBuilder2()
//        val timestamp = (System.currentTimeMillis() / 1000 + expirationTimeInSeconds).toInt()
//
//        val result = tokenBuilder.buildTokenWithUid(
//            appId, appCertificate,
//            channelName, uid, RtcTokenBuilder2.Role.ROLE_PUBLISHER, timestamp, timestamp
//        )
//
//
//        token = result

        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

        binding.endcallBtn.setOnClickListener{
            leaveChannel();
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
        binding.flipCameraBtn.setOnClickListener{
            agoraEngine?.switchCamera()
            showMessage("Camera flipped")
        }
        binding.turnOffCameraBtn.setOnClickListener{
            if(isCameraOff==true){
                isCameraOff=false
                agoraEngine?.enableLocalVideo(true)
                binding.turnOffCameraBtn.setImageResource(R.drawable.videocam_filled_white)
                showMessage("Camera on")
            }else{
                isCameraOff=true
                agoraEngine?.enableLocalVideo(false)
                binding.turnOffCameraBtn.setImageResource(R.drawable.videocam_off)
                showMessage("Camera off")
            }
        }

        setupVideoSDKEngine();
        joinChannel();

    }


    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            showMessage("Remote user joined $uid")

            // Set the remote video view
            runOnUiThread { setupRemoteVideo(uid) }
        }

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            isJoined = true
            showMessage("Joined Channel $channel")
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            showMessage("Remote user offline $uid $reason")
            runOnUiThread { remoteSurfaceView!!.visibility = View.GONE }
        }
    }

    private fun setupRemoteVideo(uid: Int) {
        remoteSurfaceView = SurfaceView(baseContext)
        remoteSurfaceView!!.setZOrderMediaOverlay(true)
        binding.remoteUser.addView(remoteSurfaceView)
        agoraEngine!!.setupRemoteVideo(
            VideoCanvas(
                remoteSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                uid
            )
        )
        remoteSurfaceView!!.visibility = View.VISIBLE
    }

    private fun setupLocalVideo() {
        localSurfaceView = SurfaceView(baseContext)
        binding.localUser.addView(localSurfaceView)
        agoraEngine!!.setupLocalVideo(
            VideoCanvas(
                localSurfaceView,
                VideoCanvas.RENDER_MODE_HIDDEN,
                0
            )
        )
    }

    fun joinChannel() {
        if (checkSelfPermission()) {
            val options = ChannelMediaOptions()

            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            setupLocalVideo()
            localSurfaceView!!.visibility = View.VISIBLE
            agoraEngine!!.startPreview()
            agoraEngine!!.joinChannel(token, channelName, uid, options)
            showMessage("Calling")
        } else {
            Toast.makeText(applicationContext, "Permissions was not granted", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun leaveChannel() {
        if (!isJoined) {
            showMessage("Join a call first")
        } else {
            agoraEngine!!.leaveChannel()
//            showMessage("You left the channel")
            showMessage("Call Ended")
            if (remoteSurfaceView != null) remoteSurfaceView!!.visibility = View.GONE
            if (localSurfaceView != null) localSurfaceView!!.visibility = View.GONE
            isJoined = false
        }
    }
}