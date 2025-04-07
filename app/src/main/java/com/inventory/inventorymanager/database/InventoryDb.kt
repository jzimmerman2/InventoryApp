package com.inventory.inventorymanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inventory.inventorymanager.data.Category
import com.inventory.inventorymanager.data.Item

@Database(entities = [Item::class, Category::class], version = 1)
abstract class InventoryDb : RoomDatabase() {
    abstract fun InventoryDbDao(): InventoryDbDao

    companion object {

        @Volatile
        private var instance : InventoryDb? = null

        fun getInstance(context: Context) : InventoryDb? {
            if (null == instance) {
                instance = buildDatabaseInstance(context)
            }
            return instance
        }

        private fun buildDatabaseInstance(context: Context) : InventoryDb{
            return Room.databaseBuilder(context.applicationContext, InventoryDb::class.java, "Inventory.db")
                .allowMainThreadQueries()
                .enableMultiInstanceInvalidation()
                .build()
        }

        fun destroyInstance() {
            instance = null
        }
    }
}