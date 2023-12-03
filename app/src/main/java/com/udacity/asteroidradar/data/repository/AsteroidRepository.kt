package com.udacity.asteroidradar.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.Network

import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.entities.asDomainModel
import com.udacity.asteroidradar.data.sourceoftruth.AsteroidsDatabase
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.utils.Constants.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepository(private val database: AsteroidsDatabase) {
    val asteroids: LiveData<List<Asteroid>> =
        database.asteroidsDao.getAllAsteroids().map { asteroids ->
            asteroids.asDomainModel()
        }
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val playlist = Network.asteroidApiService.getPlaylist().await()
//            val playlist = Network.asteroidApiService.getAsteroids(
//            startDate = "2023-12-2",
//                endDate = "2023-12-3",
//                apiKey = API_KEY,
//            ).await()
            database.asteroidsDao.insertAll(*playlist.asDatabaseModel())
        }
    }
}
