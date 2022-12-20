package com.example.zadanie8

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var playRecAudioButton = findViewById<Button>(R.id.RecPlayAudio)
        playRecAudioButton.setOnClickListener { _->
            startActivity(Intent(this, PlayRecAudioActivity::class.java))
        }

        var playWideoButton = findViewById<Button>(R.id.PlayWid)
        playWideoButton.setOnClickListener { _->
            startActivity(Intent(this, PlayWideoActivity::class.java))
        }

        var recWideoButton = findViewById<Button>(R.id.RecWid)
        recWideoButton.setOnClickListener { _->
            startActivity(Intent(this, RecWideoActivity::class.java))
        }
    }
}