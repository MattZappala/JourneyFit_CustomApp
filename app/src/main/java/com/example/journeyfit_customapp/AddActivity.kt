package com.example.journeyfit_customapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

//Spinner: https://developer.android.com/guide/topics/ui/controls/spinner

class AddActivity : AppCompatActivity(){
    var activitiesList = mutableListOf<String>( "Running", "Walking", "Basketball", "Tennis")
    lateinit var spinnerValue: String
    lateinit var vActivity: Activity
    var activityId: Int? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individual_activity)

        val db = DatabaseHandler(this)

        val dateInput = findViewById<EditText>(R.id.editDate)
        val timeInput = findViewById<EditText>(R.id.editTime)
        val distInput = findViewById<EditText>(R.id.editDistance)
        val feelInput = findViewById<SeekBar>(R.id.seekBarFeel)
        val locInput = findViewById<EditText>(R.id.editLocation)
        val comInput = findViewById<EditText>(R.id.editComments)
        val otherInput = findViewById<EditText>(R.id.editOtherActivityInput)
        val btnSubmit = findViewById<Button>(R.id.buttonSubmit)
        // access the spinner
        val spinner = findViewById<Spinner>(R.id.spinner)

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
            distInput.setText(vActivity.distance.toString()) //TODO: change distance text to double not int
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
            val type = if(otherInput.isEnabled){
                otherInput.text.toString()
            }else{
                spinnerValue
            }
            if(btnSubmit.text == "Submit") {
                db.addActivity(
                    Activity(
                        dateInput.text.toString(),
                        type,
                        timeInput.text.toString(),
                        distInput.text.toString().toDouble(),
                        true,
                        feelInput.progress,
                        locInput.text.toString(),
                        comInput.text.toString(),
                        null
                    )
                )
            } else {
                val result = db.updateActivity(
                    Activity(
                        dateInput.text.toString(),
                        type,
                        timeInput.text.toString(),
                        distInput.text.toString().toDouble(),
                        true,
                        feelInput.progress,
                        locInput.text.toString(),
                        comInput.text.toString(),
                        vActivity.id
                    )
                )
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