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
        testData(db)

       // val activityList = db.viewActivity("SELECT * FROM ${DatabaseHandler.TABLE_ACTIVITY} WHERE type = \"Basketball\"")
        //Log.i("Testing DB", activityList.toString())

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

    private fun testData(db: DatabaseHandler) {
        db.addActivity(Activity("1/10/20","Basketball","1:00pm",11.0,true,3,"kew","fun",null,1))
        db.addActivity(Activity("2/10/20","Tennis","2:30pm",24.5,true,4,"East kew","Very fun",null,1))
        db.addActivity(Activity("3/10/20","Squash","12:00pm",12.2,true,5,"Hawthorne","Good times",null,1))
        db.addActivity(Activity("5/10/20","Squash","11:00am",1.2,true,1,"Hawthorne East","Times",null,1))
    }
}