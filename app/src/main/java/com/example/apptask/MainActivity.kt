package com.example.apptask

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.apptask.ui.theme.AppTaskTheme

class MainActivity : ComponentActivity() {
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

class Constants {
    companion object {
        const val STOCK_QUANTITY_MIN = 0
        const val STOCK_QUANTITY_MAX = 9999
        const val CLOCK_FORMAT = "hh:mm:ss"
    }
}

data class Stock(val clock: String, val quantity: Int, val comment: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home() {
    var canShowDialog by rememberSaveable { mutableStateOf(true) }
    if (canShowDialog)
        FormDialog(setShowDialog = { canShowDialog = it })
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = "Home")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { canShowDialog = true }) {
                Icon(Icons.Default.Add, "Floating action button")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StockList()
        }
    }
}