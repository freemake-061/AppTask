package com.example.apptask.ui

import android.os.Build
import android.widget.TextClock
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apptask.Constants
import com.example.apptask.R
import com.example.apptask.Route
import com.example.apptask.StockList

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    stockListViewModel: StockListViewModel = viewModel(),
    onNavigateToScreen: (Route) -> Unit
) {
    val stockListUiState by stockListViewModel.uiState.collectAsState()

    if (stockListUiState.canShowForm) {
        FormDialog(
            onDismissRequest = { stockListViewModel.closeForm() },
            onClickAdd = { quantity, comment ->
                stockListViewModel.closeForm()
                stockListViewModel.addStock(quantity, comment)
            }
        )
    }
    if (stockListUiState.canShowSum) {
        SumDialog(
            sum = stockListViewModel.sumQuantity(),
            onDismissRequest = { stockListViewModel.closeSum() }
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
                        onClickClear = { stockListViewModel.clearStock() },
                        onClickSum = {
                            stockListViewModel.showSum()
                            stockListViewModel.sumQuantity()
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { stockListViewModel.showForm() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.home_button_add_desc)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StockList(
                stockListUiState = stockListUiState,
                onCheckedChange = { index ->
                    stockListViewModel.onCheckedChange(index)
                },
                onClickStock = { /*TODO*/ },
                onClickDelete = { index ->
                    stockListViewModel.deleteStock(index)
                }
            )
        }
    }
}

@Composable
private fun Menu(
    onClickClear: () -> Unit,
    onClickSum: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = stringResource(R.string.home_button_menu_desc)
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
                    onClickSum()
                }
            )
        }
    }
}

@Composable
private fun SumDialog(
    sum: Int,
    onDismissRequest: () -> Unit
) {
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormDialog(
    onDismissRequest: () -> Unit,
    onClickAdd: (Int, String) -> Unit
) {
    var quantity by rememberSaveable { mutableIntStateOf(Constants.STOCK_QUANTITY_MIN) }
    var comment by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.form_label_title),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.form_button_close_desc),
                        tint = colorResource(android.R.color.darker_gray),
                        modifier = Modifier.clickable { onDismissRequest() }
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.form_label_quantity) + "%,d".format(quantity))
                    Spacer(modifier = Modifier.weight(1f))
                    ElevatedButton(
                        onClick = { quantity++ },
                        enabled = when(quantity) {
                            Constants.STOCK_QUANTITY_MAX -> false
                            else -> true
                        }
                    ) {
                        Text(text = stringResource(R.string.form_button_plus))
                    }
                    ElevatedButton(
                        onClick = { quantity-- },
                        enabled = when(quantity) {
                            Constants.STOCK_QUANTITY_MIN -> false
                            else -> true
                        }
                    ) {
                        Text(text = stringResource(R.string.form_button_minus))
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isDarkTheme = isSystemInDarkTheme()
                    AndroidView(
                        factory = { context ->
                            TextClock(context).apply {
                                format12Hour?.let { this.format12Hour = Constants.CLOCK_FORMAT }
                                format24Hour?.let { this.format24Hour = Constants.CLOCK_FORMAT }
                                timeZone?.let { this.timeZone = null }
                                if (isDarkTheme) {
                                    setTextColor(context.getColor(R.color.white))
                                }
                            }
                        }
                    )
                    BasicTextField(
                        modifier = Modifier.weight(1f),
                        value = comment,
                        onValueChange = { comment = it },
                        singleLine = true,
                        decorationBox = @Composable { innerTextField ->
                            TextFieldDefaults.DecorationBox(
                                value = comment,
                                innerTextField = innerTextField,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = remember { MutableInteractionSource() },
                                contentPadding = TextFieldDefaults.contentPaddingWithLabel(
                                    start = 0.dp,
                                    top = 0.dp,
                                    end = 0.dp,
                                    bottom = 0.dp
                                ),
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.form_placeholder),
                                        style = TextStyle(color = Color.Gray)
                                    )
                                }
                            )
                        }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onClickAdd(quantity, comment)
                        }
                    ) {
                        Text(text = stringResource(R.string.form_button_add))
                    }
                }
            }
        }
    }
}