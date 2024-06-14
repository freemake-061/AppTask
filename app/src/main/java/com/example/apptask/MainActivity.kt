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

    class StockDetailScreen(index: Int) : Route() {
        override val value: String = "StockDetail/$index"
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

data class Stock(val clock: String, val quantity: Int, val comment: String)
data class StockRowData(var isChecked: Boolean, val stock: Stock)

var initialStocks = listOf(
    StockRowData(false, Stock("00:00:00", 0,    "コメント")),
    StockRowData(false, Stock("00:00:00", 1,    "コメント")),
    StockRowData(false, Stock("00:00:00", 1000, "コメント")),
    StockRowData(false, Stock("00:00:00", 9999, "コメントコメントコメントコメントコメントコメントコメントコメントコメント"))
)

@RequiresApi(Build.VERSION_CODES.P)
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
                    route = "StockDetail/{index}",
                    arguments = listOf(
                        navArgument("index") { type = NavType.IntType }
                    ),
                    enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth}) }
                ) { backStackEntry ->
                    val index = backStackEntry.arguments?.getInt("index")
                    if (index != null) {
                        StockDetailScreen(
                            onNavigateToScreen = onNavigateToScreen,
                            index = index
                        )
                    }
                }
            }
        }
    }
}