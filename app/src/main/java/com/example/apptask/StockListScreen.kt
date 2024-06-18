package com.example.apptask

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    stockRowList: List<StockRowData>,
    canShowDialog: Boolean,
    onDismissRequest: () -> Unit,
    onClickClose: () -> Unit,
    onClickAdd: (Stock) -> Unit,
    onClickClear: () -> Unit,
    onClickFAB: () -> Unit,
    onCheckedChange: (Int, Boolean) -> Unit,
    onClickStock: (Stock) -> Unit,
    onClickDelete: (Int) -> Unit
    ) {
    if (canShowDialog) {
        FormDialog(
            onDismissRequest = onDismissRequest,
            onClickClose = onClickClose,
            onClickAdd = onClickAdd
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(text = stringResource(R.string.home_topbar_title))
                },
                actions = {
                    Menu(
                        onClickClear = onClickClear,
                        stockRowList = stockRowList
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickFAB) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.home_button_desc_add)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StockList(
                stockRowList = stockRowList,
                onCheckedChange = onCheckedChange,
                onClickStock = onClickStock,
                onClickDelete = onClickDelete
            )
        }
    }
}

@Composable
private fun Menu(onClickClear: () -> Unit, stockRowList: List<StockRowData>) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var canShowDialog by rememberSaveable { mutableStateOf(false) }
    if (canShowDialog) {
        SumDialog(
            stockRowList = stockRowList,
            onDismissRequest = { canShowDialog = false }
        )
    }
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = stringResource(R.string.home_button_desc_menu)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_button_clear)) },
                onClick = {
                    expanded = false
                    onClickClear()
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_button_sum)) },
                onClick = {
                    expanded = false
                    canShowDialog = true
                }
            )
        }
    }
}

@Composable
private fun SumDialog(
    stockRowList: List<StockRowData>,
    onDismissRequest: () -> Unit
) {
    val isCheckedStocks = stockRowList.filter { it.isChecked }
    val sum = isCheckedStocks.sumOf { it.stock.quantity }
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        text = { Text(stringResource(R.string.sum_label_message, sum)) },
        confirmButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(stringResource(R.string.sum_button_ok))
            }
        }
    )
}