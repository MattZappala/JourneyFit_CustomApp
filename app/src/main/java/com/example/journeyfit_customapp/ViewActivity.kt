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

        //ser up spinner variables
        val spinnerType = findViewById<Spinner>(R.id.filter1)
        val spinnerLoc = findViewById<Spinner>(R.id.filter2)
        //set up recycler view
        list = findViewById<RecyclerView>(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(this)
        list.layoutManager = linearLayoutManager

        //set up db connection
        db = DatabaseHandler(this)
        //set the data shown in the list
        setView()

        //get lists to display in spinners
        getLists()

        //set up spinner adapter to act as filter for the activity types
        if (spinnerType != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, typesList)
            spinnerType.adapter = adapter
            spinnerType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    //assign spinner value to variable
                    spinnerValueType = typesList[position]
                    //call setView to update list
                    setView()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // do nothing
                }
            }
        }
        //same as above but for location
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

    //set view in list
    private fun setView(){
        //set up query
        var query = "SELECT * FROM ${DatabaseHandler.TABLE_ACTIVITY}"
        //add to the query to show the filter when calling the database
        when {
            spinnerValueType != "No Filter" && spinnerValueLoc == "No Filter" -> query += " WHERE type = \"$spinnerValueType\""
            spinnerValueLoc != "No Filter" && spinnerValueType == "No Filter" -> query += " WHERE location = \"$spinnerValueLoc\""
            spinnerValueType != "No Filter" && spinnerValueLoc != "No Filter" -> query += " WHERE type = \"$spinnerValueType\" AND location = \"$spinnerValueLoc\""
        }
        //update view with the returned list of activities
        activityList = db.viewActivity(query)
        adapter = ActivityAdapter(activityList)
        list.adapter = adapter
    }

    //get lists for spinners
    @ExperimentalStdlibApi
    private fun getLists(){
        //remove all items in list so that it updates if data is deleted
        while (typesList.size > 1) {
            typesList.removeLast()
        }
        //return all unique values in the type column
        val dbTypesList = db.viewSet("type")
        Log.i("Testing", typesList.toString())
        //add them to list
        dbTypesList.forEach {
            typesList.add(it)
        }
        //same as above but for location
        while (locList.size > 1) {
            locList.removeLast()
        }
        val dbLocList = db.viewSet("location")
        Log.i("Testing", locList.toString())
        dbLocList.forEach {
            locList.add(it)
        }
    }

    //when returned to page update views when record has been edited
    @ExperimentalStdlibApi
    override fun onResume() {
        super.onResume()
        Log.i("Testing", "resume")
        setView()
        getLists()
    }
}