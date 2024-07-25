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

    //  入力フォーム
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
        val newStockRow = StockRow(
            isChecked = false,
            stock = Stock(
                uri = null,
                time = getCurrentTime(),
                quantity = quantity,
                comment = comment
            )
        )
        val newStockList = _uiState.value.stockList + newStockRow
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentTime(): String {
        val formatTime = DateTimeFormatter.ofPattern(Constants.CLOCK_FORMAT)
        return formatTime.format(LocalDateTime.now())
    }

    //  リスト
    fun deleteStock(index: Int) {
        val targetStockRow = _uiState.value.stockList[index]
        val newStockList = _uiState.value.stockList.minus(targetStockRow)
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }
    }

    fun onCheckedChange(index: Int) {
        val newStockRow = _uiState.value.stockList[index].copy(
            isChecked = !_uiState.value.stockList[index].isChecked
        )
        val newStockList = _uiState.value.stockList.toMutableList()
        newStockList[index] = newStockRow
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }
    }

    //  メニュー > 全て削除
    fun allClearStockList() {
        _uiState.update { currentState ->
            currentState.copy(stockList = listOf())
        }
    }

    //  メニュー > 合計
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

    //  詳細画面
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