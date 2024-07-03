package com.example.app1.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app1.database.MainRepository
import com.example.app1.model.ImageResponse
import com.example.app1.model.Urls
import com.example.app1.utils.CommonFunction.convertToImageEntity
import com.example.app1.utils.CommonFunction.convertToListImageResponse
import com.example.app1.utils.Constants.ACCESS_KEY
import com.example.app1.utils.Constants.BASE_URL
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//@HiltViewModel
class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    val listImage: MutableLiveData<List<ImageResponse>> = MutableLiveData()
    var page = 1

    fun fetchData() {
        object : Thread(Runnable {
            val url = "$BASE_URL$ACCESS_KEY&page=$page&per_page=10"
            val jsonString = getJsonStringFromURL(url)

            val listImageResponse = arrayListOf<ImageResponse>()
            val jsonArray = jsonString?.let { JSONArray(it) }

            if (jsonArray != null) {
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val urls = item.getJSONObject("urls")
                    val imageResponse = ImageResponse(
                        item.getString("id"), Urls(
                            urls.getString("full"),
                            urls.getString("raw"),
                            urls.getString("regular"),
                            urls.getString("small"),
                            urls.getString("small_s3"),
                            urls.getString("thumb"),
                        )
                    )
                    val index =
                        listImage.value?.toMutableList()?.indexOfFirst { it.id == imageResponse.id }
                            ?: -1
                    if (index != -1) {
                        Log.d("Existed", "Existed")
                    } else {
                        listImageResponse.add(imageResponse)
                    }
                }
                val currentList = listImage.value?.toMutableList() ?: mutableListOf()
                currentList.addAll(listImageResponse)
                getAllImage().convertToListImageResponse().forEach { item ->
                    val index = currentList.indexOfFirst { it.id == item.id }
                    if (index != -1) currentList[index].isTicked = true
                }
                Handler(Looper.getMainLooper()).post {
                    listImage.value = currentList
                }
            } else Log.d("TAG", "null")
        }) {}.start()
    }

    fun loadMore() {
        page++
        fetchData()
    }

    @Throws(IOException::class, JSONException::class)
    fun getJsonStringFromURL(urlString: String?): String? {
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

    fun getAll() = repository.getAll()

    fun getAllImage() = repository.getAllImage()

    fun insertTick(imageResponse: ImageResponse) {
        val thread = object : Thread(Runnable {
            repository.insert(imageResponse.convertToImageEntity())
        }) {}
        thread.start()
    }

    fun deleteTick(id: String) {
        val thread = object : Thread(Runnable {
            repository.delete(id)
        }) {}
        thread.start()
    }

    fun updateList() {
        object : Thread(Runnable {
            try {
                val currentList = listImage.value?.toMutableList() ?: mutableListOf()
                currentList.forEach { it.isTicked = false }
                getAllImage().convertToListImageResponse().forEach { item ->
                    val index = currentList.indexOfFirst { it.id == item.id }
                    if (index != -1) currentList[index].isTicked = true
                }
                Handler(Looper.getMainLooper()).post {
                    listImage.value = (currentList)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }) {}.start()
    }

    fun clearTick() {
        object : Thread(Runnable {
            val currentList = listImage.value?.toMutableList() ?: mutableListOf()
            currentList.forEach {
                it.isTicked = false
            }
            Handler(Looper.getMainLooper()).post {
                listImage.value = (currentList)
            }
        }) {}.start()
    }

}