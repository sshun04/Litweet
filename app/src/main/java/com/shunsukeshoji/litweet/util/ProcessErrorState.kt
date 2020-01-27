package com.shunsukeshoji.litweet.util

enum class ProcessErrorState(val message: String) {
    BAD_CONNECTION("通信エラーが発生しました。\n通信状況を確認してやり直してください"),
    NOT_INITIALIZED("エラーが発生しました。リトライするかアプリを再起動してください"),
    UNKNOWN("エラーが発生しました。アプリを再起動してください")
}