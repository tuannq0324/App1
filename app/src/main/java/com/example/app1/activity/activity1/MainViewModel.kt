package com.example.app1.activity.activity1

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app1.BaseApplication
import com.example.app1.api.model.ImageItem
import com.example.app1.api.model.ImageResponse
import com.example.app1.database.MainRepository
import com.example.app1.model.DataItem
import com.example.app1.model.ImageViewItem
import com.example.app1.utils.CommonFunction.convertToImageEntity
import com.example.app1.utils.combineWiths

enum class ViewState {
    Loading, Success, Failed, Empty,
}

enum class LoadMoreState {
    Idle, Loading, Success, Failed,
}

class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    private val imageEntities = repository.getAll()

    private val imageItems: MutableLiveData<List<ImageItem>> = MutableLiveData()

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val loadMoreState = MutableLiveData(LoadMoreState.Idle)

    val imageViewItems: LiveData<List<ImageViewItem>> = imageItems.combineWiths(
        imageEntities, loadMoreState
    ) { imageItems, entities, loadMoreState ->
        val result = mutableListOf<ImageViewItem>()

        val selected = entities?.map { it.imageId } ?: emptyList()

        val map = imageItems?.map {
            ImageViewItem.Image(DataItem(item = it, isSelected = selected.contains(it.id)))
        } ?: emptyList()

        result.addAll(map)

        when (loadMoreState) {
            LoadMoreState.Loading -> result.add(ImageViewItem.LoadMore)
            LoadMoreState.Failed -> result.add(ImageViewItem.LoadMoreFailed)
            else -> {}
        }

        result
    }

    private var page = 1

    init {
        fetchData()
    }

    fun fetchData() {
        Thread {
            updateViewState(ViewState.Loading)

            val currentList = imageItems.value?.toMutableList() ?: mutableListOf()

            when (val data = BaseApplication.requester.loadImages(page, 10)) {
                is ImageResponse.Success -> {
                    Thread.sleep(3000)

                    page++

                    updateViewState(ViewState.Success)

                    currentList.addAll(data.items)

                    updateImageItems(items = currentList)
                }

                is ImageResponse.Failed -> {
                    updateViewState(ViewState.Failed)
                }
            }

        }.start()
    }

    fun loadMore() {
        if (loadMoreState.value == LoadMoreState.Loading) return

        updateLoadMoreState(LoadMoreState.Loading)

        Thread {
            Thread.sleep(3000)

            val currentList = imageItems.value?.toMutableList() ?: mutableListOf()

            when (val data = BaseApplication.requester.loadMore(page, 10)) {
                is ImageResponse.Success -> {

                    page++

                    updateLoadMoreState(LoadMoreState.Success)

                    currentList.addAll(data.items)

                    updateImageItems(items = currentList)
                }

                is ImageResponse.Failed -> {
                    updateLoadMoreState(LoadMoreState.Failed)
                }
            }
        }.start()
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

    private fun updateImageItems(items: List<ImageItem>) {
        Handler(Looper.getMainLooper()).post {
            imageItems.value = items.distinctBy {
                it.id
            }
        }
    }

    private fun updateViewState(state: ViewState) {
        Handler(Looper.getMainLooper()).post {
            _viewState.value = state
        }
    }

    private fun updateLoadMoreState(state: LoadMoreState) {
        Handler(Looper.getMainLooper()).post {
            loadMoreState.value = state
        }
    }
}