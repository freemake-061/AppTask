package com.example.apptask

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun StockCard(index: Int, stockCardData: StockCardData, onCheckedChange: (Boolean) -> Unit ) {
    var isChecked by rememberSaveable { mutableStateOf(false) }
    var cardColor = Color(0xFFFFFBFE)
    if (stockCardData.isChecked) {
        cardColor = Color(0xFF00FF00)
    } else if (index % 2 == 1) {
        cardColor = Color(0xFFE6E6FA)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = cardColor)
            .clickable { isChecked = !isChecked }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
            Text(text = stockCardData.stock.clock)
            Text(text = "%,d".format(stockCardData.stock.quantity))
            Text(
                text = stockCardData.stock.comment,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Delete",
                modifier = Modifier.clickable { stocks.removeAt(index) }
            )
        }
    }
}

@Composable
fun StockList(stocks: List<StockCardData>) {
    LazyColumn {
        itemsIndexed(stocks) { index, stock ->
            StockCard(index, stock, onCheckedChange = )
        }
    }
}