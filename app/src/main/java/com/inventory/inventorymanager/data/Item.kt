package com.inventory.inventorymanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["name", "category"], tableName = "items")
data class Item(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "isPacked") val isPacked: Boolean = false
)