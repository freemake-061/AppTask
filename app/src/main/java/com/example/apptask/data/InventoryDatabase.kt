package com.example.apptask.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Stock::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}