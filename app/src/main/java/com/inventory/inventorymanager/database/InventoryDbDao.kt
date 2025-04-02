package com.inventory.inventorymanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inventory.inventorymanager.data.Item

@Dao
interface InventoryDbDao {
    @Query("SELECT * FROM items")
    fun getAll(): List<Item>

    @Query("SELECT * FROM items WHERE category = :category")
    fun searchByCategoryNonRecursive(category: String): List<Item>

    @Query("SELECT * FROM items WHERE category LIKE :category")
    fun searchByCategoryRecursive(category: String): List<Item>

    @Query("SELECT * FROM items WHERE name LIKE :name")
    fun searchByName(name: String): List<Item>

    @Query("UPDATE items " +
            "SET isPacked = NOT isPacked " +
            "WHERE name = :name AND category = :category")
    fun flipIsPacked(name: String, category: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg item: Item)

    @Delete
    fun deleteItem(item: Item)
}