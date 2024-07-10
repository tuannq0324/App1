package com.example.app1.utils

import com.example.app1.api.model.ImageItem
import com.example.app1.database.model.ImageEntity

object CommonFunction {

    fun ImageEntity.convertToImageResponse(): ImageItem = ImageItem(
        id = imageId, urls = qualityUrls
    )

    fun ImageItem.convertToImageEntity(): ImageEntity = ImageEntity(
        imageId = id,
        qualityUrls = urls
    )

}