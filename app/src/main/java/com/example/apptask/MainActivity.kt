package com.example.apptask

import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TextClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.apptask.ui.theme.AppTaskTheme
import java.time.LocalTime
import kotlin.concurrent.timer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateForm()
        }
    }
}

const val max = 9999
const val min = 0

@Composable
fun CreateForm() {
    var quantity by remember { mutableIntStateOf(min) }
    Column(modifier = Modifier.padding(all = 8.dp)) {
        Box(modifier = Modifier.fillMaxWidth()){
            Text(
                text = "数量：${"%,d".format(quantity)}",  // カンマ付き表示
                fontSize = 20.sp
            )
            Row(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                // プラスボタン
                Button(
                    onClick = { quantity ++ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier.size(width = 35.dp, height = 30.dp),
                    shape = RoundedCornerShape(3.dp),
                    contentPadding = PaddingValues(0.dp),
                    enabled = when(quantity) {
                        max -> false
                        else -> true
                    }
                ) {
                    Text(text = "＋")
                }
                // マイナスボタン
                Button(
                    onClick = { quantity -- },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier.size(width = 35.dp, height = 30.dp),
                    shape = RoundedCornerShape(3.dp),
                    contentPadding = PaddingValues(0.dp),
                    enabled = when(quantity) {
                        min -> false
                        else -> true
                    }
                ) {
                    Text(text = "－")
                }
            }
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            AndroidView(
                factory = { context ->
                    TextClock(context).apply {
                        format12Hour?.let { this.format12Hour = "hh:mm:ss" }
                        timeZone?.let { this.timeZone = it }
                        textSize.let { this.textSize = 20f }
                    }
                }
            )
            var comment by remember { mutableStateOf("") }
            TextField(
                value = comment,
                onValueChange = { comment = it },
                placeholder = { Text(text = "コメントを入力", fontSize = 15.sp) },
                singleLine = true,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuantityPreview() {
    CreateForm()
}