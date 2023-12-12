package com.udacity.asteroidradar.data.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.AsteroidsFilter
import com.udacity.asteroidradar.api.Network.asteroids
import com.udacity.asteroidradar.api.parseAllAsteroidsJsonResult
import com.udacity.asteroidradar.data.domain.PictureOfDay
import com.udacity.asteroidradar.data.entities.DbAsteroid
import com.udacity.asteroidradar.data.entities.DbPictureOfDay
import com.udacity.asteroidradar.data.sourceoftruth.AsteroidsDatabase
import com.udacity.asteroidradar.utils.Constants.endDateForWeek
import com.udacity.asteroidradar.utils.Constants.startDateForWeek
import com.udacity.asteroidradar.utils.Constants.today
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

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
                val pictureOfDay = asteroids.imageOfTheDay(BuildConfig.API_KEY)
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


    //    TODO @SimbaStart:     filtering methods to retrieve Asteroids based on "weekly / daily / saved"
    fun getWeeklyAsteroids(): LiveData<List<DbAsteroid>> {
        val startDate = startDateForWeek
        val endDate = endDateForWeek
        return database.asteroidsDao.getWeeklyAsteroids(startDate, endDate)
    }

    fun getTodayAsteroids(): LiveData<List<DbAsteroid>> {
        val today = today
        return database.asteroidsDao.getTodayAsteroids(today)
    }

    //    TODO @SimbaStart:     get corresponding enum class properties to matching Dao methods()
    fun getAllAsteroids(filter: AsteroidsFilter): LiveData<List<DbAsteroid>> {
        return when (filter) {
            AsteroidsFilter.SHOW_WEEKLY -> {
                val startDate = startDateForWeek
                val endDate = endDateForWeek
                database.asteroidsDao.getWeeklyAsteroids(startDate, endDate)
            }

            AsteroidsFilter.SHOW_DAILY -> {
                val today = today
                database.asteroidsDao.getTodayAsteroids(today)
            }

            AsteroidsFilter.SHOW_SAVED -> {
                database.asteroidsDao.getAllAsteroids()
            }
        }
    }

    //    TODO @SimbaStart:     method to get all Asteroids and save them to localDb
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val response = asteroids.getAsteroids(
                    startDate = startDateForWeek,
                    endDate = endDateForWeek,
                    apiKey = BuildConfig.API_KEY
                )
                Timber.tag(TAG).i("Response: %s", response)

                if (response.isSuccessful) {
                    val jsonResult = JSONObject(response.body() ?: "")
                    Timber.tag(TAG).i("jsonResult received :: %s", jsonResult)

                    // Check if the "near_earth_objects" key is present in the JSON
                    if (jsonResult.has("near_earth_objects")) {
                        val parsedAsteroids = parseAllAsteroidsJsonResult(jsonResult)
                        Timber.tag(TAG).i("parsedAsteroids      ::     %s", parsedAsteroids)
                        database.asteroidsDao.insertAll(*parsedAsteroids.toTypedArray())
                        Timber.tag(TAG).i("insert success")
                    } else {
                        Timber.tag(TAG).i("No value for near_earth_objects")
                    }
                } else {
                    Timber.tag(TAG).i("API request failed with code %s", response.code())
                }
            } catch (e: Exception) {
                // Handle the exception
                Timber.tag(TAG).i("failed to insert due to %s", e.message)
            }
        }
    }
}
