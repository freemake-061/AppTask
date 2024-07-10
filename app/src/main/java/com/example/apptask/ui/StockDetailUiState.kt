package com.example.apptask.ui

import android.net.Uri

data class StockDetailUiState (
    val uri: Uri? = null,
    val time: String = "",
    val quantity: Int = 0,
    val comment: String = ""
)