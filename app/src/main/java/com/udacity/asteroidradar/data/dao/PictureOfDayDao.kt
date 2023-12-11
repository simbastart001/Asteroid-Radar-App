package com.udacity.asteroidradar.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.entities.DbPictureOfDay

//
//@Dao
//interface PictureOfDayDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(vararg pictureOfDay: DbPictureOfDay)
//
//    @Query("SELECT * FROM picture_of_day_table LIMIT 1")
//    fun getPictureOfDay(): LiveData<List<DbPictureOfDay>>
//
//}


@Dao
interface PictureOfDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pictureOfDayEntity: DbPictureOfDay)

    @Query("SELECT * FROM picture_of_day_table WHERE title = :title")
    suspend fun getPictureOfDay(title: String): DbPictureOfDay?

}