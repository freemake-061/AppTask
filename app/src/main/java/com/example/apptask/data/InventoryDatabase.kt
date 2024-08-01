package com.example.apptask.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Stock::class], version = 1, exportSchema = false)
@TypeConverters(UriConverter::class)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}