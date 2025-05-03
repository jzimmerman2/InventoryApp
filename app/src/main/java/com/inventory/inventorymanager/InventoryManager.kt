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

    //------------------ITEMS--------------------
    fun getAllItems() : List<Item> {
        return dataAccess.getAllItems()
    }

    fun searchItemsByCategoryNonRecursive(category: String) : List<Item> {
        return dataAccess.searchItemsByCategoryNonRecursive(category)
    }

    fun searchItemsByCategoryRecursive(category: String) : List<Item> {
        return dataAccess.searchItemsByCategoryRecursive("$category%")
    }

    fun searchByName(name: String) : List<Item> {
        return dataAccess.searchItemsByName("%$name%")
    }

    fun flipIsPacked(name: String, category: String) {
        dataAccess.flipIsPacked(name, category)
    }

    fun getUnpackedItems() : List<Item> {
        return dataAccess.getUnpackedItems()
    }

    fun insertItem(item: Item) {
        dataAccess.insertItem(item)
    }

    fun insertItems(items: List<Item>) {
        for (item in items) dataAccess.insertItem(item)
    }

    fun deleteItem(item: Item) {
        dataAccess.deleteItem(item)
    }

    fun deleteItems(items: List<Item>) {
        for (item in items) dataAccess.deleteItem(item)
    }

    fun DELETEALLITEMS() {
        val items = dataAccess.getAllItems()
        deleteItems(items)
    }

    fun replaceItems(newItems: List<Item>) {
        val oldItems = dataAccess.getAllItems()
        deleteItems(oldItems)
        insertItems(newItems)
    }

    //-----------CATEGORIES-----------------
    fun getAllCategories() : List<Category> {
        return dataAccess.getAllCategories()
    }

    fun getSubCategories(rootOfSearch: String) : List<Category> {
        return dataAccess.getSubCategories(rootOfSearch)
    }

    fun getSubCategoriesRecursive(rootOfSearch: String) : List<Category> {
        var subCategories : MutableList<Category> = getSubCategories(rootOfSearch).toMutableList()
        var subCategoriesRecursive : MutableList<Category> = mutableListOf()

        for (subCat in subCategories) {
            subCategoriesRecursive.addAll(getSubCategoriesRecursive(subCat.name))
        }

        subCategories.addAll(subCategoriesRecursive)

        return subCategories
    }

    fun getCategoryParent(category: String) : List<Category> {
        return dataAccess.getParent(category)
    }

    fun getCategoryByName(category: String) : List<Category> {
        return dataAccess.getCategoryByName(category)
    }

    fun insertCategory(category: Category) {
        dataAccess.insertCategory(category)
    }

    fun deleteCategory(category: Category) {
        dataAccess.deleteCategory(category)
    }

    fun deleteCategories(categories: List<Category>) {
        for (category in categories) dataAccess.deleteCategory(category)
    }

    fun DELETEALLCATEGORIES() {
        val categories = dataAccess.getAllCategories()
        deleteCategories(categories)
    }

    //--------------CATEGORIES AND ITEMS---------------
    fun getEverythingInCategory(curDir: String) : List<InventoryData> {
        val items = dataAccess.searchItemsByCategoryNonRecursive(curDir)
        val categories = dataAccess.getSubCategories(curDir)

        return categories + items as List<InventoryData>
    }

    fun searchDataByNameInCategory(name: String, curDir : String) : List<InventoryData> {
        val items : List<Item> = dataAccess.searchItemsByNameInCategory("%$name%", curDir)
        val categories : List<Category> = dataAccess.searchCategoriesByNameInCategory("%$name%", curDir)

        return categories + items as List<InventoryData>
    }
}





