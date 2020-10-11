package com.example.journeyfit_customapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.ItemKeyProvider.SCOPE_MAPPED
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.individual_activity.*

class ViewActivity : AppCompatActivity() {
    private lateinit var adapter: ActivityAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var activityList: List<Activity>
    lateinit var list: RecyclerView
    lateinit var db: DatabaseHandler
    var typesList = mutableListOf<String>( "No Filter")
    var locList = mutableListOf<String>( "No Filter")
    lateinit var spinnerValue: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_list)

        val spinnerType = findViewById<Spinner>(R.id.filter1)
        val spinnerLoc = findViewById<Spinner>(R.id.filter2)
        list = findViewById<RecyclerView>(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(this)
        list.layoutManager = linearLayoutManager

        db = DatabaseHandler(this)
        setView()

        val dbTypesList = db.viewSet("type")
        Log.i("Testing", typesList.toString())
        dbTypesList.forEach {
                typesList.add(it)
        }

        val dbLocList = db.viewSet("location")
        Log.i("Testing", typesList.toString())
        dbLocList.forEach {
            locList.add(it)
        }

        if (spinnerType != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, typesList)
            spinnerType.adapter = adapter
            spinnerType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    spinnerValue = typesList[position]

                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        if (spinnerLoc != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, locList)
            spinnerLoc.adapter = adapter
            spinnerLoc.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    spinnerValue = locList[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun setView(){
        activityList = db.viewActivity()
        adapter = ActivityAdapter(activityList)
        list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        Log.i("Testing", "resume")
        setView()
    }
}