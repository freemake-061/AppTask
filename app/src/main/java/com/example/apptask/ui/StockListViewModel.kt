package com.example.apptask.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StockListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StockListUiState())
    val uiState: StateFlow<StockListUiState> = _uiState.asStateFlow()

    fun addStock(quantity: Int, comment: String) {
        val newStock = StockUiState(
            isChecked = false,
            time = "00:00:00",
            quantity = quantity,
            comment = comment
        )
        val newStockList = _uiState.value.stockList + newStock
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }
    }

}