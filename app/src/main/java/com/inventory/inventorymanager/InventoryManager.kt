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
import com.inventory.inventorymanager.data.Category
import com.inventory.inventorymanager.data.InventoryData
import com.inventory.inventorymanager.data.Item
import com.inventory.inventorymanager.database.InventoryDb
import com.inventory.inventorymanager.database.InventoryDbDao

class InventoryManager(context: Context) {

    private val dataAccess : InventoryDbDao = InventoryDb.getInstance(context.applicationContext)!!.InventoryDbDao()

    fun getAllItems() : List<Item> {
        return dataAccess.getAllItems()
    }

    fun itemsByCategoryNonRecursive(category: String) : List<Item> {
        return dataAccess.itemsByCategoryNonRecursive(category)
    }

    fun itemsByCategoryRecursive(category: String) : List<Item> {
        return dataAccess.itemsByCategoryRecursive("$category%")
    }

    fun itemsByName(name: String) : List<Item> {
        return dataAccess.itemsByName("%$name%")
    }

    fun flipIsPacked(name: String, category: String) {
        dataAccess.flipIsPacked(name, category)
    }

    fun getUnpacked() : List<Item> {
        return dataAccess.getUnpacked()
    }

    fun insertItem(item: Item) {
        dataAccess.insertItem(item)
    }

    fun insertAllItems(items: List<Item>) {
        for (item in items) dataAccess.insertItem(item)
    }

    fun replaceInventory(newItems: List<Item>) {
        val oldItems = dataAccess.getAllItems()
        for (item in oldItems) {
            dataAccess.deleteItem(item)
        }
        insertAllItems(newItems)
    }

    fun getEverythingInCategory(category: String) : List<InventoryData> {
        val items = dataAccess.itemsByCategoryNonRecursive(category)
        val subcats = dataAccess.getSubCategories(category)
        return items + subcats
    }

    fun getAllCategories() : List<Category> {
        return dataAccess.getAllCategories()
    }

    fun getSubCategories(rootOfSearch: String): List<Category> {
        return dataAccess.getSubCategories(rootOfSearch)
    }

    fun getSubCategoriesRecursive(rootOfSearch: String): List<Category> {
        var subCats: MutableList<Category> = mutableListOf()
        var subCatsToSearch: MutableList<Category> = mutableListOf()

        //get initial subcategories
        subCats = dataAccess.getSubCategories(rootOfSearch).toMutableList()
        subCatsToSearch = subCats

        //return if there are no subcategories
        if (subCats.isEmpty()) {
            return subCats
        }

        //otherwise search recursively
        for (subcat in subCatsToSearch) {
            subCats.addAll(getSubCategoriesRecursive(subcat.category_name))
        }
        return subCats
    }

    fun getCategoryParent(category: String) : Category {
        return dataAccess.getParent(category)
    }

    fun insertCategory(category: Category) {
        dataAccess.insertCategory(category)
    }
}





