package com.example.apptask

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
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
        // uriをString型にする
        override val value: String = "StockDetail/${"uri:"+stock.uri.toString()}/${stock.clock}/${stock.quantity}/${stock.comment}"
    }
}

class MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTask()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
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

data class Stock(var uri: Uri?, val clock: String, val quantity: Int, val comment: String)
data class StockRowData(var isChecked: Boolean, val stock: Stock)

var initialStocks = listOf(
    StockRowData(false, Stock(null, "00:00:00", 0,    "コメント")),
    StockRowData(false, Stock(null, "00:00:00", 1,    "コメント")),
    StockRowData(false, Stock(null, "00:00:00", 1000, "コメント")),
    StockRowData(false, Stock(null, "00:00:00", 9999, "コメントコメントコメントコメントコメントコメントコメントコメントコメント"))
)

@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun AppTask() {
    AppTaskTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "StockList") {
                val onNavigateToScreen: (Route) -> Unit = { route ->
                    println(route.value)
                    navController.navigate(route.value)
                }
                val onPopToScreen: (Route) -> Unit = { route ->
                    navController.popBackStack(route.value, false)
                }
                composable(
                    route = "StockList",
                    enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth}) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth}) }
                ) {
                    StockListScreen(onNavigateToScreen = onNavigateToScreen)
                }
                composable(
                    route = "StockDetail/{stringUri}/{clock}/{quantity}/{comment}",
                    arguments = listOf(
                        navArgument("stringUri") { type = NavType.StringType },
                        navArgument("clock") { type = NavType.StringType },
                        navArgument("quantity") { type = NavType.IntType },
                        navArgument("comment") { type = NavType.StringType }
                    ),
                    enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth}) }
                ) { backStackEntry ->
                    val stringUri = backStackEntry.arguments?.getString("stringUri")
                    val uri = Uri.parse(stringUri?.drop(4)) //頭のuri:を取る
                    val clock = backStackEntry.arguments?.getString("clock")
                    val quantity = backStackEntry.arguments?.getInt("quantity")
                    val comment = backStackEntry.arguments?.getString("comment")
                    if (clock != null && quantity != null && comment != null) {
                        StockDetailScreen(
                            onPopToScreen = onPopToScreen,
                            stock = Stock(uri, clock, quantity, comment)
                        )
                    }
                }
            }
        }
    }
}