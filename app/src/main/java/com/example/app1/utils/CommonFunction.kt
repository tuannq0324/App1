package com.example.app1.utils

import com.example.app1.database.ImageEntity
import com.example.app1.model.ImageResponse
import com.example.app1.model.Urls

object CommonFunction {

    fun ImageEntity.convertToImageResponse(): ImageResponse = ImageResponse(
        imageId, Urls(urlFull, urlRaw, urlRegular, urlSmall, urlSmallS3, urlThumb), isTicked
    )

    fun List<ImageEntity>.convertToListImageResponse(): List<ImageResponse> {
        val list = arrayListOf<ImageResponse>()
        mapTo(list){
            it.convertToImageResponse()
        }
        return list
    }

    fun ImageResponse.convertToImageEntity(): ImageEntity = ImageEntity(
        imageId = id,
        urlFull = urls.full,
        urlRaw = urls.raw,
        urlRegular = urls.regular,
        urlSmall =  urls.small,
        urlSmallS3 = urls.small_s3,
        urlThumb =  urls.thumb,
        isTicked = isTicked,
    )

    fun List<ImageResponse>.convertToListImageEntity(): List<ImageEntity> {
        val list = arrayListOf<ImageEntity>()
        mapTo(list){
            it.convertToImageEntity()
        }
        return list
    }

}