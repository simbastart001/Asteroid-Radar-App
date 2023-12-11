package com.udacity.asteroidradar.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkPictureOfDay(
    val mediaType: String,
    val title: String,
    val url: String
)