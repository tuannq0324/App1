package com.example.app1.activity.activity1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.app1.R
import com.example.app1.model.ImageViewItem
import com.example.app1.utils.Constants.VIEW_TYPE_ITEM
import com.example.app1.utils.Constants.VIEW_TYPE_LOAD_MORE
import com.example.app1.utils.Constants.VIEW_TYPE_LOAD_MORE_FAILED

class ImageAdapter(
    private val data: ArrayList<ImageViewItem?>,
    private val listener: (ImageViewItem) -> Unit,
    private val tryAgain: () -> Unit,
) : RecyclerView.Adapter<ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val itemImage =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_image, parent, false)
        val itemLoadMore =
            LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false)
        val itemLoadMoreFailed = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_load_more_failed, parent, false)

        return when (viewType) {
            VIEW_TYPE_LOAD_MORE -> ImageViewHolder(itemLoadMore, listener, tryAgain)
            VIEW_TYPE_LOAD_MORE_FAILED -> ImageViewHolder(itemLoadMoreFailed, listener, tryAgain)
            else -> ImageViewHolder(itemImage, listener, tryAgain)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is ImageViewItem.LoadMore -> VIEW_TYPE_LOAD_MORE
            is ImageViewItem.LoadMoreFailed -> VIEW_TYPE_LOAD_MORE_FAILED
            else -> VIEW_TYPE_ITEM + position
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(newList: List<ImageViewItem>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan =
            getItemViewType(position) == VIEW_TYPE_LOAD_MORE || getItemViewType(position) == VIEW_TYPE_LOAD_MORE_FAILED
        data[position]?.let { holder.bind(it) }
    }

//    class ImageViewHolder(
//        private var binding: ViewBinding,
//        private val listener: (ImageViewItem) -> Unit,
//        private val tryAgain: () -> Unit
//    ) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(imageResponse: ImageViewItem) {
//            val spannableString =
//                SpannableString(binding.root.context.getString(R.string.load_failed_try_again))
//            spannableString.setSpan(
//                ForegroundColorSpan(Color.BLUE),
//                spannableString.indexOf("try"),
//                spannableString.length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//            when (binding) {
////                is ItemLoadFailedBinding -> {
////                    (binding as ItemLoadFailedBinding).apply {
////                        tvTryAgain.text = spannableString
////                        tvTryAgain.setOnClickListener {
////                            tryAgain.invoke()
////                        }
////                    }
////                }
//
//                is ItemLoadMoreFailedBinding -> {
//                    (binding as ItemLoadMoreFailedBinding).apply {
//                        tvTryAgain.text = spannableString
//                        tvTryAgain.setOnClickListener {
//                            tryAgain.invoke()
//                        }
//                    }
//                }
//
//                is ItemLoadMoreBinding -> {
//                    (binding as ItemLoadMoreBinding).progressBar.isIndeterminate = true
//                }
//
//                is ItemRvImageBinding -> {
//                    (binding as ItemRvImageBinding).apply {
//
//                        val image = imageResponse as ImageViewItem.Image
//
//                        ivTick.isSelected = image.item.isSelected == true
//
//                        ivItem.load(imageResponse.item.item.qualityUrls?.thumb) {
//                            placeholder(R.drawable.ic_image_default)
//                            error(R.drawable.ic_load_failed)
//                            crossfade(true)
//                            placeholderMemoryCacheKey(imageResponse.item.item.id)
//                            precision(Precision.EXACT)
//                            scale(Scale.FILL)
//                        }
//
//                        root.setOnClickListener {
//                            ivTick.isSelected = !ivTick.isSelected
//                            listener(imageResponse)
//                        }
//                    }
//                }
//            }
//        }
//    }
}