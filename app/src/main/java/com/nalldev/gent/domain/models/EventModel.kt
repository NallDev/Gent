package com.nalldev.gent.domain.models

data class EventModel(
    val id: Int,
    val name : String,
    val summary : String,
    val description : String,
    val image : String,
    val ownerName : String,
    val cityName : String,
    val beginTime : String,
    val endTime : String,
    val category : String,
    val quota : Int,
    val registrants : Int,
    val link : String,
    val isBookmark: Boolean
)