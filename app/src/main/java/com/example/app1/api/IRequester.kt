package com.example.app1.api

import com.example.app1.api.model.ImageResponse

interface IRequester {
    fun loadImages(page: Int, perPage: Int) : ImageResponse

    fun loadMore(page: Int, perPage: Int = 10) : ImageResponse
}