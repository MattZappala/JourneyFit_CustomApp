package com.example.journeyfit_customapp

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class ViewActivity : AppCompatActivity() {
    private lateinit var adapter: ActivityAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var activityList: List<Activity>
    private lateinit var list: RecyclerView
    private lateinit var db: DatabaseHandler
    var typesList = mutableListOf<String>( "No Filter")
    var locList = mutableListOf<String>( "No Filter")
    var spinnerValueType: String = "No Filter"
    var spinnerValueLoc: String = "No Filter"

    @ExperimentalStdlibApi
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

        getLists()

        if (spinnerType != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, typesList)
            spinnerType.adapter = adapter
            spinnerType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    spinnerValueType = typesList[position]
                    setView()
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
                    spinnerValueLoc = locList[position]
                    setView()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun setView(){
        var query = "SELECT * FROM ${DatabaseHandler.TABLE_ACTIVITY}"
        when {
            spinnerValueType != "No Filter" && spinnerValueLoc == "No Filter" -> query += " WHERE type = \"$spinnerValueType\""
            spinnerValueLoc != "No Filter" && spinnerValueType == "No Filter" -> query += " WHERE location = \"$spinnerValueLoc\""
            spinnerValueType != "No Filter" && spinnerValueLoc != "No Filter" -> query += " WHERE type = \"$spinnerValueType\" AND location = \"$spinnerValueLoc\""
        }
        activityList = db.viewActivity(query)
        adapter = ActivityAdapter(activityList)
        list.adapter = adapter
    }


    @ExperimentalStdlibApi
    private fun getLists(){
        while (typesList.size > 1) {
            typesList.removeLast()
        }
        val dbTypesList = db.viewSet("type")
        Log.i("Testing", typesList.toString())
        dbTypesList.forEach {
            typesList.add(it)
        }

        while (locList.size > 1) {
            locList.removeLast()
        }
        val dbLocList = db.viewSet("location")
        Log.i("Testing", locList.toString())
        dbLocList.forEach {
            locList.add(it)
        }
    }

    @ExperimentalStdlibApi
    override fun onResume() {
        super.onResume()
        Log.i("Testing", "resume")
        setView()
        getLists()
    }
}