package com.example.apptask.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stock: Stock)

    @Update
    suspend fun update(stock: Stock)

    @Delete
    suspend fun delete(stock: Stock)

    @Query("SELECT * from stocks WHERE id = :id")
    fun getStock(id: Int): Flow<Stock>

    @Query("SELECT * from stocks ORDER BY id ASC")
    fun getAllStocks(): Flow<List<Stock>>
}