package com.example.zadanie8

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecWideoActivity : AppCompatActivity() {
    var videoView: VideoView? = null
    var currVideoUri: Uri? = null
    val REQUEST_VIDEO_CAPTURE = 31
    val REQUEST_VIDEO_PERMISSION_CODE = 32
    var recStartButt: Button? = null
    var wideoPlayButt: Button? = null
    private var myAdapter: WideoListAdapter? = null
    private val data = ArrayList<Pair<Long, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_wideo)

        recStartButt = findViewById<Button>(R.id.buttonVideoRecord)
        wideoPlayButt = findViewById<Button>(R.id.buttonVideoPlay)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            recStartButt?.isEnabled = false
            wideoPlayButt?.isEnabled = false
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_VIDEO_PERMISSION_CODE)
        }

        videoView = findViewById(R.id.videoView2)
        val mediaController = MediaController(this)
        videoView?.setMediaController(mediaController)

        recStartButt?.setOnClickListener { _ ->
            val i = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            i.resolveActivity(packageManager) != null
            startActivityForResult(i, REQUEST_VIDEO_CAPTURE)

        }

        wideoPlayButt?.setOnClickListener { _ ->
            videoView?.start()
        }

        val uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        println("URI: ${uri.path}")
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor==null) {

        } else if(!cursor.moveToFirst()) {
            //no media case
        } else {
            val idColumn = cursor.getColumnIndex(android.provider.MediaStore.Video.Media._ID)
            val titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Video.Media.TITLE)

            do {
                val itemId = cursor.getLong(idColumn)
                val itemTitle = cursor.getString(titleColumn)
                data.add(Pair(itemId, itemTitle))
            } while (cursor.moveToNext())
            myAdapter = WideoListAdapter(data) { position -> onListItemClicked(position) }
            val rView = findViewById<RecyclerView>(R.id.recyclerView)
            rView.layoutManager = LinearLayoutManager(this)
            rView.adapter = myAdapter
        }
    }

    private fun onListItemClicked(position: Int) {
        currVideoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, data[position].first)
        videoView?.setVideoURI(currVideoUri)
        videoView?.seekTo(1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currVideoUri = data?.data
        println("data $data")
        videoView?.setVideoURI(currVideoUri)
        videoView?.seekTo(1)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_VIDEO_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    recStartButt?.isEnabled = true
                    wideoPlayButt?.isEnabled = true
                } else {
                    Toast.makeText(this, "Bez udzielenia zgody nie da się nagrać wideo", Toast.LENGTH_SHORT)
                }
                return
            }

            else -> {
            }
        }
    }
}