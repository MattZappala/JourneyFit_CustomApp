package com.example.journeyfit_customapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class ScheduleActivity : AppCompatActivity() {
    //declare variables
    private lateinit var db: DatabaseHandler
    private lateinit var timeInput: EditText
    private lateinit var dateInput: CalendarView
    private lateinit var dateVal: String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_activity)
        //set up db variable
        db = DatabaseHandler(this)

        //connect widgets
        dateInput = findViewById<CalendarView>(R.id.calendarView)
        timeInput = findViewById<EditText>(R.id.editTime)
        val spin = findViewById<Spinner>(R.id.spinnerSchedule)
        val locInput = findViewById<EditText>(R.id.editLocation)
        val btnSchedule = findViewById<Button>(R.id.buttonSchedule)

        //restrict users from selecting past dates
        dateInput.minDate = System.currentTimeMillis() - 1000;

        //set up formatting for date
        val pattern = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(Date(dateInput.date))

        dateVal = date
        Log.i("Testing",dateVal)

        //get value of date from date picker widget
        dateInput.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            dateVal = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            Log.i("Testing",dateVal)
        }

        //get list for spinner
        val typesList = db.viewSet("type")
        var spinnerValueType = "Basketball"

        //set up spinner like other activites
        if (spin != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, typesList)
            spin.adapter = adapter
            spin.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    spinnerValueType = typesList[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    //do nothing
                }
            }
        }

        //on submit add activity to database if no errors are found
        btnSchedule.setOnClickListener {
            if (!checkErrors()) {
                db.addActivity(
                    Activity(
                        dateVal,
                        spinnerValueType,
                        timeInput.text.toString(),
                        null,
                        true,
                        0,
                        locInput.text.toString(),
                        "none",
                        null,
                        0
                    )
                )
                //finish activity and go back to previous page
                finish()
            }
        }

    }

    //check errors
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkErrors(): Boolean {
        var error = false
        val pattern = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = dateVal
        //check if date is current date
        if(date == simpleDateFormat.format(Date())){
            Log.i("Testing", LocalTime.now().toString())
            val time = LocalTime.parse(timeInput.text)
            //check time entered is in the future e.g. if current date is 1-11-20 at 1:00pm the user can not enter 1-11-20 12:00pm as it is in the past
            if(time <= LocalTime.now()){
                //display toast
                Toast.makeText(this,"Please select time in the future",Toast.LENGTH_LONG).show()
                Log.i("Testing", "true")
                //error exists
                error = true
            }
        }
        //same regex expression as add activity but set up for 24 hour time hh:MM
        val regex = "^([0-1][0-9]|2[0-3]):[0-5][0-9]\$".toRegex()
        if (!timeInput.text.toString().matches(regex)) {
            //error if regex expression does not match
            timeInput.error = "Please enter 24hr time as HH:MM e.g. 07:59"
            error = true
        }
        return error
    }
}