package com.shunsukeshoji.litweet.presentation.view

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
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
            if (it.isNotEmpty()) {
                recyclerViewAdapter.setTweet(it)
            }
        })

        searchFab.setOnClickListener {
            setUpDialog()
        }
    }

    private fun setUpDialog() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("友達を追加しよう")
            .setView(editText)
            .setPositiveButton("追加", DialogInterface.OnClickListener { _, _ ->
                mainActivityViewModel.onSubmitId(editText.text.toString()){
                    editText.error = "このIDは存在しません"
                }
            })
            .setNegativeButton("キャンセル", DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })
            .show()
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel.onStartActivity()
    }

}
