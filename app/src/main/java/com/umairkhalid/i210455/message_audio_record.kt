package com.umairkhalid.i210455


import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.File
import java.io.IOException

class message_audio_record {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    var isRecording = false
        private set

//    fun startRecording() {
//        if (!isRecording) {
//            recorder = MediaRecorder()
//            outputFile = File.createTempFile("audio", ".3gp")
//            recorder?.apply {
//                setAudioSource(MediaRecorder.AudioSource.MIC)
//                Log.e("Audio Rec", "SUIII")
//                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//                setOutputFile(outputFile?.absolutePath)
//                prepare()
//                start()
//            }
//            isRecording = true
//        }
//    }

    fun startRecording() {
        if (!isRecording) {
            recorder = MediaRecorder().apply {
                try {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    outputFile = File.createTempFile("audio", ".3gp")
                    setOutputFile(outputFile?.absolutePath)
                    prepare()
                    start()
                    isRecording = true
                    Log.d("AudioRecorder", "Recording started")
                } catch (e: IOException) {
                    Log.e("AudioRecorder", "Failed to start recording: ${e.message}")
                    releaseRecorder()
                }
            }
        }
    }
    private fun releaseRecorder() {
        recorder?.apply {
            reset()
            release()
        }
        recorder = null
    }


    fun stopRecording(callback: (Uri?) -> Unit) {
        if (isRecording) {
            recorder?.apply {
                stop()
                release()
            }
            isRecording = false
            callback(Uri.fromFile(outputFile))
        } else {
            callback(null)
        }
    }

    fun cancelRecording() {
        if (isRecording) {
            recorder?.apply {
                stop()
                release()
            }
            outputFile?.delete()
            isRecording = false
        }
    }
}