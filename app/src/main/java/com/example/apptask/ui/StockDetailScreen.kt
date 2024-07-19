package com.example.apptask.ui

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.apptask.R
import com.example.apptask.Route

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailScreen(
    stockListViewModel: StockListViewModel,
    onPopToScreen: (Route) -> Unit,
    index: Int
) {
    val stockListUiState by stockListViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(
                        text = stockListUiState.stockList[index].stock.comment,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onPopToScreen(Route.StockListScreen()) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.detail_button_back_desc)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(text = "time:${stockListUiState.stockList[index].stock.time}")
            Text(text = "quantity:${stockListUiState.stockList[index].stock.quantity}")
            Text(text = "comment:${stockListUiState.stockList[index].stock.comment}")
            ImagePicker(
                stockUri = stockListUiState.stockList[index].stock.uri,
                onClickSave = { imageUri ->
                    stockListViewModel.updateImageUri(index, imageUri)
                },
                onPopToScreen = onPopToScreen
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ImagePicker(
    stockUri: Uri?,
    onClickSave: (Uri?) -> Unit,
    onPopToScreen: (Route) -> Unit
) {
    var imageUri: Uri? by rememberSaveable { mutableStateOf(stockUri) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        imageUri = uri
    }
    Column {
        Row {
            Button(
                onClick = { launcher.launch("image/*") }
            ) {
                Text(text = stringResource(R.string.detail_button_add))
            }
            Button(
                onClick = {
                    onClickSave(imageUri)
                    onPopToScreen(Route.StockListScreen())
                }
            ) {
                Text(text = "保存")
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(model = imageUri, contentDescription = stringResource(R.string.detail_image_desc))
        }
    }
}