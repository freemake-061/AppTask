package com.example.apptask.ui

import android.net.Uri

data class StockA(
    val uri: Uri?,
    val time: String,
    val quantity: Int,
    val comment: String
)

data class StockRow(
    val isChecked: Boolean,
    val stockA: StockA
)

data class StockListUiState(
    val stockList: List<StockRow> = listOf(),
    val canShowForm: Boolean = false,
    val canShowSum: Boolean = false
)