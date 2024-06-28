package com.example.apptask.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FormViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    fun showForm() {
        _uiState.value = FormUiState()
    }

    fun incrementQuantity() {
        _uiState.update { currentState ->
            currentState.copy(quantity = _uiState.value.quantity.inc())
        }
    }

    fun decrementQuantity() {
        _uiState.update { currentState ->
            currentState.copy(quantity = _uiState.value.quantity.dec())
        }
    }

    fun onCommentChange(newComment: String) {
        _uiState.update { currentState ->
            currentState.copy(comment = newComment)
        }
    }

}