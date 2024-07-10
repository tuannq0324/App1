package com.example.app1.activity.activity2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.app1.database.MainRepository
import com.example.app1.model.DataItem
import com.example.app1.model.ImageViewItem
import com.example.app1.utils.CommonFunction.convertToImageEntity
import com.example.app1.utils.CommonFunction.convertToImageResponse

//@HiltViewModel
class Activity2ViewModel(private val repository: MainRepository) : ViewModel() {

    val imageViewItems = repository.getAll().map { list ->
        list.map {
            ImageViewItem.Image(DataItem(item = it.convertToImageResponse(), isSelected = true))
        }
    }

    fun updateSelect(imageItem: ImageViewItem) {
        Thread {
            if (imageItem is ImageViewItem.Image) {
                val item = imageItem.item.item

                val isExist = repository.isExist(id = item.id)

                if (isExist) {
                    repository.delete(item.id)
                } else {
                    repository.insert(item.convertToImageEntity())
                }
            }
        }.start()
    }

}