package com.example.app1.model

data class ImageResponse(
    var id: String, var urls: Urls, var isTicked: Boolean? = false
)