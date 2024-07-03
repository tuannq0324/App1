package com.example.app1.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.R
import com.example.app1.adapter.ImageAdapter
import com.example.app1.database.AppDatabase
import com.example.app1.database.MainRepository
import com.example.app1.utils.CommonFunction.convertToListImageResponse
import com.example.app1.viewmodel.Activity2ViewModel
import com.example.app1.viewmodel.ViewModelFactory

//@AndroidEntryPoint
class MainActivity2 : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory(MainRepository(AppDatabase.getInstance(this)))
        )[Activity2ViewModel::class.java]
    }
    private val mAdapter by lazy {
        ImageAdapter(arrayListOf()) {
            viewModel.deleteTick(it.id)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val rvImage = findViewById<RecyclerView>(R.id.rvImage)
        rvImage.adapter = mAdapter

        viewModel.getAll().observe(this) {
            if (it != null && it.isNotEmpty()) {
                mAdapter.addAll(it.convertToListImageResponse())
            } else mAdapter.addAll(arrayListOf())
        }
    }
}