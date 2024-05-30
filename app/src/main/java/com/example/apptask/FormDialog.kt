package com.example.apptask

import android.os.Build
import android.widget.TextClock
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDialog(
    onDismissRequest: () -> Unit,
    onClickClose: () -> Unit,
    onClickAdd: (StockCardData) -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                var quantity by rememberSaveable { mutableIntStateOf(Constants.STOCK_QUANTITY_MIN) }
                var comment by rememberSaveable { mutableStateOf("") }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.form_label_title),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.form_button_desc_close),
                        tint = colorResource(android.R.color.darker_gray),
                        modifier = Modifier.clickable { onClickClose() }
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.form_label_quantity) + "%,d".format(quantity))
                    Spacer(modifier = Modifier.weight(1f))
                    ElevatedButton(
                        onClick = { quantity ++ },
                        enabled = when(quantity) {
                            Constants.STOCK_QUANTITY_MAX -> false
                            else -> true
                        }
                    ) {
                        Text(text = stringResource(R.string.form_button_plus))
                    }
                    ElevatedButton(
                        onClick = { quantity -- },
                        enabled = when(quantity) {
                            Constants.STOCK_QUANTITY_MIN -> false
                            else -> true
                        }
                    ) {
                        Text(text = stringResource(R.string.form_button_minus))
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isDarkTheme = isSystemInDarkTheme()
                    AndroidView(
                        factory = { context ->
                            TextClock(context).apply {
                                format12Hour?.let { this.format12Hour = Constants.CLOCK_FORMAT }
                                format24Hour?.let { this.format24Hour = Constants.CLOCK_FORMAT }
                                timeZone?.let { this.timeZone = null }
                                if (isDarkTheme) {
                                    setTextColor(context.getColor(R.color.white))
                                }
                            }
                        }
                    )
                    BasicTextField(
                        modifier = Modifier.weight(1f),
                        value = comment,
                        onValueChange = { comment = it },
                        singleLine = true,
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
                                    bottom = 0.dp
                                ),
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.form_placeholder),
                                        style = TextStyle(color = Color.Gray)
                                    )
                                }
                            )
                        }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val formatTime = DateTimeFormatter.ofPattern(Constants.CLOCK_FORMAT)
                            val currentTime = formatTime.format(LocalDateTime.now())
                            onClickAdd(
                                StockCardData(
                                    isChecked = false,
                                    Stock(
                                        clock = currentTime,
                                        quantity = quantity,
                                        comment = comment
                                    )
                                )
                            )
                        }
                    ) {
                        Text(text = stringResource(R.string.form_button_add))
                    }
                }
            }
        }
    }
}