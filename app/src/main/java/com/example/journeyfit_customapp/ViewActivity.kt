package com.example.journeyfit_customapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.ItemKeyProvider.SCOPE_MAPPED
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewActivity : AppCompatActivity() {
    private lateinit var adapter: ActivityAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var activityList: List<Activity>
    lateinit var list: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_list)

        list = findViewById<RecyclerView>(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(this)
        list.layoutManager = linearLayoutManager

        val db = DatabaseHandler(this)
        activityList = db.viewActivity()

        adapter = ActivityAdapter(activityList)
        list.adapter = adapter
    }
}