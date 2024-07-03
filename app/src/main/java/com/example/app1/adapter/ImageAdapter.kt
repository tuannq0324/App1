package com.example.app1.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app1.R
import com.example.app1.model.ImageResponse

class ImageAdapter(
    private val data: ArrayList<ImageResponse>,
    private val listener: (ImageResponse) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_image, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(newList: List<ImageResponse>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val ivItem = holder.itemView.findViewById<ImageView>(R.id.ivItem)
        val ivTick = holder.itemView.findViewById<ImageView>(R.id.ivTick)
        ivTick.isSelected = data[position].isTicked == true
        Glide.with(holder.itemView.context)
            .load(data[position].urls.full)
            .into(ivItem)
        holder.itemView.setOnClickListener {
            data[position].isTicked = !data[position].isTicked!!
            listener(data[position])
            notifyItemChanged(position)
        }
    }
}