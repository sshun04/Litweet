package com.shunsukeshoji.litweet.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shunsukeshoji.litweet.R
import com.shunsukeshoji.litweet.presentation.adapter.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModel()
    lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        mainActivityViewModel.accounts.observe(this, Observer {
            recyclerViewAdapter =
                RecyclerViewAdapter(it)
            recyclerView.apply {
                adapter = recyclerViewAdapter
                layoutManager = linearLayoutManager
            }
        })

        mainActivityViewModel.tweets.observe(this, Observer {
            if (it != null) {
                recyclerViewAdapter.setTweet(it)
            }
        })

        searchFab.setOnClickListener {
            mainActivityViewModel.setUpDialog(supportFragmentManager)
        }
    }


    override fun onStart() {
        super.onStart()
        mainActivityViewModel.onStartActivity()
    }

}
