package com.udacity.asteroidradar.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.Network.asteroids
import com.udacity.asteroidradar.api.parseAllAsteroidsJsonResult
import com.udacity.asteroidradar.data.dao.PictureOfDayDao
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.domain.PictureOfDay
import com.udacity.asteroidradar.data.entities.DbPictureOfDay
import com.udacity.asteroidradar.data.entities.asDomainModel
import com.udacity.asteroidradar.data.sourceoftruth.AsteroidsDatabase
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.utils.Constants.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val TAG = "AsteroidDebug"

class AsteroidRepository(private val database: AsteroidsDatabase) {
    var count = 0L
    suspend fun getImage(): PictureOfDay {
        return withContext(Dispatchers.IO) {
            val cachedPictureOfDay = database.pictureOfDayDao.getPictureOfDay(CACHED_TITLE)

            if (cachedPictureOfDay != null) {
                // If data is available in the local database, return it
                PictureOfDay(
                    cachedPictureOfDay.mediaType, cachedPictureOfDay.title, cachedPictureOfDay.url
                )
            } else {
                // Fetch data from the network
                val pictureOfDay = asteroids.imageOfTheDay(API_KEY)
                count++

                // Save the fetched data to the local database
                database.pictureOfDayDao.insert(
                    DbPictureOfDay(
                        count, pictureOfDay.mediaType, pictureOfDay.title, pictureOfDay.url
                    )
                )
                pictureOfDay
            }
        }
    }

    companion object {
        private const val CACHED_TITLE = "cached_title"
    }

//    suspend fun getImage(): PictureOfDay {
//        return withContext(Dispatchers.IO) {
//            asteroids.imageOfTheDay(API_KEY)
//        }
//    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val response = asteroids.getAsteroids(
                    startDate = "2022-08-17", endDate = "2022-08-18", apiKey = API_KEY
                )

                Log.i(TAG, "Response: $response")

                if (response.isSuccessful) {
                    val jsonResult = JSONObject(response.body() ?: "")
                    Log.i(TAG, "jsonResult received :: $jsonResult")

                    // Check if the "near_earth_objects" key is present in the JSON
                    if (jsonResult.has("near_earth_objects")) {
                        val parsedAsteroids = parseAllAsteroidsJsonResult(jsonResult)
                        Log.i(TAG, "parsedAsteroids      ::     $parsedAsteroids")
                        database.asteroidsDao.insertAll(*parsedAsteroids.toTypedArray())
                        Log.i(TAG, "insert success")
                    } else {
                        Log.i(TAG, "No value for near_earth_objects")
                    }
                } else {
                    Log.i(TAG, "API request failed with code ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle the exception
                Log.i(TAG, "failed to insert due to ${e.message}")
            }
        }
    }

}