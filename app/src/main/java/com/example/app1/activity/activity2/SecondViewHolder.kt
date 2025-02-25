package com.example.app1.activity.activity2

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Precision
import coil.size.Scale
import com.example.app1.R
import com.example.app1.model.ImageViewItem

class SecondViewHolder(
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