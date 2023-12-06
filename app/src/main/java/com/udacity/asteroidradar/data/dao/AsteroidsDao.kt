package com.udacity.asteroidradar.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.entities.DbAsteroid

@Dao
interface AsteroidsDao {
    @Query("select * from asteroids")
    fun getAllAsteroids(): LiveData<List<DbAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DbAsteroid)
}
