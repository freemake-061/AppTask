package com.example.apptask.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "stocks")
data class Stock(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quantity: Int,
    val comment: String?,
    val uri: Uri?,
    val deleteFlag: Boolean,
    val createdDateTime: LocalDateTime,
    val updatedDateTime: LocalDateTime
)