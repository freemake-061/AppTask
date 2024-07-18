package com.example.apptask.ui

import com.example.apptask.Stock

data class StockRowUiState(
    val isChecked: Boolean,
    val stock: Stock
)

data class StockListUiState(
    val stockList: List<StockRowUiState> = listOf(),
    val canShowForm: Boolean = false,
    val canShowSum: Boolean = false
)