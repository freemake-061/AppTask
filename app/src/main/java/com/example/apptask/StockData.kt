package com.example.apptask

import androidx.compose.runtime.mutableStateListOf

object StockData {
    var stocks = mutableStateListOf(
        Stock("00:00:00", 0, "コメント"),
        Stock("00:00:00", 1, "コメント"),
        Stock("00:00:00", 1000, "コメント"),
        Stock("00:00:00", 9999, "コメントコメントコメントコメントコメントコメントコメントコメントコメント"),
    )
}