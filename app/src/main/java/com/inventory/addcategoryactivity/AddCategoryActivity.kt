package com.inventory.addcategoryactivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.inventory.R
import com.inventory.directoryactivity.CurrentDirectory
import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Category

class AddCategoryActivity : ComponentActivity() {

    private var inventoryManager = InventoryManager(applicationContext)
    private lateinit var categoryNameEntry: EditText
    private lateinit var addCatButton: Button

    private lateinit var currentDirectory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_category_from_directory)

        categoryNameEntry = findViewById<EditText>(R.id.AddCategoryFromDirectoryNameEntry)

        currentDirectory = tryRetrieveCurrentDirectory()

        addCatButton = setUpAddCatButton(findViewById<Button>(R.id.AddCategoryFromDirectoryButton), categoryNameEntry,
            currentDirectory.toString(), inventoryManager)
    }

    fun setUpAddCatButton(addCatButton: Button, nameEntry: EditText, currentDirectory: String, manager: InventoryManager) : Button {
        addCatButton.setOnClickListener {
            val pathListCategory = categoryPathStrToList(nameEntry.text.toString())
            var pathListParentCategory: List<String> =
                if (pathListCategory.isEmpty()) listOf() else pathListCategory.dropLast(1)

            val strCategory = categoryPathListToStr(pathListCategory)
            val strCategoryParent = categoryPathListToStr(pathListParentCategory)
            manager.insertCategory(Category(strCategory, strCategoryParent))
        }
        return addCatButton
    }

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