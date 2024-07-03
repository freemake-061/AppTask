package com.example.apptask.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.apptask.Constants

class StockListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StockListUiState())
    val uiState: StateFlow<StockListUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun addStock(quantity: Int, comment: String) {
        val formatTime = DateTimeFormatter.ofPattern(Constants.CLOCK_FORMAT)
        val currentTime = formatTime.format(LocalDateTime.now())
        val newStock = StockUiState().copy(
            time = currentTime,
            quantity = quantity,
            comment = comment
        )
        val newStockList = _uiState.value.stockList + newStock
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }
    }

}