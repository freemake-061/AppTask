package com.example.apptask.data

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Entity(tableName = "stocks")
data class Stock(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quantity: Int = 0,
    val comment: String? = null,
    val uri: Uri? = null,
    val deleteFlag: Boolean = false,
    val createdDateTime: LocalDateTime = LocalDateTime.of(0, 0, 0, 0, 0),
    val updatedDateTime: LocalDateTime = LocalDateTime.of(0, 0, 0, 0, 0)
)