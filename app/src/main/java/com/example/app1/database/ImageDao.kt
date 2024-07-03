package com.example.app1.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {

    @Query("SELECT * FROM tbl_image")
    fun getAll(): LiveData<List<ImageEntity>>

    @Query("SELECT * FROM tbl_image")
    fun getAllImage(): List<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imageEntity: ImageEntity)

    @Query("DELETE FROM tbl_image WHERE imageId = :id")
    fun delete(id: String)

}