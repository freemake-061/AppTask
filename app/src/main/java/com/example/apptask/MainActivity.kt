package com.example.apptask

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.apptask.ui.theme.AppTaskTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTaskTheme {
                Surface {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        CreateForm()
                        CreateList(StockData.stockList)
                    }
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    name = "light Mode"
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuantityPreview() {
    AppTaskTheme {
        Surface {
            Column(modifier = Modifier.fillMaxHeight()) {
                CreateForm()
                CreateList(StockData.stockList)
            }
        }
    }
}

data class Stock(val clock: String, val quantity: Int, val comment: String)
const val max = 9999
const val min = 0
var sum = 0

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateForm() {
    Column(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        var quantity by rememberSaveable { mutableIntStateOf(min) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            // 数量
            Text(
                text = "数量：${"%,d".format(quantity)}",  // カンマ付き表示
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 時刻
            val darkTheme: Boolean = isSystemInDarkTheme()
            AndroidView(
                factory = { context ->
                    TextClock(context).apply {
                        format12Hour?.let { this.format12Hour = "hh:mm:ss" }
                        format24Hour?.let { this.format24Hour = "hh:mm:ss" }
                        timeZone?.let { this.timeZone = null }
                        textSize.let { this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f) }
                        if (darkTheme) {
                            setTextColor(context.getColor(R.color.white))
                        } else {
                            setTextColor(context.getColor(R.color.black))
                        }
                    }
                }
            )
            // コメント入力欄
            val focusManager = LocalFocusManager.current
            var comment by rememberSaveable { mutableStateOf("") }
            BasicTextField(
                value = comment,
                modifier = Modifier.weight(1f),
                onValueChange = { comment = it },
                singleLine = true,
                textStyle = TextStyle(fontSize = with(LocalDensity.current) { 20.dp.toSp() }),
                decorationBox = @Composable { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = comment,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = MutableInteractionSource(),
                        contentPadding = TextFieldDefaults.contentPaddingWithLabel(
                            start = 0.dp,
                            top = 0.dp,
                            end = 0.dp,
                            bottom = 0.dp,
                        ),
                        placeholder = {
                            Text(
                                text = "コメントを入力",
                                color = Color.Gray,
                                fontSize = with(LocalDensity.current) { 15.dp.toSp() }
                            )
                        }
                    )
                }
            )
            // 追加ボタン
            Button(
                onClick = {
                    val formatTime = DateTimeFormatter.ofPattern("HH:mm:ss")
                    val currentTime = formatTime.format(LocalDateTime.now())
                    focusManager.clearFocus()
                    StockData.stockList += Stock(
                        clock = currentTime,
                        quantity = quantity,
                        comment = comment
                    )
                    quantity = 0
                    comment = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.Gray
                ),
                modifier = Modifier.size(width = 50.dp, height = 30.dp),
                shape = RoundedCornerShape(3.dp),
                contentPadding = PaddingValues(0.dp),
                enabled = comment.isNotEmpty()
            ) {
                Text(
                    text = "追加",
                    fontSize = with(LocalDensity.current) { 15.dp.toSp() }
                )
            }
        }
    }
}

@Composable
fun StockCard(ind: Int, stc: Stock) {
    var isChecked by remember { mutableStateOf(false) }
    var cardColor = Color(0xFFFFFBFE)
    if (isChecked) {
        cardColor = Color(0xFF00ff00)
    } else if (ind % 2 == 0) {
        cardColor = Color(0xFFe6e6fa)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = cardColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                modifier = Modifier.size(15.dp),
                checked = isChecked,
                onCheckedChange = {
                    isChecked = !isChecked
                    if (isChecked) {
                        sum += stc.quantity
                    } else {
                        sum -= stc.quantity
                    }
                }
            )
            // 時刻
            Text(
                text = stc.clock,
                overflow = TextOverflow.Ellipsis,
                fontSize = with(LocalDensity.current) { 15.dp.toSp() }
            )
            // 数量
            Box(
                modifier = Modifier.size(width = 40.dp, height = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ){
                Text(
                    text = "%,d".format(stc.quantity),
                    overflow = TextOverflow.Ellipsis,
                    fontSize = with(LocalDensity.current) { 15.dp.toSp() }
                )
            }
            // コメント
            if (stc.comment.isEmpty()) {
                Text(
                    text = "コメント無し",
                    color = Color.LightGray,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = with(LocalDensity.current) { 15.dp.toSp() }
                )
            } else {
                Text(
                    text = stc.comment,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,    // 長いコメントは省略
                    fontSize = with(LocalDensity.current) { 15.dp.toSp() }
                )
            }
            // 削除ボタン
            Button(
                onClick = {
                    if (isChecked) {
                        sum -= stc.quantity
                    }
                    StockData.stockList.removeAt(ind)
                },
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
        itemsIndexed(stocks) { index, stock ->
            StockCard(index, stock)
        }
    }
    var alertDialog by rememberSaveable { mutableStateOf(false) }
    var sumDialog by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.End)
    ) {
        // クリアボタン
        Button(
            onClick = {
                alertDialog = true
            },
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
                text = "クリア",
                fontSize = with(LocalDensity.current) { 15.dp.toSp() }
            )
        }
        // 合計ボタン
        Button(
            onClick = { sumDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.Gray
            ),
            modifier = Modifier.size(width = 150.dp, height = 30.dp),
            shape = RoundedCornerShape(3.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "選択された合計数量",
                fontSize = with(LocalDensity.current) { 15.dp.toSp() }
            )
        }
    }
    if (alertDialog) {
        AlertDialog(
            onDismissRequest = { alertDialog = false },
            text = {
                Text(text = "本当に削除しますか？")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        StockData.stockList.clear()
                        sum = 0
                        alertDialog = false
                    }
                ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { alertDialog = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
    if (sumDialog) {
        AlertDialog(
            onDismissRequest = { sumDialog = false },
            text = {
                Text(text = "合計${"%,d".format(sum)}です。")
            },
            confirmButton = {
                TextButton(onClick = { sumDialog = false }) {
                    Text(text = "OK")
                }
            },
        )
    }
}

object StockData {
    var stockList = mutableStateListOf(
        Stock("23:59:59", 9999, "コメント")
    )
}