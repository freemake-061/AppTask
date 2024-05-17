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
fun StockCard(ind: Int, stc: Stock) {
    var isChecked by rememberSaveable { mutableStateOf(false) }
    var cardColor = Color(0xFFFFFBFE)
    if (isChecked) {
        cardColor = Color(0xFF00FF00)
    } else if (ind % 2 == 1) {
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
                onCheckedChange = { isChecked = !isChecked }
            )
            Text(text = stc.clock)
            Text(text = "%,d".format(stc.quantity))
            Text(
                text = stc.comment,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Delete",
                modifier = Modifier.clickable { StockData.stocks.removeAt(ind) }
            )
        }
    }
}

@Composable
fun StockList(stocks: List<Stock>) {
    LazyColumn {
        itemsIndexed(stocks) { index, stock ->
            StockCard(index, stock)
        }
    }
}