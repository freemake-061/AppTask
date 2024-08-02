package com.example.apptask.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class Stock(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uri: Uri?,
    val quantity: Int,
    val comment: String
)