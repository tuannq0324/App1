package com.example.app1.activity.activity2

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.R
import com.example.app1.model.ImageViewItem
import com.example.app1.utils.Constants.VIEW_TYPE_ITEM

class SecondAdapter(
    private val data: ArrayList<ImageViewItem?>,
    private val listener: (ImageViewItem) -> Unit,
) : RecyclerView.Adapter<SecondViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondViewHolder {

        val itemImage =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_image, parent, false)
        return SecondViewHolder(itemImage, listener)
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

    override fun onBindViewHolder(holder: SecondViewHolder, position: Int) {
        data[position]?.let { holder.bind(it) }
    }
}