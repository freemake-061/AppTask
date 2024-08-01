package com.example.apptask.data

import android.app.Application
import androidx.room.Room

class InventoryApplication : Application() {
    companion object {
        lateinit var  database: InventoryDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext, InventoryDatabase::class.java,"item_database"
        ).build()
    }
}