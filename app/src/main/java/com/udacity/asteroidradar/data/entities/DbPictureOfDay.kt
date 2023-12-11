package com.udacity.asteroidradar.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_of_day_table")
data class DbPictureOfDay(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mediaType: String,
    val title: String,
    val url: String
)

