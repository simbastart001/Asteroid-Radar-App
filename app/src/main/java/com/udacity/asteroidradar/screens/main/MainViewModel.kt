package com.udacity.asteroidradar.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.AsteroidsFilter
import com.udacity.asteroidradar.api.Network.asteroids
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.domain.PictureOfDay
import com.udacity.asteroidradar.data.entities.DbAsteroid
import com.udacity.asteroidradar.data.entities.asDomainModel
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.data.sourceoftruth.getDatabase
import com.udacity.asteroidradar.utils.Constants.API_KEY
import com.udacity.asteroidradar.utils.Constants.endDateForWeek
import com.udacity.asteroidradar.utils.Constants.startDateForWeek
import com.udacity.asteroidradar.utils.Constants.today
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

private const val TAG = "TheAsteroidBug"

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    private val _entityAsteroids = MutableLiveData<List<DbAsteroid>>()
    private val _imagePproperty = MutableLiveData<PictureOfDay>()
    val imagePproperty: MutableLiveData<PictureOfDay>
        get() = _imagePproperty
    private val _properties = MutableLiveData<List<Asteroid>>()
    val properties: LiveData<List<Asteroid>>
        get() = _properties

    private val _weeklyAsteroids = MutableLiveData<List<DbAsteroid>>()
    val weeklyAsteroids: LiveData<List<DbAsteroid>> get() = _weeklyAsteroids

    private val _todayAsteroids = MutableLiveData<List<DbAsteroid>>()
    val todayAsteroids: LiveData<List<DbAsteroid>> get() = _todayAsteroids
    private val _filter = MutableLiveData<AsteroidsFilter>()
    val filter: LiveData<AsteroidsFilter>
        get() = _filter

    init {

        viewModelScope.launch {
            try {
                val pictureOfDay = asteroidRepository.getImage()
                _imagePproperty.value = pictureOfDay
                Timber.tag(TAG).i("picture of the day is       ::    %s", pictureOfDay)
            } catch (e: Exception) {
                Timber.tag(TAG).e("Error fetching image property: %s", e.message)
            }
        }

        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }

        viewModelScope.launch {
            updateAsteroidFilter(AsteroidsFilter.SHOW_SAVED)
        }

    }

    //    TODO @SimbaStart:     navigating of Asteroid item in the recyclerView
    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid
        get() = _navigateToAsteroid

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroid.value = null
    }

    //    TODO @SimbaStart:     method to retrieve Asteroids by filtering...
    fun updateAsteroidFilter(filter: AsteroidsFilter) {
        asteroidRepository.getWeeklyAsteroids().observeForever { _weeklyAsteroids.value = it }
        asteroidRepository.getTodayAsteroids().observeForever { _todayAsteroids.value = it }
        asteroidRepository.getAllAsteroids(filter).observeForever {
            _entityAsteroids.value = it
            _properties.value = it?.asDomainModel()
        }
    }

}
