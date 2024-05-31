package com.example.apptask

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.apptask.ui.theme.AppTaskTheme
import android.util.Log

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTaskTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Home()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    fontScale = 1f,
    name = "Light Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    fontScale = 1f,
    name = "Dark Mode"
)
@Composable
private fun Preview() {
    AppTaskTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Home()
        }
    }
}

data class Stock(val clock: String, val quantity: Int, val comment: String)
data class StockRowData(var isChecked: Boolean, val stock: Stock)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home() {
    var stockRowList by rememberSaveable { mutableStateOf(initialStocks) }
    var canShowDialog by rememberSaveable { mutableStateOf(false) }
    if (canShowDialog) {
        FormDialog(
            onDismissRequest = { canShowDialog = false },
            onClickClose = { canShowDialog = false },
            onClickAdd = { stock ->
                canShowDialog = false
                stockRowList += stock
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(R.string.home_topbar_title))
                },
                actions = {
                    Menu(
                        onClickClear = {
                            stockRowList = stockRowList.toMutableList().also {
                                it.clear()
                            }
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { canShowDialog = true }) {
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
                onCheckedChange = { index, isChecked ->
                    stockRowList = stockRowList.toMutableList().also {
                        it[index] = it[index].copy(isChecked = isChecked)
                    }
                },
                onClickDelete = { index ->
                    stockRowList = stockRowList.toMutableList().also {
                        it.removeAt(index)
                    }
                }
            )
        }
    }
}

var initialStocks = listOf(
    StockRowData(false, Stock("00:00:00", 0,    "コメント")),
    StockRowData(false, Stock("00:00:00", 1,    "コメント")),
    StockRowData(false, Stock("00:00:00", 1000, "コメント")),
    StockRowData(false, Stock("00:00:00", 9999, "コメントコメントコメントコメントコメントコメントコメントコメントコメント"))
)

@Composable
private fun Menu(onClickClear: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var canShowDialog by rememberSaveable { mutableStateOf(false) }
    if (canShowDialog) {
        SumDialog(onDismissRequest = { canShowDialog = false })
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
private fun SumDialog(onDismissRequest: () -> Unit) {
    /*
    後回し
    val checkedStocks = stocks.filter { it.isChecked }
    val sum = checkedStocks.sumOf { it.stock.quantity }
     */
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        text = { Text(stringResource(R.string.sum_label_message, 0)) },
        confirmButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(stringResource(R.string.sum_button_ok))
            }
        }
    )
}