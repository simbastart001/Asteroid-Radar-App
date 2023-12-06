package com.udacity.asteroidradar.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.data.sourceoftruth.getDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    val playlist = asteroidRepository.asteroids
}
