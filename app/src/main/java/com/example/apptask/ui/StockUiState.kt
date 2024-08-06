package com.example.apptask.ui

import com.example.apptask.data.Stock

data class StockRow(
    val isChecked: Boolean,
    val stock: Stock
)

data class StockListUiState(
    val stockList: List<StockRow> = listOf(),
    val canShowForm: Boolean = false,
    val canShowSum: Boolean = false
)