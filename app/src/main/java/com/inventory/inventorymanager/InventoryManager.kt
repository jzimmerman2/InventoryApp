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

    fun searchItemsByNameInCategory(name: String, category: String) : List<Item> {
        return dataAccess.searchItemsByNameInCategory("%$name%", category)
    }

    fun searchItemsByNameInCategoryRecursive(name: String, category: String) : List<Item> {
        val parent : String = "/${category.substringBeforeLast("/")}"
        val catShortName : String = category.substringAfterLast("/")

        //var resultsInCategory : List<Item> = dataAccess.searchItemsByNameInCategory("%$name%", "$parent/$catShortName")
        //var resultsInSubCategories : List<Item> = dataAccess.searchItemsByNameInCategoryRecursive("%$name%", "$parent/$catShortName/%")
        var resultsInCategory : List<Item> = dataAccess.searchItemsByNameInCategory("%$name%", category)
        var resultsInSubCategories : List<Item> = dataAccess.searchItemsByNameInCategoryRecursive("%$name%", "$category/%")

        return resultsInCategory + resultsInSubCategories
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

    fun moveItem(item : Item, newDir: String) {
        insertItem(Item(item.name, newDir, item.quantity, item.isPacked))
        deleteItem(item)
    }

    fun moveItems(items : List<Item>, newDir: String) {
        for (item in items) moveItem(item, newDir)
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

    fun searchCategoriesByNameInCategory(name : String, category: String) : List<Category> {
        return dataAccess.searchCategoriesByNameInCategory("%$name%", category)
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
        val everythingInCategory: List<InventoryData> = getEverythingInCategory(category.name)
        for (data in everythingInCategory) {
            when (data) {
                is Item -> dataAccess.deleteItem(data)
                is Category -> dataAccess.deleteCategory(data)
            }
        }
        dataAccess.deleteCategory(category)
    }

    fun deleteCategories(categories: List<Category>) {
        for (category in categories) deleteCategory(category)
    }

    fun DELETEALLCATEGORIES() {
        val categories = dataAccess.getAllCategories()
        deleteCategories(categories)
    }

    fun moveCategory(category: Category, newDir: String) {
        //check that we're not moving category into itself
        if (newDir.contains(category.name)) return

        //move category
        insertCategory(Category("$newDir/${category.getShortName()}", newDir))

        val itemsInCategory : List<Item> = searchItemsByCategoryNonRecursive(category.name)
        moveItems(itemsInCategory, "$newDir/${category.getShortName()}")

        val subCategories : List<Category> = getSubCategories(category.name)
        for (subCat in subCategories) {
            moveCategory(subCat, "$newDir/${category.getShortName()}")
        }

        deleteCategory(category)
    }

    //--------------CATEGORIES AND ITEMS---------------
    fun getEverythingInCategory(curDir: String) : List<InventoryData> {
        val items = dataAccess.searchItemsByCategoryNonRecursive(curDir)
        val categories = dataAccess.getSubCategories(curDir)

        return categories + items as List<InventoryData>
    }

    fun deleteData(data: InventoryData) {
        when (data) {
            is Item -> deleteItem(data)
            is Category -> deleteCategory(data)
        }
    }

    fun deleteDatas(datas : List<InventoryData>) {
        for (data in datas) deleteData(data)
    }

    fun moveData(data : InventoryData, newDir: String) {
        when (data) {
            is Item -> moveItem(data, newDir)
            is Category -> moveCategory(data, newDir)
        }
    }

    fun searchDataByNameInCategory(name : String, category: String) : List<InventoryData> {
        val items : List<Item> = searchItemsByNameInCategory(name, category)
        val categories : List<Category> = searchCategoriesByNameInCategory(name, category)

        return categories + items as List<InventoryData>
    }
}





