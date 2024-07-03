package com.example.app1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app1.database.MainRepository

//@HiltViewModel
class Activity2ViewModel(private val repository: MainRepository) : ViewModel() {

    fun getAll() = repository.getAll()

    fun deleteTick(id: String) {
        val thread = object : Thread(Runnable {
            repository.delete(id)
        }) {}
        thread.start()
    }

}