package com.example.app1.activity.activity1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.app1.R
import com.example.app1.activity.ViewModelFactory
import com.example.app1.activity.activity2.MainActivity2
import com.example.app1.database.AppDatabase
import com.example.app1.database.MainRepository

//@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory(MainRepository(AppDatabase.getInstance(this)))
        )[MainViewModel::class.java]
    }

    private val mAdapter by lazy {
        ImageAdapter(
            data = arrayListOf(), listener = viewModel::updateSelect, tryAgain = viewModel::loadMore
        )
    }

    private lateinit var rvImage: RecyclerView
    private lateinit var button: Button
    private lateinit var linearLayoutLoading: LinearLayout
    private lateinit var constraintLayoutError: ConstraintLayout
    private lateinit var tvLoadFailed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        setOnClick()

        observerData()
    }

    private fun observerData() {
        viewModel.viewState.observe(this) {
            when (it) {
                ViewState.Loading -> {
                    constraintLayoutError.visibility = View.GONE
                    linearLayoutLoading.visibility = View.VISIBLE
                }

                ViewState.Success -> {
                    linearLayoutLoading.visibility = View.GONE
                    constraintLayoutError.visibility = View.GONE
                }

                ViewState.Failed, ViewState.Empty -> {
                    constraintLayoutError.visibility = View.VISIBLE
                    Toast.makeText(this, "Load failed", Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
        viewModel.imageViewItems.observe(this) {
            if (it != null && it.isNotEmpty()) {
                mAdapter.addAll(it)
            }
        }
    }

    private fun initView() {
        rvImage = findViewById(R.id.rvImage)

        button = findViewById(R.id.btnActivity2)

        linearLayoutLoading = findViewById(R.id.linearLayoutLoading)

        constraintLayoutError = findViewById(R.id.constraintLayoutError)

        tvLoadFailed = findViewById(R.id.tvLoadFailed)

        rvImage.adapter = mAdapter

        rvImage.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("loadMore", "onScrollStateChanged: ")
                    viewModel.loadMore()
                }
            }

//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (!recyclerView.canScrollVertically(1)) {
//                    Log.d("loadMore", "onScrolled: ")
//                    viewModel.loadMore()
//                }
//            }
        })
    }

    private fun setOnClick() {
        button.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        tvLoadFailed.setOnClickListener {
            viewModel.fetchData()
        }
    }
}