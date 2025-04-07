package com.inventory.inventorymanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inventory.inventorymanager.data.Category
import com.inventory.inventorymanager.data.Item

@Dao
interface InventoryDbDao {
    //Items
    @Query("SELECT * FROM items")
    fun getAllItems(): List<Item>

    @Query("SELECT * FROM items WHERE category = :category")
    fun itemsByCategoryNonRecursive(category: String): List<Item>

    @Query("SELECT * FROM items WHERE category LIKE :category")
    fun itemsByCategoryRecursive(category: String): List<Item>

    @Query("SELECT * FROM items WHERE name LIKE :name")
    fun itemsByName(name: String): List<Item>

    @Query("UPDATE items " +
            "SET isPacked = NOT isPacked " +
            "WHERE name = :name AND category = :category")
    fun flipIsPacked(name: String, category: String)

    @Query("SELECT * FROM items WHERE isPacked = false")
    fun getUnpacked() : List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    //categories
    @Query("SELECT * FROM categories")
    fun getAllCategories(): List<Category>

    @Query("SELECT * FROM categories WHERE category_parent = :rootOfSearch")
    fun getSubCategories(rootOfSearch: String): List<Category>

    /*
    @Query("WITH subcategory AS (SELECT category_name AS sc FROM categories WHERE category_parent = :rootOfSearch " +
            "UNION ALL " +
            "SELECT category_name FROM subcategory, categories WHERE subcategory.sc = categories.category_parent) " +
            "SELECT * FROM subcategory")
    fun getSubCategoriesRecursive(rootOfSearch: String): List<Category>
    */

    @Query("SELECT * FROM categories WHERE category_name IN (SELECT category_parent FROM categories WHERE category_name = :category)")
    fun getParent(category: String): Category

    /*@Query("WITH parent AS (SELECT category_parent AS p FROM categories WHERE category_name = :category " +
            "UNION ALL " +
            "SELECT category_parent FROM parent, categories WHERE parent.p = categories.category_name) " +
            "SELECT * FROM parent")
    fun getAllParents
    */

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCategory(category: Category)

}
