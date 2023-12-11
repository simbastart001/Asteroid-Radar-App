package com.udacity.asteroidradar.data.dto

import com.udacity.asteroidradar.data.domain.PictureOfDay
import com.udacity.asteroidradar.data.entities.DbPictureOfDay
import com.udacity.asteroidradar.network.NetworkPictureOfDay

data class PictureOfDayDTO(
    val id: Long = 0, val mediaType: String, val title: String, val url: String
) {
    companion object {
        fun fromDomainModel(domainModel: PictureOfDay): PictureOfDayDTO {
            return PictureOfDayDTO(
                mediaType = domainModel.mediaType, title = domainModel.title, url = domainModel.url
            )
        }

        fun fromEntityModel(entityModel: DbPictureOfDay): PictureOfDayDTO {
            return PictureOfDayDTO(
                id = entityModel.id,
                mediaType = entityModel.mediaType,
                title = entityModel.title,
                url = entityModel.url
            )
        }

        fun toEntityModel(dto: PictureOfDayDTO): DbPictureOfDay {
            return DbPictureOfDay(
                id = dto.id, mediaType = dto.mediaType, title = dto.title, url = dto.url
            )
        }

        fun fromNetworkModel(networkModel: NetworkPictureOfDay): PictureOfDayDTO {
            return PictureOfDayDTO(
                mediaType = networkModel.mediaType,
                title = networkModel.title,
                url = networkModel.url
            )
        }
    }
}
