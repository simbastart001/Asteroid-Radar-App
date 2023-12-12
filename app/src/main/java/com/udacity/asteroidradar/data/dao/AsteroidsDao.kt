package com.udacity.asteroidradar.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.entities.DbAsteroid

@Dao
interface AsteroidsDao {
    @Query("select * from asteroids order by closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<DbAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DbAsteroid)

    @Query("DELETE FROM asteroids WHERE closeApproachDate < :date")
    suspend fun deletePreviousDayAsteroids(date: String)

    @Query("SELECT * FROM asteroids WHERE closeApproachDate = :today ORDER BY closeApproachDate ASC")
    fun getTodayAsteroids(today: String): LiveData<List<DbAsteroid>>

    @Query("SELECT * FROM asteroids WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate ASC")
    fun getWeeklyAsteroids(startDate: String, endDate: String): LiveData<List<DbAsteroid>>

}
