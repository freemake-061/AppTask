package com.example.apptask

import android.widget.TextClock
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDialog(setShowDialog: (Boolean) -> Unit) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.form_title),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = colorResource(android.R.color.darker_gray),
                        modifier = Modifier.clickable { setShowDialog(false) }
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var quantity by rememberSaveable { mutableIntStateOf(min) }
                    Text(text = stringResource(R.string.form_quantity) + "%,d".format(quantity))
                    Spacer(modifier = Modifier.weight(1f))
                    ElevatedButton(
                        onClick = { quantity ++ },
                        enabled = when(quantity) {
                            max -> false
                            else -> true
                        }
                    ) {
                        Text(text = "+")
                    }
                    ElevatedButton(
                        onClick = { quantity -- },
                        enabled = when(quantity) {
                            min -> false
                            else -> true
                        }
                    ) {
                        Text(text = "-")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val clockFormat = "hh:mm:ss"
                    val isDarkTheme = isSystemInDarkTheme()
                    AndroidView(
                        factory = { context ->
                            TextClock(context).apply {
                                format12Hour?.let { this.format12Hour = clockFormat }
                                format24Hour?.let { this.format24Hour = clockFormat }
                                timeZone?.let {this.timeZone = null}
                                if (isDarkTheme) {
                                    setTextColor(context.getColor(R.color.white))
                                }
                            }
                        }
                    )
                    var comment by rememberSaveable { mutableStateOf("") }
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
                                        text = stringResource(R.string.form_placeHolder),
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
                    Button(onClick = { setShowDialog(false) }) {
                        Text(text = stringResource(R.string.form_addButton))
                    }
                }
            }
        }
    }
}