package com.example.apptask

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp

@Composable
fun StockList() {
    var isChecked by rememberSaveable { mutableStateOf(false) }
    var lineColor = Color(0xFFe6e6fa)
    if (isChecked) {
        lineColor = Color(0xFF00ff00)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = lineColor)
            .clickable { isChecked = !isChecked }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = !isChecked }
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = "23:59:59 9999 Test"
            )
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}