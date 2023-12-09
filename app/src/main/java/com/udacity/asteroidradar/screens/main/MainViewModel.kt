package com.udacity.asteroidradar.screens.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.Network.asteroids
import com.udacity.asteroidradar.data.domain.Asteroid
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

    private val _properties = MutableLiveData<List<Asteroid>>()

    val properties: LiveData<List<Asteroid>>
        get() = _properties

    init {
        viewModelScope.launch {
            database.asteroidsDao.getAllAsteroids().observeForever { dbAsteroids ->
                _entityAsteroids.value = dbAsteroids
                // Update _properties here if needed
                _properties.value = dbAsteroids?.asDomainModel()
            }
        }

        viewModelScope.launch {
            // Fetch data from the repository and update _properties
            asteroidRepository.refreshAsteroids()
        }
    }

    // ... (other functions)

    // Function to check if properties has the same information as _entityAsteroids
    fun checkIfPropertiesMatchEntityAsteroids(): Boolean {
        val propertiesList = _properties.value
        val entityAsteroidsList = _entityAsteroids.value

        // Check if both lists are not null
        if (propertiesList != null && entityAsteroidsList != null) {
            // Convert entityAsteroidsList to List<Asteroid>
            val entityAsteroidsAsAsteroids = entityAsteroidsList.asDomainModel()

            // Compare the two lists
            return propertiesList == entityAsteroidsAsAsteroids
        }

        // Handle the case where one or both lists are null
        return false
    }

    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToSelectedProperty.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null!!
    }

}
