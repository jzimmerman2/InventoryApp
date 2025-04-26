package com.inventory.addcategoryactivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.inventory.R
import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Category

class AddCategoryActivity : ComponentActivity() {

    private lateinit var inventoryManager: InventoryManager
    private lateinit var categoryNameEntry: EditText
    private lateinit var addCatButton: Button

    private lateinit var currentDirectory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_category)

        inventoryManager = InventoryManager(applicationContext)

        categoryNameEntry = findViewById<EditText>(R.id.AddCategoryEntry)

        currentDirectory = tryRetrieveCurrentDirectory()

        addCatButton = setUpAddCatButton(findViewById<Button>(R.id.ActivityAddCategoryButton), categoryNameEntry,
            currentDirectory, inventoryManager)
    }

    fun setUpAddCatButton(addCatButton: Button, nameEntry: EditText, currentDirectory: String, manager: InventoryManager) : Button {
        addCatButton.setOnClickListener {
            val categoryShortName = nameEntry.text.toString()
            val parent = currentDirectory
            val categoryFullName = "$currentDirectory/$categoryShortName"
            manager.insertCategory(Category(categoryFullName, parent))
        }
        return addCatButton
    }
/*
    fun categoryPathStrToList(category: String) : List<String> {
        category.dropWhile { it == '/' }
        category.dropLastWhile { it == '/' }
        return category.split('/')
    }

    fun categoryPathListToStr(category: List<String>) : String {
        if (category.isEmpty()) return "/"

        var strCategory: String = ""
        for (cat in category) {
            strCategory += "/"
            strCategory += cat
        }
        return strCategory
    }
*/
    fun tryRetrieveCurrentDirectory() : String {
        try {
            return intent.getStringExtra("current directory")!!
        }
        catch (e: Exception) {
            failedRetrieveCurrentDirectory()
        }
        //never make it here
        return ""
    }

    fun failedRetrieveCurrentDirectory() {
        Toast.makeText(applicationContext, "Can't retrieve current directory, exiting activity", Toast.LENGTH_SHORT).show()
        finish()
    }
}