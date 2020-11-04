package com.example.journeyfit_customapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//data class to easily pass to other activities
@Parcelize
data class Activity(
    var date: String,
    var type: String,
    var time: String,
    var distance: Double?,
    var metric: Boolean?,
    var feel: Int,
    var location: String?,
    var comments: String,
    var id: Int?,
    var status: Int
) : Parcelable {
}