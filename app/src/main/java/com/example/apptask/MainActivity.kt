package com.example.apptask

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptask.ui.theme.AppTaskTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTaskTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "Home") {
                        composable(route = "Home") {
                            Home(
                                onNavigateToConversation = { navController.navigate("StockDetail") }
                            )
                        }
                        composable(route = "StockDetail") { StockDetail() }
                    }
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
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "Home") {
                composable(route = "Home") {
                    Home(
                        onNavigateToConversation = { navController.navigate("StockDetail") }
                    )
                }
                composable(route = "StockDetail") { StockDetail() }
            }
        }
    }
}

data class Stock(val clock: String, val quantity: Int, val comment: String)
data class StockRowData(var isChecked: Boolean, val stock: Stock)

var initialStocks = listOf(
    StockRowData(false, Stock("00:00:00", 0,    "コメント")),
    StockRowData(false, Stock("00:00:00", 1,    "コメント")),
    StockRowData(false, Stock("00:00:00", 1000, "コメント")),
    StockRowData(false, Stock("00:00:00", 9999, "コメントコメントコメントコメントコメントコメントコメントコメントコメント"))
)