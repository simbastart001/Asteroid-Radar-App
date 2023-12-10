package com.udacity.asteroidradar.screens.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.Network.asteroids
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.domain.PictureOfDay
import com.udacity.asteroidradar.data.entities.DbAsteroid
import com.udacity.asteroidradar.data.entities.asDomainModel
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.data.sourceoftruth.getDatabase
import com.udacity.asteroidradar.network.asDomainModel
import com.udacity.asteroidradar.utils.Constants.API_KEY
import kotlinx.coroutines.launch


private const val TAG = "AsteroidBug"

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _navigateToSelectedProperty = MutableLiveData<Asteroid>()
    val navigateToSelectedProperty: LiveData<Asteroid>
        get() = _navigateToSelectedProperty

    private val _entityAsteroids = MutableLiveData<List<DbAsteroid>>()

    private val _imagePproperty = MutableLiveData<PictureOfDay>()

    val imagePproperty: MutableLiveData<PictureOfDay>
        get() = _imagePproperty

    private val _properties = MutableLiveData<List<Asteroid>>()

    val properties: LiveData<List<Asteroid>>
        get() = _properties

    init {

        viewModelScope.launch {
            try {
                val pictureOfDay = asteroidRepository.getImage()
                _imagePproperty.value = pictureOfDay
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching image property: ${e.message}")
            }
        }

        viewModelScope.launch {
            database.asteroidsDao.getAllAsteroids().observeForever { dbAsteroids ->
                _entityAsteroids.value = dbAsteroids
                _properties.value = dbAsteroids?.asDomainModel()
            }
        }

        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToSelectedProperty.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null!!
    }

}
