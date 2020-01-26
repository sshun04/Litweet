package com.shunsukeshoji.litweet.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shunsukeshoji.litweet.R
import com.shunsukeshoji.litweet.presentation.adapter.RecyclerViewAdapter
import com.shunsukeshoji.litweet.presentation.fragment.PostDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.IllegalStateException
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModel()
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        viewModel.accounts.observe(this, Observer {
            recyclerViewAdapter =
                RecyclerViewAdapter(it)
            recyclerView.apply {
                adapter = recyclerViewAdapter
                layoutManager = linearLayoutManager
            }
        })

        viewModel.tweets.observe(this, Observer {
            recyclerViewAdapter.submitList(it?.toList())
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        viewModel.errorLiveData.observe(this, Observer { throwable ->
            val snackBar = Snackbar.make(
                rootLayout,
                "",
                Snackbar.LENGTH_LONG
            ).apply {
                setBackgroundTint(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.dark_87
                    )
                )
            }

            when (throwable) {
                is UnknownHostException -> {
                    snackBar.setText(R.string.error_bad_connection)
                }
                is IllegalStateException -> {
                    snackBar.setAction("リトライ") {
                        viewModel.init()
                    }
                    snackBar.setText(throwable.message ?: getString(R.string.error_unKnown))
                }
                else -> {
                    snackBar.setText(getString(R.string.error_unKnown))
                }
            }

            snackBar.show()
            throwable.stackTrace
        })

        searchFab.setOnClickListener {
            PostDialogFragment.show(supportFragmentManager)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.init()
    }
}