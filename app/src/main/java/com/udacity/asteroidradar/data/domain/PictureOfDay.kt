package com.udacity.asteroidradar.data.domain

import com.squareup.moshi.Json

data class PictureOfDay(
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
)
