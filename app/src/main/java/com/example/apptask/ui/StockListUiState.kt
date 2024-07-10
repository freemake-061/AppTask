package com.example.apptask.ui

data class StockUiState(
    var isChecked: Boolean = false,
    val time: String = "00:00:00",
    val quantity: Int = 0,
    val comment: String = ""
)

data class StockListUiState(
    val stockList: List<StockUiState> = listOf(),
    val canShowForm: Boolean = false,
    val canShowSum: Boolean = false
)