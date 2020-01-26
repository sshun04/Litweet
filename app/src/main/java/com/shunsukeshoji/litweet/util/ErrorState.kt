package com.shunsukeshoji.litweet.util

enum class ErrorState(val message: String) {
    ACCOUNT_DOES_NOT_EXIST("このアカウントは存在しません"),
    ACCOUNT_ALREADY_SUBMITTED("このアカウントは追加済みです"),
    NOT_INITIALIZED("アプリの初期化に失敗しています。\nリトライするかアプリを再起動してください。"),
}