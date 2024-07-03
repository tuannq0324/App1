package com.example.app1.database

import androidx.lifecycle.LiveData

class MainRepository(database: AppDatabase) {

    private val imageDao = database.imageDao()

    fun getAll(): LiveData<List<ImageEntity>> {
        return imageDao.getAll()
    }

    fun getAllImage(): List<ImageEntity> {
        return imageDao.getAllImage()
    }

    fun insert(imageEntity: ImageEntity) {
        imageDao.insert(imageEntity)
    }

    fun delete(id: String) {
        imageDao.delete(id)
    }
}