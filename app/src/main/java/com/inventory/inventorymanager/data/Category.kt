package com.inventory.inventorymanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["name", "parent"], tableName = "categories")
data class Category(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "parent") val parent: String
) : InventoryData() {

    fun getShortName() : String {
        return name.substringAfterLast("/")
    }
}