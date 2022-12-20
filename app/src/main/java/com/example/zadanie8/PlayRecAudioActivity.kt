package com.example.zadanie8

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class PlayRecAudioActivity : AppCompatActivity() {
    val mediaRecorder = MediaRecorder()
    val mediaPlayer = MediaPlayer()
    var audioDirPath: String? = null
    var audioFilePath: String? = null
    var audioFileName = "audio_filename1" + ".3gp"
    val REQUEST_ADIO_PERMISSION_CODE = 11

    var recStartAudioButt: Button? = null
    var recStopAudioButt: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_rec_audio)
        recStartAudioButt = findViewById(R.id.buttonAudioRecStart)
        recStopAudioButt = findViewById(R.id.buttonAudioRecStop)
        val recPlayAudioButt = findViewById<Button>(R.id.buttonAudioRecPlay)
        val recPlayStopAudioButt = findViewById<Button>(R.id.buttonAudioRecPlayStop)
        val textView = findViewById<TextView>(R.id.textView)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val maxDuration = findViewById<TextView>(R.id.tvDurationTime)
        val currentTime = findViewById<TextView>(R.id.tvCurrentTime)
        val pauseButt = findViewById<Button>(R.id.buttonPause)
        val resumeButt = findViewById<Button>(R.id.buttonResume)
        var isPrepered = false

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            recStartAudioButt?.isEnabled = false
            recStopAudioButt?.isEnabled = false
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_ADIO_PERMISSION_CODE)
        }
        mediaPlayer.setOnPreparedListener{ _ ->
            isPrepered = true
        }
        audioDirPath = filesDir.absolutePath
        audioFilePath = "$audioDirPath/$audioFileName"

        recStopAudioButt?.isEnabled = false

        recStartAudioButt?.setOnClickListener { _ ->
            recStartAudioButt?.isEnabled = false
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder.setOutputFile(audioFilePath)
            mediaRecorder.prepare()
            mediaRecorder.start()
            recStopAudioButt?.isEnabled = true
        }

        recStopAudioButt?.setOnClickListener { _ ->
            recStopAudioButt?.isEnabled = false
            mediaRecorder.stop()
            mediaRecorder.release()
            Toast.makeText(this, audioFilePath, Toast.LENGTH_SHORT)
            textView.text = "File recorded to:\n$audioFilePath"
            mediaPlayer.setDataSource(audioFilePath)
            mediaPlayer.prepare()
            seekBar.max = mediaPlayer.duration/1000
            maxDuration.text = "${((mediaPlayer.duration/1000) - ((mediaPlayer.duration/1000)%60))/60}".padStart(2, '0') +":"+ "${(mediaPlayer.duration/1000)%60}".padStart(2, '0')
            recStartAudioButt?.isEnabled = true
            recPlayAudioButt?.isEnabled = true
        }

        recPlayAudioButt.isEnabled = false
        recPlayStopAudioButt.isEnabled = false

        recPlayAudioButt?.setOnClickListener { _ ->
            mediaPlayer.start()
            recPlayStopAudioButt?.isEnabled = true
            pauseButt.isEnabled = true
        }

        pauseButt.setOnClickListener { _ ->
            pauseButt.isEnabled = false
            mediaPlayer.pause()
            resumeButt.isEnabled = true
        }

        resumeButt.setOnClickListener { _ ->
            resumeButt.isEnabled = false
            mediaPlayer.start()
            pauseButt.isEnabled = true
        }

        recPlayStopAudioButt?.setOnClickListener { _ ->
            resumeButt.isEnabled = false
            pauseButt.isEnabled = false
            recPlayStopAudioButt.isEnabled = false
            recPlayAudioButt.isEnabled = false
            isPrepered = false
            mediaPlayer.stop()
            mediaPlayer.release()
            Toast.makeText(this, "You stopped the audio", Toast.LENGTH_SHORT)
        }

        val mHandler = Handler()
        this@PlayRecAudioActivity.runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null && isPrepered) {
                    val mCurrentPosition: Int = mediaPlayer.currentPosition / 1000
                    seekBar.setProgress(mCurrentPosition)
                    currentTime.text = "${((mCurrentPosition) - ((mCurrentPosition)%60))/60}".padStart(2, '0') +":"+ "${(mCurrentPosition)%60}".padStart(2, '0')
                }
                mHandler.postDelayed(this, 1000)
            }
        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000)
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ADIO_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    recStartAudioButt?.isEnabled = true
                    recStopAudioButt?.isEnabled = true
                } else {
                    Toast.makeText(this, "Bez udzielenia zgody nie da się nagrać audio", Toast.LENGTH_SHORT)
                }
                return
            }

            else -> {
            }
        }
    }
}