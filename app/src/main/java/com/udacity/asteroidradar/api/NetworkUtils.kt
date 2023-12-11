package com.udacity.asteroidradar.api

import android.annotation.SuppressLint
import android.util.Log
import com.udacity.asteroidradar.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.udacity.asteroidradar.data.entities.DbAsteroid
import org.json.JSONObject

private const val TAG = "AsteroidDebug"

fun parseAllAsteroidsJsonResult(jsonResult: JSONObject): List<DbAsteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")
    val asteroidList = mutableListOf<DbAsteroid>()
    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()

    nextSevenDaysFormattedDates.forEach { date ->

        if (nearEarthObjectsJson.has(date)) {
            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(date)

            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)

                Log.i(TAG, "Parsing asteroid for date $date: $asteroidJson")

                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter =
                    asteroidJson.getJSONObject("estimated_diameter").getJSONObject("kilometers")
                        .getDouble("estimated_diameter_max")
                val closeApproachData =
                    asteroidJson.getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth =
                    closeApproachData.getJSONObject("miss_distance").getDouble("astronomical")
                val isPotentiallyHazardous =
                    asteroidJson.getBoolean("is_potentially_hazardous_asteroid")

                val asteroid = DbAsteroid(
                    id,
                    codename,
                    date,
                    absoluteMagnitude,
                    estimatedDiameter,
                    relativeVelocity,
                    distanceFromEarth,
                    isPotentiallyHazardous
                )
                asteroidList.add(asteroid)
            }
        }
    }

    return asteroidList
}

@Suppress("DEPRECATION")
@SuppressLint("WeekBasedYear")
private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    calendar.time = Date(2022 - 1900, 8 - 1, 17)

    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)

        Log.i(TAG, "Generated dates: $formattedDateList")

    }

    return formattedDateList
}