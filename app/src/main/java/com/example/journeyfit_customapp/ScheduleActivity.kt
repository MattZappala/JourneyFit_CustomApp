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
    private lateinit var db: DatabaseHandler
    private lateinit var timeInput: EditText
    private lateinit var dateInput: CalendarView
    private lateinit var dateVal: String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_activity)

        db = DatabaseHandler(this)

        dateInput = findViewById<CalendarView>(R.id.calendarView)
        timeInput = findViewById<EditText>(R.id.editTime)
        val spin = findViewById<Spinner>(R.id.spinnerSchedule)
        val locInput = findViewById<EditText>(R.id.editLocation)
        val btnSchedule = findViewById<Button>(R.id.buttonSchedule)

        dateInput.minDate = System.currentTimeMillis() - 1000;

        val pattern = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(Date(dateInput.date))

        dateVal = date
        Log.i("Testing",dateVal)

        dateInput.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            dateVal = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            Log.i("Testing",dateVal)
        }

        val typesList = db.viewSet("type")
        var spinnerValueType = "Basketball"

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
                    // write code to perform some action
                }
            }
        }

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
                finish()
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkErrors(): Boolean {
        var error = false
        val pattern = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = dateVal

        if(date == simpleDateFormat.format(Date())){
            Log.i("Testing", LocalTime.now().toString())
            val time = LocalTime.parse(timeInput.text)
            if(time <= LocalTime.now()){
                Toast.makeText(this,"Please select time in the future",Toast.LENGTH_LONG).show()
                Log.i("Testing", "true")
                error = true
            }
        }

        val regex = "^([0-1][0-9]|2[0-3]):[0-5][0-9]\$".toRegex()
        if (!timeInput.text.toString().matches(regex)) {
            timeInput.error = "Please enter 24hr time as HH:MM e.g. 07:59"
            error = true
        }
        return error
    }
}