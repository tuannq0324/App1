package com.example.app1.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.R
import com.example.app1.adapter.ImageAdapter
import com.example.app1.database.AppDatabase
import com.example.app1.database.MainRepository
import com.example.app1.viewmodel.MainViewModel
import com.example.app1.viewmodel.ViewModelFactory

//@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory(MainRepository(AppDatabase.getInstance(this)))
        )[MainViewModel::class.java]
    }
    private val mAdapter by lazy {
        ImageAdapter(arrayListOf()) {
            if (it.isTicked == true) {
                viewModel.insertTick(it)
            } else {
                viewModel.deleteTick(it.id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rvImage = findViewById<RecyclerView>(R.id.rvImage)
        val btnActivity2 = findViewById<Button>(R.id.btnActivity2)

        viewModel.fetchData()
        viewModel.listImage.observe(this) {
            if (it != null && it.isNotEmpty()) {
                rvImage.setItemViewCacheSize(it.size)
                mAdapter.addAll(it)
            }
        }
        viewModel.getAll().observe(this) {
            if (it != null && it.isNotEmpty()) {
                viewModel.updateList()
            } else viewModel.clearTick()
        }

        rvImage.adapter = mAdapter
        btnActivity2.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        rvImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!rvImage.canScrollVertically(1)) {
                    viewModel.loadMore()
                }
            }
        })
    }
}