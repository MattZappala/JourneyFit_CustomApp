package com.example.journeyfit_customapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*

//for images https://material.io/resources/icons/?style=baseline

class MainActivity() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //set up support for navigation drawer
        setSupportActionBar(toolbar)
        //set up nav drawer
        drawer = findViewById(R.id.drawer_layout)
        //set up toggle button in top left to open and close drawer
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav)
        //add nav listener
        navigationView.setNavigationItemSelectedListener(this)

        //setup image widgets
        val imageAdd = findViewById<ImageView>(R.id.imageAdd)
        val imageList = findViewById<ImageView>(R.id.imageList)
        val imageSchedule = findViewById<ImageView>(R.id.imageSchedule)

        //used to populate the database for testing purposes
        //val db = DatabaseHandler(this)
        //test data
        //testData(db)

        //add click listeners to all images
        imageAdd.setOnClickListener {
            //create intent to move to another activity
            val intent = Intent(this,AddActivity::class.java)
            //go to other activity
            startActivity(intent)
        }

        imageList.setOnClickListener {
            val intent = Intent(this,ViewActivity::class.java)
            startActivity(intent)
        }

        imageSchedule.setOnClickListener {
            val intent = Intent(this,ScheduleActivity::class.java)
            startActivity(intent)
        }
    }

    //data for testing
    private fun testData(db: DatabaseHandler) {
        db.addActivity(Activity("1-10-20","Basketball","1:00",11.0,true,3,"kew","fun",null,1))
        db.addActivity(Activity("2-10-20","Tennis","2:30",24.5,true,4,"East kew","Very fun",null,1))
        db.addActivity(Activity("3-10-20","Squash","12:00",12.2,true,5,"Hawthorne","Good times",null,1))
        db.addActivity(Activity("4-10-20","Squash","11:00",1.2,true,1,"Hawthorne East","Times",null,1))
        db.addActivity(Activity("5-10-20","Walking","1:00",11.0,true,3,"kew","fun",null,1))
        db.addActivity(Activity("6-10-20","Yoga","2:30pm",24.5,true,4,"East kew","Very fun",null,1))
        db.addActivity(Activity("30-10-20","Basketball","12:00",12.2,true,5,"Hawthorne","Good times",null,0))
        db.addActivity(Activity("8-10-20","Table Tennis","11:00",1.2,true,1,"Malvern","Times",null,1))
        db.addActivity(Activity("9-10-20","Yoga","1:00",11.0,true,3,"kew","fun",null,1))
        db.addActivity(Activity("10-10-20","Rock Climbing","2:30",24.5,true,4,"Camberwell","Very fun",null,1))
        db.addActivity(Activity("11-10-20","Wrestling","12:00",12.2,true,5,"Camberwell","Good times",null,1))
    }

    //used to sync state of toggle button
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    //when new configuration is set
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    //when toggle button is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //when item is selected in nav menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //determine which menu item is selected and go to page
        when (item.itemId) {
            R.id.main -> Toast.makeText(this, "On Home Page", Toast.LENGTH_SHORT).show()
            R.id.add_activity -> intent = Intent(this,AddActivity::class.java)
            R.id.list -> intent = Intent(this,ViewActivity::class.java)
            R.id.schedule -> intent = Intent(this,ScheduleActivity::class.java)
        }
        //close the drawer
        drawer.closeDrawer(GravityCompat.START)
        //move to page
        startActivity(intent)
        return true
    }
}