package com.example.journeyfit_customapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class ScheduleActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHandler
    private lateinit var timeInput: EditText
    private lateinit var dateInput: CalendarView
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yy")
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

        val typesList = db.viewSet("type")

        if (spin != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, typesList)
            spin.adapter = adapter
            spin.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    val spinnerValueType = typesList[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        btnSchedule.setOnClickListener {
            if (!checkErrors()) {
                Log.i("Testing",
                    Date(dateInput.date).toString()
                )
                finish()
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkErrors(): Boolean {
        var error = false
        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(Date(dateInput.date))

        if(date == simpleDateFormat.format(Date())){
            Log.i("Testing", "true")
        }

        val regex = "^([0-9]*):[0-5][0-9]$".toRegex()
        if (!timeInput.text.toString().matches(regex)) {
            timeInput.error = "Please enter time as HH:MM"
            error = true
        }
        return error
    }
    //regex for time ^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$ 00:00 to 24:59
}