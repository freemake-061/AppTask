package com.example.apptask

import android.os.Bundle
import android.util.TypedValue
import android.widget.TextClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                CreateForm()
                CreateList(StockData.stockList)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuantityPreview() {
    Column {
        CreateForm()
        CreateList(StockData.stockList)
    }
}


const val max = 9999
const val min = 0

@Composable
fun QuantityForm() {
    Box(modifier = Modifier.fillMaxWidth()){
        Row(
            modifier = Modifier.align(Alignment.TopEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            // 数量
            var quantity by remember { mutableIntStateOf(min) }
            Text(
                text = "数量：${"%,d".format(quantity)}",  // カンマ付き表示
                color = Color.Black,
                fontSize = with(LocalDensity.current) { 30.dp.toSp() }
            )
            Spacer(modifier = Modifier.weight(1f))
            // プラスボタン
            Button(
                onClick = { quantity ++ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.Gray
                ),
                modifier = Modifier.size(width = 50.dp, height = 45.dp),
                shape = RoundedCornerShape(3.dp),
                contentPadding = PaddingValues(0.dp),
                enabled = when(quantity) {
                    max -> false
                    else -> true
                }
            ) {
                Text(
                    text = "＋",
                    fontSize = with(LocalDensity.current) { 15.dp.toSp() }
                )
            }
            // マイナスボタン
            Button(
                onClick = { quantity -- },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.Gray
                ),
                modifier = Modifier.size(width = 50.dp, height = 45.dp),
                shape = RoundedCornerShape(3.dp),
                contentPadding = PaddingValues(0.dp),
                enabled = when(quantity) {
                    min -> false
                    else -> true
                }
            ) {
                Text(
                    text = "－",
                    fontSize = with(LocalDensity.current) { 15.dp.toSp() }
                )
            }
        }
    }
}

@Composable
fun CommentForm() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AndroidView(
                factory = { context ->
                    TextClock(context).apply {
                        format12Hour?.let { this.format12Hour = "HH:mm:ss" }
                        timeZone?.let { this.timeZone = it }
                        textSize.let { this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f) }
                        setTextColor(context.getColor(R.color.black))
                    }
                }
            )

            var comment by remember { mutableStateOf("") }
            BasicTextField(
                value = comment,
                onValueChange = { comment = it },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(5.dp)
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (comment.isEmpty()) {
                            Text(
                                text = "コメントを入力",
                                color = Color.Gray,
                                fontSize = with(LocalDensity.current) { 20.dp.toSp() },  //フォントサイズをdpで指定
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        } else {
                            innerTextField()
                        }
                    }
                }
            )

            Button(
                onClick = { addStock() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.Gray
                ),
                modifier = Modifier.size(width = 50.dp, height = 30.dp),
                shape = RoundedCornerShape(3.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "追加",
                    fontSize = with(LocalDensity.current) { 15.dp.toSp() }
                )
            }
        }
    }
}

fun addStock() {
    StockData.stockList += Stock("23:59:59", 9999, "コメントコメントコメントコメントコメントコメント")
}

@Composable
fun CreateForm() {
    Column(
        modifier = Modifier.padding(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        QuantityForm()
        CommentForm()
    }
}

data class Stock(val clock: String, val quantity: Int, val comment: String)

@Composable
fun StockCard(stc: Stock) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color(0xFFbac3ff))
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = stc.clock)
            Text(text = "%,d".format(stc.quantity))
            Text(
                text = stc.comment,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)  // 長いコメントは省略
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.Gray
                ),
                modifier = Modifier.size(width = 50.dp, height = 30.dp),
                shape = RoundedCornerShape(3.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "削除",
                    fontSize = with(LocalDensity.current) { 15.dp.toSp() }
                )
            }
        }
    }
}

@Composable
fun CreateList(stocks: List<Stock>) {
    LazyColumn {
        items(stocks) { stock ->
            StockCard(stock)
        }
    }
}

object StockData {
    val stockList = mutableListOf(
        Stock("23:59:59", 9999, "コメントコメントコメントコメントコメントコメント"),
        Stock("00:00:00", 0, "")
    )
}