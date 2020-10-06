package com.example.journeyfit_customapp

data class Activity(
    var date: String,
    var type: String,
    var time: String,
    var distance: Double,
    var metric: Boolean?,
    var feel: Int,
    var location: String?,
    var comments: String?
                    ) {
}