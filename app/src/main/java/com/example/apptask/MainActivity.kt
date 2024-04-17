package com.example.apptask

import android.content.res.ColorStateList
import android.icu.util.Calendar
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.indicatorLine
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
import androidx.compose.ui.platform.LocalDensity
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
    Column(
        modifier = Modifier.padding(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            Text(
                text = "数量：${"%,d".format(quantity)}",  // カンマ付き表示
                color = Color.Black,
                fontSize = with(LocalDensity.current) { 30.dp.toSp() }  //フォントサイズをdpで指定
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
                        fontSize = with(LocalDensity.current) { 15.dp.toSp() }  //フォントサイズをdpで指定
                    )
                }
                // マイナスボタン
                Button(
                    onClick = { quantity -- },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.DarkGray
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
                        fontSize = with(LocalDensity.current) { 15.dp.toSp() }  //フォントサイズをdpで指定
                    )
                }
            }
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.align(Alignment.TopEnd),
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
                    },
                    modifier = Modifier.padding(2.dp)
                )
                var comment by remember { mutableStateOf("") }
                BasicTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    singleLine = true,
                    modifier = Modifier
                        .size(width = 220.dp, height = 30.dp)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(1.dp)
                        )
                        .padding(0.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(0.dp)
                        ) {
                            if (comment.isEmpty()) {
                                Text(
                                    text = "コメントを入力",
                                    color = Color.Gray,
                                    fontSize = with(LocalDensity.current) { 20.dp.toSp() },  //フォントサイズをdpで指定
                                    modifier = Modifier.padding(
                                        start = 5.dp
                                    )
                                )
                            } else {
                                innerTextField()
                            }
                        }
                    }
                )
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.Black
                    ),
                    modifier = Modifier.size(width = 50.dp, height = 30.dp),
                    shape = RoundedCornerShape(3.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "追加",
                        fontSize = with(LocalDensity.current) { 15.dp.toSp() }  //フォントサイズをdpで指定
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuantityPreview() {
    CreateForm()
}