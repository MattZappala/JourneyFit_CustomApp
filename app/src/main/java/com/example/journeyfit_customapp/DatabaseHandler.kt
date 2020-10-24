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
        private const val DATABASE_NAME = "ActDatabase"
        const val TABLE_ACTIVITY = "ActivityTable"
        private const val KEY_DATE = "date"
        private const val KEY_TYPE = "type"
        private const val KEY_TIME = "time"
        private const val KEY_DIST = "distance"
        private const val KEY_FEEL = "feel"
        private const val KEY_LOCATION = "location"
        private const val KEY_COMMENTS = "comments"
        private const val KEY_ID = "id"
        private const val KEY_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_ACTIVITY_TABLE = ("CREATE TABLE " + TABLE_ACTIVITY + "("
                + KEY_ID +" INTEGER NOT NULL PRIMARY KEY," + KEY_DATE + " TEXT,"
                + KEY_TYPE + " TEXT," + KEY_TIME + " TEXT," + KEY_DIST + " REAL,"
                + KEY_FEEL + " INTEGER," + KEY_LOCATION + " TEXT,"
                + KEY_COMMENTS + " TEXT," + KEY_STATUS+ " INTEGER" +")")
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
        contentValues.put(KEY_DATE, act.date)
        contentValues.put(KEY_TYPE, act.type)
        contentValues.put(KEY_TIME, act.time)
        contentValues.put(KEY_DIST, act.distance)
        contentValues.put(KEY_FEEL, act.feel)
        contentValues.put(KEY_LOCATION, act.location)
        contentValues.put(KEY_COMMENTS, act.comments)
        contentValues.put(KEY_STATUS, act.status)
        //contentValues.put(KEY_ID,act.id)

        // Inserting Row
        val success = db.insert(TABLE_ACTIVITY, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //method to read data
    fun viewActivity(query:String):List<Activity>{
        val actList:ArrayList<Activity> = ArrayList<Activity>()
        var selectQuery = query
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
        var aId: Int
        var aStatus: Int
        if (cursor.moveToFirst()) {
            do {
                aDate = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                aType = cursor.getString(cursor.getColumnIndex(KEY_TYPE))
                aTime = cursor.getString(cursor.getColumnIndex(KEY_TIME))
                aDist = cursor.getDouble(cursor.getColumnIndex(KEY_DIST))
                aFeel = cursor.getInt(cursor.getColumnIndex(KEY_FEEL))
                aLoc = cursor.getString(cursor.getColumnIndex(KEY_LOCATION))
                aCom = cursor.getString(cursor.getColumnIndex(KEY_COMMENTS))
                aId = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                aStatus = cursor.getInt(cursor.getColumnIndex(KEY_STATUS))
                val act= Activity(aDate,aType,aTime,aDist,null,aFeel,aLoc,aCom,aId,aStatus)
                actList.add(act)
            } while (cursor.moveToNext())
        }
        return actList
    }

    fun viewSet(key:String):List<String>{
        val actList:ArrayList<String> = ArrayList<String>()
        val selectQuery = "SELECT DISTINCT $key FROM $TABLE_ACTIVITY"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var value: String
        if (cursor.moveToFirst()) {
            do {
                value = cursor.getString(cursor.getColumnIndex(key))
                actList.add(value)
            } while (cursor.moveToNext())
        }
        return actList
    }

    //method to delete data
    fun deleteActivity(act: Activity):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, act.id) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_ACTIVITY,"id="+act.id,null)
        db.close() // Closing database connection
        return success
    }

    //method to update data
    fun updateActivity(act: Activity):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_DATE, act.date)
        contentValues.put(KEY_TYPE, act.type)
        contentValues.put(KEY_TIME, act.time)
        contentValues.put(KEY_DIST, act.distance)
        contentValues.put(KEY_FEEL, act.feel)
        contentValues.put(KEY_LOCATION, act.location)
        contentValues.put(KEY_COMMENTS, act.comments)
        contentValues.put(KEY_STATUS, act.status)
        // Updating Row
        val success = db.update(TABLE_ACTIVITY, contentValues,"id="+act.id,null)
        db.close() // Closing database connection
        return success
    }
}

