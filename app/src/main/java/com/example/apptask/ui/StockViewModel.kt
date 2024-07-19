package com.example.apptask.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.apptask.Constants

class StockViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StockListUiState())
    val uiState: StateFlow<StockListUiState> = _uiState

    fun showForm() {
        _uiState.update { currentState ->
            currentState.copy(canShowForm = true)
        }
    }

    fun closeForm() {
        _uiState.update { currentState ->
            currentState.copy(canShowForm = false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addStock(quantity: Int, comment: String) {
        val formatTime = DateTimeFormatter.ofPattern(Constants.CLOCK_FORMAT)
        val currentTime = formatTime.format(LocalDateTime.now())
        val newStockRow = StockRowUiState(
            isChecked = false,
            stock = Stock(
                uri = null,
                time = currentTime,
                quantity = quantity,
                comment = comment
            )
        )
        val newStockList = _uiState.value.stockList + newStockRow
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }
    }

    fun deleteStock(index: Int) {
        val newStockList = _uiState.value.stockList.minus(_uiState.value.stockList[index])
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }
    }

    fun onCheckedChange(index: Int) {
        val newStock = _uiState.value.stockList[index].copy(
            isChecked = !_uiState.value.stockList[index].isChecked
        )
        val newStockList = _uiState.value.stockList.toMutableList()
        newStockList[index] = newStock
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }
    }

    fun clearStock() {
        _uiState.update { currentState ->
            currentState.copy(stockList = listOf())
        }
    }

    fun showSum() {
        _uiState.update { currentState ->
            currentState.copy(canShowSum = true)
        }
    }

    fun closeSum() {
        _uiState.update { currentState ->
            currentState.copy(canShowSum = false)
        }
    }

    fun sumQuantity(): Int {
        val isCheckedStock = _uiState.value.stockList.filter { it.isChecked }
        return isCheckedStock.sumOf { it.stock.quantity }
    }

    fun updateImageUri(index: Int, uri: Uri?) {
        val newStock = _uiState.value.stockList[index].stock.copy(uri = uri)
        val newStockRow = _uiState.value.stockList[index].copy(stock = newStock)
        val newStockList = _uiState.value.stockList.toMutableList()
        newStockList[index] = newStockRow
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }
    }

}