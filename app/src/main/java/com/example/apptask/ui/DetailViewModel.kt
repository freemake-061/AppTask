package com.example.apptask.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptask.data.InventoryApplication
import com.example.apptask.data.Stock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class DetailViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(Stock())
    val uiState: StateFlow<Stock> = _uiState

    //  詳細画面
    fun updateImageUri(uri: Uri?) {
        val newStock = _uiState.value.copy(uri = uri)

        viewModelScope.launch {
            val dao = InventoryApplication.database.stockDao()
            dao.update(newStock)
        }
    }
}