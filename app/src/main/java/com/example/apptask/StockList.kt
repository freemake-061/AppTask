package com.example.apptask

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockCard(
    index: Int,
    stockRowData: StockRowData,
    onCheckedChange: (Boolean) -> Unit,
    onClickStock: () -> Unit,
    onClickDelete: () -> Unit
) {
    var cardColor = Color(0xFFFFFBFE)
    if (stockRowData.isChecked) {
        cardColor = Color(0xFF00FF00)
    } else if (index % 2 == 1) {
        cardColor = Color(0xFFE6E6FA)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = cardColor)
            .combinedClickable(
                onClick = { onClickStock() },
                /*
                後で選択に関すること変更
                onLongClick = { onCheckedChange(!stockRowData.isChecked) }
                 */
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Checkbox(
                checked = stockRowData.isChecked,
                onCheckedChange = onCheckedChange
            )
            Text(text = stockRowData.stock.clock)
            Text(text = "%,d".format(stockRowData.stock.quantity))
            Text(
                text = stockRowData.stock.comment,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.form_button_desc_delete),
                modifier = Modifier.clickable { onClickDelete() }
            )
        }
    }
}

@Composable
fun StockList(
    stockRowList: List<StockRowData>,
    onCheckedChange: (Int, Boolean) -> Unit,
    onClickStock: () -> Unit,
    onClickDelete: (Int) -> Unit
) {
    LazyColumn {
        itemsIndexed(stockRowList) { index, stockRowData ->
            StockCard(
                index = index,
                stockRowData = stockRowData,
                onCheckedChange = { isChecked ->
                    onCheckedChange(index, isChecked)
                },
                onClickStock = { onClickStock() },
                onClickDelete = { onClickDelete(index) }
            )
        }
    }
}