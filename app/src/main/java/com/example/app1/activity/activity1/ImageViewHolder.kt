package com.example.app1.activity.activity1

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Precision
import coil.size.Scale
import com.example.app1.R
import com.example.app1.model.ImageViewItem

class ImageViewHolder(
    val item: View,
    private val listener: (ImageViewItem) -> Unit,
    private val tryAgain: () -> Unit
) : RecyclerView.ViewHolder(item) {
    fun bind(imageResponse: ImageViewItem) {

        when (imageResponse) {
            is ImageViewItem.Image -> {
                val ivItem = item.findViewById<ImageView>(R.id.ivItem)
                val ivTick = item.findViewById<ImageView>(R.id.ivTick)

                ivTick.isSelected = imageResponse.item.isSelected
                ivItem.load(imageResponse.item.item.urls.last()) {
                    placeholder(R.drawable.ic_image_default)
                    error(R.drawable.ic_load_failed)
                    crossfade(true)
                    memoryCacheKey(imageResponse.item.item.id)
                    precision(Precision.EXACT)
                    scale(Scale.FILL)
                }

                item.setOnClickListener {
                    listener(imageResponse)
                }
            }

            ImageViewItem.LoadMore -> {}

            ImageViewItem.LoadMoreFailed -> {
                val tvTryAgain = item.findViewById<TextView>(R.id.tvTryAgain)
                if (tvTryAgain != null) {
                    val spannableString =
                        SpannableString(itemView.context.getString(R.string.load_failed_try_again))
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.BLUE),
                        spannableString.indexOf("try"),
                        spannableString.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvTryAgain.text = spannableString
                    tvTryAgain.setOnClickListener {
                        tryAgain.invoke()
                    }
                }
            }
        }
    }
}