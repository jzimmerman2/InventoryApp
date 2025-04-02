package com.inventory.inventorymanager

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inventory.inventorymanager.data.Item
import com.inventory.inventorymanager.database.InventoryDb
import com.inventory.inventorymanager.database.InventoryDbDao

class InventoryManager(context: Context) {

    private val dataAccess : InventoryDbDao = InventoryDb.getInstance(context.applicationContext)!!.InventoryDbDao()

    fun getAll() : List<Item> {
        return dataAccess.getAll()
    }

    fun searchByCategoryNonRecursive(category: String) : List<Item> {
        return dataAccess.searchByCategoryNonRecursive(category)
    }

    fun searchByCategoryRecursive(category: String) : List<Item> {
        return dataAccess.searchByCategoryRecursive("$category%")
    }

    fun searchByName(name: String) : List<Item> {
        return dataAccess.searchByName("%$name%")
    }

    fun insertItem(item: Item) {
        dataAccess.insertAll(item)
    }

    fun insertAllItems(items: List<Item>) {
        for (item in items) dataAccess.insertAll(item)
    }

    fun replaceInventory(newItems: List<Item>) {
        val oldItems = dataAccess.getAll()
        for (item in oldItems) {
            dataAccess.deleteItem(item)
        }
        insertAllItems(newItems)
    }

    fun flipIsPacked(name: String, category: String) {
        dataAccess.flipIsPacked(name, category)
    }
}





