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

class AddActivity : AppCompatActivity(){
    //create initial list for first time app users
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
        //set up inputs
        dateInput = findViewById<EditText>(R.id.editDate)
        timeInput = findViewById<EditText>(R.id.editTime)
        distInput = findViewById<EditText>(R.id.editDistance)
        feelInput = findViewById<SeekBar>(R.id.seekBarFeel)
        locInput = findViewById<EditText>(R.id.editLocation)
        comInput = findViewById<EditText>(R.id.editComments)
        otherInput = findViewById<EditText>(R.id.editOtherActivityInput)
        val btnSubmit = findViewById<Button>(R.id.buttonSubmit)

        //initial hint set to current date so user does not need to enter date if they had just completed the activity
        val currentDate = LocalDateTime.now()
        //format the date a specific way
        dateInput.hint = currentDate.format(formatter)

        //eturn string list of unique activity types in the database
        val typesList = db.viewSet("type")
        Log.i("Testing", typesList.toString())
        //add them to the list for the spinner
        typesList.forEach {
            //if it already contains the string do not add it again
            if(!activitiesList.contains(it)){
                activitiesList.add(it)
            }
        }
        //add option for user to enter custom activity
        activitiesList.add("Other")
        //don't let user edit otherInput unless "Other" is selected in spinner (executed later)
        otherInput.isEnabled = false
        //set initial type variable as must not be null
        var type = "Running"

        //check to see if activity was started via the view activity page and that data has been passed to this activity
        if(intent.getParcelableExtra<Activity>("activity") != null) {
            //add all data to be displayed as text inputs
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
        //initialize spinner adapter and set up spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
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
                        //set value to global variable
                        spinnerValue = activitiesList[position]
                        if (spinnerValue == "Other"){
                            //enable user to write in other text input in other is selected in spinner
                            otherInput.isEnabled = true
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                        //nothing
                    }
            }
        }
        //when user presses submit button do this
        btnSubmit.setOnClickListener {
            //if other input is enabled, set type to read other input else read from spinner
            val mType = if(otherInput.isEnabled){
                otherInput.text.toString()
            }else{
                spinnerValue
            }
            //if nothing entered for distance make string equal to 0.0
            val dist = if(distInput.text.toString() != ""){
                distInput.text.toString()
            } else {
                "0.0"
            }
            //if date not entered, set date value to the hint which is the current date
            val date = if(dateInput.text.toString() != ""){
                dateInput.text.toString()
            } else {
                dateInput.hint.toString()
            }
            //check errors via error function below
            if (!checkErrors()) {
                //if button is submit meaning user wants to add new activity then add activity to database
                if(btnSubmit.text == "Submit") {
                    //call addActivity and pass activity with variables in the text fields
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
                    //else update the existing activity if the user selected activity via the view activity page
                    //call updateActivity and pass activity with variables in the text fields
                    //return result value to determine if the update was successful
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
                    //set text for Toast
                    val stringRes = if (result == 1){
                        "Update Successful"
                    } else  {
                        "Update Failed"
                    }
                    //Show result of update
                    Toast.makeText(this,stringRes,Toast.LENGTH_LONG).show()
                }
                //finish activity and go back to previous page
                finish()
            }
        }
    }

    //check errors
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkErrors(): Boolean {
        var error = false
        try{
            //make sure date is in the correct format and is in the past
            if(dateInput.text.toString() != ""){
                if(LocalDate.parse(dateInput.text,formatter) > LocalDate.now()){
                    //display this error if true
                    dateInput.error = "Input current or past date"
                    error = true
                }
            } else {
               //check hint is correct format and is in the past
                if(LocalDate.parse(dateInput.hint,formatter) > LocalDate.now()){
                    dateInput.error = "Input current or past date"
                    error = true
                }
            }
        } catch (e: DateTimeException){
            //if date can not be formatted it will catch here and display this error
            Log.i("Testing", "DateTimeException Thrown")
            dateInput.error = "Must be in format DD-MM-YY"
            error = true
        }
        //create regex expression for time e.g. hh:MM
        val regex = "^([0-9]*):[0-5][0-9]$".toRegex()
        //check to see if input matches regex expression
        if (!timeInput.text.toString().matches(regex)) {
            //show error
            timeInput.error = "Please time spent as HH:MM e.g. 1 hour and 30 mins = 1:30"
            error = true
        }
        return error
    }
}