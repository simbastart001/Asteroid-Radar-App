package com.udacity.asteroidradar.data.sourceoftruth

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.data.dao.AsteroidsDao
import com.udacity.asteroidradar.data.dao.PictureOfDayDao
import com.udacity.asteroidradar.data.entities.DbAsteroid
import com.udacity.asteroidradar.data.entities.DbPictureOfDay

@Database(entities = [DbAsteroid::class, DbPictureOfDay::class], version = 2)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: AsteroidsDatabase
fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, AsteroidsDatabase::class.java, "asteroidsdb"
            ).build()
        }
    }
    return INSTANCE
}
