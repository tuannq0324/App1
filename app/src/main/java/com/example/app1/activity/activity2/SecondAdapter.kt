package com.example.app1.activity.activity2

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Precision
import coil.size.Scale
import com.example.app1.R
import com.example.app1.model.ImageViewItem
import com.example.app1.utils.Constants.VIEW_TYPE_ITEM

class SecondAdapter(
    private val data: ArrayList<ImageViewItem?>,
    private val listener: (ImageViewItem) -> Unit,
) : RecyclerView.Adapter<SecondAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val itemImage =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_image, parent, false)
        return ImageViewHolder(itemImage, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM + position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(newList: List<ImageViewItem>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        data[position]?.let { holder.bind(it) }
    }

    class ImageViewHolder(
        itemView: View,
        private val listener: (ImageViewItem) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(imageResponse: ImageViewItem) {

            val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
            val ivTick = itemView.findViewById<ImageView>(R.id.ivTick)

            val item = (imageResponse as ImageViewItem.Image).item

            ivTick.isSelected = item.isSelected == true

            ivItem.load(imageResponse.item.item.urls.last()) {
                placeholder(R.drawable.ic_image_default)
                error(R.drawable.ic_load_failed)
                crossfade(true)
                memoryCacheKey(imageResponse.item.item.id)
                precision(Precision.EXACT)
                scale(Scale.FILL)
            }

            itemView.setOnClickListener {
                listener(imageResponse)
            }

        }
    }
}