package com.example.journeyfit_customapp

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageAdd = findViewById<ImageView>(R.id.imageAdd)

        imageAdd.setOnClickListener {
            val intent = Intent(this,AddActivity::class.java)
            startActivity(intent)
        }
    }
}