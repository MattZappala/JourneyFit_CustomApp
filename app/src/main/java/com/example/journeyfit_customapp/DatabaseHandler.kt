package com.example.journeyfit_customapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

//Source: https://www.javatpoint.com/kotlin-android-sqlite-tutorial

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_ACTIVITY = "ActivityTable"
        private val KEY_DATE = "date"
        private val KEY_TYPE = "type"
        private val KEY_TIME = "time"
        private val KEY_DIST = "distance"
        private val KEY_FEEL = "feel"
        private val KEY_LOCATON = "location"
        private val KEY_COMMENTS = "comments"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_ACTIVITY_TABLE = ("CREATE TABLE " + TABLE_ACTIVITY + "("
                + "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT,"
                + KEY_TYPE + " TEXT," + KEY_TIME + " TEXT," + KEY_DIST + " REAL,"
                + KEY_FEEL + " INTEGER," + KEY_LOCATON + " TEXT,"
                + KEY_COMMENTS + " TEXT" + ")")
        db?.execSQL(CREATE_ACTIVITY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_ACTIVITY")
        onCreate(db)
    }

    //method to insert data
    fun addActivity(act: Activity):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_DATE, act.date.toString())
        contentValues.put(KEY_TYPE, act.type)
        contentValues.put(KEY_TIME, act.time.toString())
        contentValues.put(KEY_DIST, act.distance)
        contentValues.put(KEY_FEEL, act.feel)
        contentValues.put(KEY_LOCATON, act.location.toString())
        contentValues.put(KEY_COMMENTS, act.comments)

        // Inserting Row
        val success = db.insert(TABLE_ACTIVITY, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //method to read data
    fun viewActivity():List<Activity>{
        val actList:ArrayList<Activity> = ArrayList<Activity>()
        val selectQuery = "SELECT  * FROM $TABLE_ACTIVITY"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var aDate: String
        var aType: String
        var aTime: String
        var aDist: Double
        var aFeel: Int
        var aLoc: String
        var aCom: String
        if (cursor.moveToFirst()) {
            do {
                aDate = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                aType = cursor.getString(cursor.getColumnIndex(KEY_TYPE))
                aTime = cursor.getString(cursor.getColumnIndex(KEY_TIME))
                aDist = cursor.getDouble(cursor.getColumnIndex(KEY_DIST))
                aFeel = cursor.getInt(cursor.getColumnIndex(KEY_FEEL))
                aLoc = cursor.getString(cursor.getColumnIndex(KEY_LOCATON))
                aCom = cursor.getString(cursor.getColumnIndex(KEY_COMMENTS))
                val act= Activity(aDate,aType,aTime,aDist,null,aFeel,aLoc,aCom)
                actList.add(act)
            } while (cursor.moveToNext())
        }
        return actList
    }
}

