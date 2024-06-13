package com.example.apptask

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apptask.ui.theme.AppTaskTheme

sealed class Route {
    abstract val value: String

    class StockListScreen() : Route() {
        override val value: String = "StockList"
    }

    class StockDetailScreen(stock: Stock) : Route() {
        override val value: String = "StockDetail/${stock.clock}/${stock.quantity}/${stock.comment}"
    }
}

class MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTask()
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
@Composable
private fun Preview() {
    AppTask()
}

data class Stock(val clock: String, val quantity: Int, val comment: String)
data class StockRowData(var isChecked: Boolean, val stock: Stock)

var initialStocks = listOf(
    StockRowData(false, Stock("00:00:00", 0,    "コメント")),
    StockRowData(false, Stock("00:00:00", 1,    "コメント")),
    StockRowData(false, Stock("00:00:00", 1000, "コメント")),
    StockRowData(false, Stock("00:00:00", 9999, "コメントコメントコメントコメントコメントコメントコメントコメントコメント"))
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AppTask() {
    AppTaskTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "StockList") {
                val onNavigateToScreen: (Route) -> Unit = { route ->
                    navController.navigate(route.value)
                }
                composable(
                    route = "StockList",
                    enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth}) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth}) }
                ) {
                    StockListScreen(onNavigateToScreen = onNavigateToScreen)
                }
                composable(
                    route = "StockDetail/{clock}/{quantity}/{comment}",
                    arguments = listOf(
                        navArgument("clock") { type = NavType.StringType },
                        navArgument("quantity") { type = NavType.IntType },
                        navArgument("comment") { type = NavType.StringType }
                    ),
                    enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth}) }
                ) { backStackEntry ->
                    val clock = backStackEntry.arguments?.getString("clock")
                    val quantity = backStackEntry.arguments?.getInt("quantity")
                    val comment = backStackEntry.arguments?.getString("comment")
                    if (clock != null && quantity != null && comment != null) {
                        StockDetailScreen(stock = Stock(clock, quantity, comment))
                    }
                }
            }
        }
    }
}