package com.nalldev.gent.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_event_bookmark")
data class EventBookmarkEntity(
    @PrimaryKey
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
)
