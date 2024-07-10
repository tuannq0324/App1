package com.example.app1.database

import androidx.lifecycle.LiveData
import com.example.app1.database.model.ImageEntity

class MainRepository(database: AppDatabase) {

    private val imageDao = database.imageDao()

    fun getAll(): LiveData<List<ImageEntity>> {
        return imageDao.getAll()
    }

    private fun getAllImage(): List<ImageEntity> {
        return imageDao.getAllImage()
    }

    fun insert(imageEntity: ImageEntity) {
        imageDao.insert(imageEntity)
    }

    fun delete(id: String) {
        imageDao.delete(id)
    }

    fun isExist(id: String) : Boolean {
        return getAllImage().find {
            it.imageId == id
        } != null

    }
}