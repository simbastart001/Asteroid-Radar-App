package com.udacity.asteroidradar.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAllAsteroidsJsonResult
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.entities.asDomainModel
import com.udacity.asteroidradar.data.sourceoftruth.AsteroidsDatabase
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.utils.Constants.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val TAG = "AsteroidDebug"

class AsteroidRepository(private val database: AsteroidsDatabase) {
    val asteroids: LiveData<List<Asteroid>> = database.asteroidsDao.getAllAsteroids().map {
        it.asDomainModel()
    }


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val response = Network.asteroids.getAsteroids(
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