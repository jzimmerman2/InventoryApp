package com.inventory.inventorymanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["category_name"], tableName = "categories")
data class Category(
    @ColumnInfo(name = "category_name") val category_name: String,
    @ColumnInfo(name = "category_parent") val category_parent: String
) : InventoryData()
