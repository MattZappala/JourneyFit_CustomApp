package com.example.journeyfit_customapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageAdd = findViewById<ImageView>(R.id.imageAdd)
        val imageList = findViewById<ImageView>(R.id.imageList)
        val imageSchedule = findViewById<ImageView>(R.id.imageSchedule)

        val db = DatabaseHandler(this)

        //test data
//        db.addActivity(Activity("1/1/20","Bball","1:00pm",12.2,true,3,"kew","fun",null))
//        db.addActivity(Activity("1/3/20","Tennis","1:00pm",12.2,true,3,"kew","fun",null))
//        db.addActivity(Activity("1/4/20","Squash","1:00pm",12.2,true,3,"kew","fun",null))
        val activityList = db.viewActivity()
        Log.i("Testing DB", activityList.toString())

        imageAdd.setOnClickListener {
            val intent = Intent(this,AddActivity::class.java)
            startActivity(intent)
        }

        imageList.setOnClickListener {
            val intent = Intent(this,ViewActivity::class.java)
            startActivity(intent)
        }

        imageSchedule.setOnClickListener {
            val intent = Intent(this,ScheduleActivity::class.java)
            startActivity(intent)
        }
    }
}