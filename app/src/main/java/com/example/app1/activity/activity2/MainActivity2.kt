package com.example.app1.activity.activity2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.R
import com.example.app1.activity.ViewModelFactory
import com.example.app1.database.AppDatabase
import com.example.app1.database.MainRepository

class MainActivity2 : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory(MainRepository(AppDatabase.getInstance(this)))
        )[Activity2ViewModel::class.java]
    }

    private val mAdapter by lazy {
        SecondAdapter(
            data = arrayListOf(),
            listener = viewModel::updateSelect
        )
    }

    private lateinit var rvImageSelected: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initRecyclerView()

        observerData()
    }

    private fun initRecyclerView() {
        rvImageSelected = findViewById(R.id.rvImageSelected)
        rvImageSelected.adapter = mAdapter
    }

    private fun observerData() {
        viewModel.imageViewItems.observe(this) {
            if (it != null && it.isNotEmpty()) {
                mAdapter.addAll(it)
            } else
                mAdapter.addAll(arrayListOf())
        }
    }
}