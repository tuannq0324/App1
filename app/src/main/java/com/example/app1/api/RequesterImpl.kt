package com.example.app1.api

import android.util.Log
import com.example.app1.api.model.ImageItem
import com.example.app1.api.model.ImageResponse
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RequesterImpl : IRequester {

    override fun loadImages(page: Int, perPage: Int): ImageResponse {
        return callApi(page = page, perPage = perPage)
    }

    override fun loadMore(page: Int, perPage: Int): ImageResponse {
        return callApi(page = page, perPage = perPage)
    }

    private fun callApi(page: Int, perPage: Int): ImageResponse {
        val url = "${BASE_URL}${ACCESS_KEY}&page=$page&per_page=${perPage}"

        return try {
            val jsonString = getJsonStringFromURL(url)

            if (jsonString != null) {
                val jsonArray = JSONArray(jsonString)

                val items = jsonArray.parseData()

                ImageResponse.Success(items)
            } else {
                ImageResponse.Failed
            }

        } catch (e: Throwable) {
            ImageResponse.Failed
        }
    }

    private fun JSONArray.parseData(): List<ImageItem> {
        val imageItems = mutableListOf<ImageItem>()

        for (i in 0 until this.length()) {
            val item = this.getJSONObject(i)

            val urls = item.getJSONObject("urls")

            val id = item.optString("id") ?: break

            val imageItem = ImageItem(
                id = id,
                urls = listOf(
                    urls.optString("full") ?: "",
                    urls.optString("raw") ?: "",
                    urls.optString("regular") ?: "",
                    urls.optString("small") ?: "",
                    urls.optString("small_s3") ?: "",
                    urls.optString("thumb") ?: "",
                )
            )

            imageItems.add(imageItem)
        }

        return imageItems
    }

    @Throws(IOException::class, JSONException::class)
    private fun getJsonStringFromURL(urlString: String?): String? {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            Log.d("TAG", connection.responseCode.toString())

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                response.toString()

            } else {
                null
            }

        } catch (e: Throwable) {
            e.printStackTrace()
            null
        } finally {
            connection.disconnect()
        }
    }
}