package com.example.apptask.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.apptask.Constants
import com.example.apptask.data.InventoryApplication
import com.example.apptask.data.Stock
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StockListUiState())
    val uiState: StateFlow<StockListUiState> = _uiState

    init {
        viewModelScope.launch {
            val dao = InventoryApplication.database.stockDao()
            dao.getAllStocks().collect {
                _uiState.update { currentState ->
                    val stockList = List(it.size) { index ->
                        StockRow(isChecked = false, it[index])
                    }
                    currentState.copy(stockList = stockList)
                }
            }
        }
    }

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
        val newStock = Stock(
            id = 0,
            quantity = quantity,
            comment = comment,
            uri = null,
            deleteFlag = false,
            createdDateTime = LocalDateTime.now(),
            updatedDateTime = LocalDateTime.now()
        )

        val newStockList = _uiState.value.stockList + StockRow(isChecked = false, stock = newStock)

        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }

        viewModelScope.launch {
            val dao = InventoryApplication.database.stockDao()
            dao.insert(newStock)
            //  Daoでflowを使っていると意図しない呼ばれ方をするため、takeやfirstを使用するorそもそもflowを使用しない
            dao.getAllStocks().take(1).collect {
                println(it)
            }
        }
    }

    //  リスト
    fun deleteStock(index: Int) {
        val targetStockRow = _uiState.value.stockList[index]
        val newStockList = _uiState.value.stockList.minus(targetStockRow)
        _uiState.update { currentState ->
            currentState.copy(stockList = newStockList)
        }

        viewModelScope.launch {
            val dao = InventoryApplication.database.stockDao()
            dao.update(targetStockRow.stock.copy(deleteFlag = true))
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