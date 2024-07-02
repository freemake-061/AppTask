package com.example.apptask.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StockListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StockUiState())
    val uiState: StateFlow<StockUiState> = _uiState.asStateFlow()

    private var stockList: MutableList<StockUiState> = mutableListOf()
    private lateinit var newStock: StockUiState

    fun addStock(quantity: Int, comment: String) {
        newStock = StockUiState(
            isChecked = false,
            time = "00:00:00",
            quantity = quantity,
            comment = comment
        )
        stockList.add(newStock)
    }

}