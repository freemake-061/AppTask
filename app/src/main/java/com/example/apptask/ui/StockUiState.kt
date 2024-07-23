package com.example.apptask.ui

import android.net.Uri

data class Stock(
    val uri: Uri?,
    val time: String,
    val quantity: Int,
    val comment: String
)

data class StockRow(
    val isChecked: Boolean,
    val stock: Stock
)

data class StockListUiState(
    val stockList: List<StockRow> = listOf(),
    val canShowForm: Boolean = false,
    val canShowSum: Boolean = false
)