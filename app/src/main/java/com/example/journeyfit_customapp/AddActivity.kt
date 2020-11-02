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
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//Spinner: https://developer.android.com/guide/topics/ui/controls/spinner

class AddActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    var activitiesList = mutableListOf<String>( "Running", "Walking", "Basketball", "Tennis")
    lateinit var spinnerValue: String
    lateinit var vActivity: Activity
    var activityId: Int? = null
    lateinit var dateInput: EditText
    lateinit var timeInput: EditText
    lateinit var distInput: EditText
    lateinit var feelInput: SeekBar
    lateinit var locInput: EditText
    lateinit var comInput: EditText
    lateinit var otherInput: EditText
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yy")

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individual_activity)

        val db = DatabaseHandler(this)

        dateInput = findViewById<EditText>(R.id.editDate)
        timeInput = findViewById<EditText>(R.id.editTime)
        distInput = findViewById<EditText>(R.id.editDistance)
        feelInput = findViewById<SeekBar>(R.id.seekBarFeel)
        locInput = findViewById<EditText>(R.id.editLocation)
        comInput = findViewById<EditText>(R.id.editComments)
        otherInput = findViewById<EditText>(R.id.editOtherActivityInput)
        val btnSubmit = findViewById<Button>(R.id.buttonSubmit)
        // access the spinner
        val spinner = findViewById<Spinner>(R.id.spinner)


        val currentDate = LocalDateTime.now()
        dateInput.hint = currentDate.format(formatter)

        val typesList = db.viewSet("type")
        Log.i("Testing", typesList.toString())
        typesList.forEach {
            if(!activitiesList.contains(it)){
                activitiesList.add(it)
            }
        }
        activitiesList.add("Other")

        otherInput.isEnabled = false

        var type = "Running"

        if(intent.getParcelableExtra<Activity>("activity") != null) {
            vActivity = intent.getParcelableExtra<Activity>("activity")!!
            dateInput.setText(vActivity.date)
            type = vActivity.type
            Log.i("Testing",type)
            timeInput.setText(vActivity.time)
            distInput.setText(vActivity.distance.toString())
            feelInput.progress = vActivity.feel
            locInput.setText(vActivity.location)
            comInput.setText(vActivity.comments)
            activityId = vActivity.id
            btnSubmit.text = "update"
        }

        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, activitiesList)
            spinner.adapter = adapter
            val index = activitiesList.indexOf(type)
            Log.i("Testing",index.toString())
            spinner.setSelection(index)
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>,
                                                view: View, position: Int, id: Long) {
                        //Toast.makeText(this@AddActivity,activitiesList[position], Toast.LENGTH_SHORT).show()
                        spinnerValue = activitiesList[position]
                        if (spinnerValue == "Other"){
                            otherInput.isEnabled = true
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
            }
        }

        btnSubmit.setOnClickListener {
            val mType = if(otherInput.isEnabled){
                otherInput.text.toString()
            }else{
                spinnerValue
            }
            val dist = if(distInput.text.toString() != ""){
                distInput.text.toString()
            } else {
                "0.0"
            }
            val date = if(dateInput.text.toString() != ""){
                dateInput.text.toString()
            } else {
                dateInput.hint.toString()
            }
            if (!checkErrors()) {
                if(btnSubmit.text == "Submit") {
                    db.addActivity(
                        Activity(
                            date,
                            mType,
                            timeInput.text.toString(),
                            dist.toDouble(),
                            true,
                            feelInput.progress,
                            locInput.text.toString(),
                            comInput.text.toString(),
                            null,
                            1
                        )
                    )
                } else {
                    val result = db.updateActivity(
                        Activity(
                            date,
                            mType,
                            timeInput.text.toString(),
                            dist.toDouble(),
                            true,
                            feelInput.progress,
                            locInput.text.toString(),
                            comInput.text.toString(),
                            vActivity.id,
                            1
                        )
                    )
                    Log.i("Testing",Activity(date, mType,timeInput.text.toString(),dist.toDouble(),true, feelInput.progress,locInput.text.toString(), comInput.text.toString(), vActivity.id,1).toString())
                    val stringRes = if (result == 1){
                        "Update Successful"
                    } else  {
                        "Update Failed"
                    }
                    Toast.makeText(this,stringRes,Toast.LENGTH_LONG).show()
                }
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkErrors(): Boolean {
        var error = false
        try{
            if(dateInput.text.toString() != ""){
                if(LocalDate.parse(dateInput.text,formatter) > LocalDate.now()){
                    dateInput.error = "Input current or past date"
                    error = true
                }
            } else {
                LocalDate.parse(dateInput.hint,formatter).toString()
                if(LocalDate.parse(dateInput.hint,formatter) > LocalDate.now()){
                    dateInput.error = "Input current or past date"
                    error = true
                }
            }
        } catch (e: DateTimeException){
            Log.i("Testing", "DateTimeException Thrown")
            dateInput.error = "Must be in format DD-MM-YY"
            error = true
        }
        val regex = "^([0-9]*):[0-5][0-9]$".toRegex()
        if (!timeInput.text.toString().matches(regex)) {
            timeInput.error = "Please time spent as HH:MM e.g. 1 hour and 30 mins = 1:30"
            error = true
        }
        return error
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}