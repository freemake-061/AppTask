package com.example.apptask

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apptask.ui.StockDetailScreen
import com.example.apptask.ui.StockListScreen
import com.example.apptask.ui.StockViewModel
import com.example.apptask.ui.theme.AppTaskTheme

sealed class Route {
    abstract val value: String

    class StockListScreen : Route() {
        override val value: String = "StockList"
    }

    class StockDetailScreen(index: Int) : Route() {
        override val value: String = "StockDetail/${index}"
    }
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            }
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

@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun AppTask() {
    AppTaskTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                route = "Stock",
                startDestination = "StockList"
            ) {
                val onNavigateToScreen: (Route) -> Unit = { route ->
                    navController.navigate(route.value)
                }
                val onPopToScreen: (Route) -> Unit = { route ->
                    navController.popBackStack(route.value, false)
                }
                composable(
                    route = "StockList",
                    enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) }
                ) {backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("Stock")
                    }
                    val stockViewModel: StockViewModel = viewModel(viewModelStoreOwner = parentEntry)
                    StockListScreen(
                        stockViewModel = stockViewModel,
                        onNavigateToScreen = onNavigateToScreen
                    )
                }
                composable(
                    route = "StockDetail/{index}",
                    arguments = listOf(
                        navArgument("index") { type = NavType.IntType }
                    ),
                    enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) }
                ) {backStackEntry ->
                    val index = backStackEntry.arguments?.getInt("index")
                    if (index != null) {
                        StockDetailScreen(
                            index = index,
                            onPopToScreen = onPopToScreen
                        )
                    }
                }
            }
        }
    }
}