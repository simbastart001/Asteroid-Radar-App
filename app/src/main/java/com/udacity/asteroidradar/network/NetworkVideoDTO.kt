package com.udacity.asteroidradar.network

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.entities.DbAsteroid

@JsonClass(generateAdapter = true)
data class NetworkAsteroids(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

@JsonClass(generateAdapter = true)
data class NetworkAsteroidsContainer(val asteroidList: List<NetworkAsteroids>)

//TODO @SimbaStart:      Convert Network results to database objects
fun NetworkAsteroidsContainer.asDomainModel(): List<Asteroid> {
    return asteroidList.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun NetworkAsteroidsContainer.asDatabaseModel(): Array<DbAsteroid> {
    return asteroidList.map {
        DbAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
