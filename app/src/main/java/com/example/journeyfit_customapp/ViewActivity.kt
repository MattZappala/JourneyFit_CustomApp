package com.example.journeyfit_customapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewActivity : AppCompatActivity() {
    private lateinit var adapter: ActivityAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_list)

        val list = findViewById<RecyclerView>(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(this)
        list.layoutManager = linearLayoutManager

        val db = DatabaseHandler(this)
        val activityList = db.viewActivity()

        adapter = ActivityAdapter(activityList)
        list.adapter = adapter
    }
}