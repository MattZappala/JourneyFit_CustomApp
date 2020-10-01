package com.example.journeyfit_customapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

//Spinner: https://developer.android.com/guide/topics/ui/controls/spinner

class AddActivity : AppCompatActivity(){
    val activitiesList = listOf<String>( "Running", "Walking", "Basketball", "Tennis", "Other")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individual_activity)

        val btnSubmit = findViewById<Button>(R.id.buttonSubmit)

        btnSubmit.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

         // access the spinner
         val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, activitiesList)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>,
                                                view: View, position: Int, id: Long) {
                        Toast.makeText(this@AddActivity,activitiesList[position], Toast.LENGTH_SHORT).show()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
            }
        }
    }
}