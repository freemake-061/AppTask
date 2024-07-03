package com.example.apptask.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StockListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StockListUiState())
    val uiState: StateFlow<StockListUiState> = _uiState.asStateFlow()

    private lateinit var newStock: StockUiState
    private  var newStockList: MutableList<StockUiState> = mutableListOf()

    fun addStock(quantity: Int, comment: String) {
        newStock = StockUiState(
            isChecked = false,
            time = "00:00:00",
            quantity = quantity,
            comment = comment
        )
        newStockList += newStockList
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }
    }

}