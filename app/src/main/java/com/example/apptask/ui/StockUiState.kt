package com.example.apptask.ui

data class StockUiState(
    val isChecked: Boolean = false,
    val time: String = "00:00:00",
    val quantity: Int = 0,
    val comment: String = ""
)

data class StockListUiState(
    val stockUiState: MutableList<StockUiState> = mutableListOf()
)