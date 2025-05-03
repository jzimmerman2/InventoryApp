package com.inventory.inventorymanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inventory.inventorymanager.data.Category
import com.inventory.inventorymanager.data.InventoryData
import com.inventory.inventorymanager.data.Item

@Dao
interface InventoryDbDao {
    //--------------ITEMS---------------------------
    @Query("SELECT * FROM items")
    fun getAllItems(): List<Item>

    @Query("SELECT * FROM items WHERE category = :category")
    fun searchItemsByCategoryNonRecursive(category: String): List<Item>

    @Query("SELECT * FROM items WHERE category LIKE :category")
    fun searchItemsByCategoryRecursive(category: String): List<Item>

    @Query("SELECT * FROM items WHERE name LIKE :name")
    fun searchItemsByName(name: String): List<Item>

    @Query("SELECT * FROM items WHERE category = :category AND name LIKE :name")
    fun searchItemsByNameInCategory(name : String, category : String) : List<Item>

    @Query("UPDATE items " +
            "SET isPacked = NOT isPacked " +
            "WHERE name = :name AND category = :category")
    fun flipIsPacked(name: String, category: String)

    @Query("SELECT * FROM items WHERE isPacked = false")
    fun getUnpackedItems() : List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    //-------------CATEGORIES--------------
    @Query("SELECT * FROM categories")
    fun getAllCategories() : List<Category>

    @Query("SELECT * FROM categories WHERE parent = :rootOfSearch")
    fun getSubCategories(rootOfSearch: String): List<Category>

    @Query("SELECT * FROM categories WHERE parent = :category AND name LIKE :name")
    fun searchCategoriesByNameInCategory(name : String, category: String) : List<Category>

    @Query("SELECT * FROM categories WHERE name IN (SELECT parent FROM categories WHERE name = :category)")
    fun getParent(category: String) : List<Category>

    @Query("SELECT * FROM categories WHERE name = :category")
    fun getCategoryByName(category: String) : List<Category>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    //-------ITEMS AND CATEGORIES---------
}