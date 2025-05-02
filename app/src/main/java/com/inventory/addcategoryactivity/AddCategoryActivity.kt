package com.inventory.addcategoryactivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.inventory.R
import com.inventory.additemactivity.FromWhere
import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Category

class AddCategoryActivity : ComponentActivity() {

    private lateinit var inventoryManager: InventoryManager
    private lateinit var categoryNameEntry: EditText
    private lateinit var addCatButton: Button

    private lateinit var currentDirectory: String
    private lateinit var fromWhere: FromWhere

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fromWhere = tryRetrieveFromWhere()
        currentDirectory = tryRetrieveCurrentDirectory()


        setContentView(R.layout.activity_add_category)

        inventoryManager = InventoryManager(applicationContext)

        categoryNameEntry = findViewById<EditText>(R.id.AddCategoryEntry)

        addCatButton = setUpAddCatButton(findViewById<Button>(R.id.ActivityAddCategoryButton), categoryNameEntry, currentDirectory, inventoryManager)
    }

    fun setUpAddCatButton(addCatButton: Button, nameEntry: EditText, currentDirectory: String, manager: InventoryManager) : Button {
        addCatButton.setOnClickListener {
            val categoryShortName = nameEntry.text.toString()
            val parent = currentDirectory
            val categoryFullName = if (parent == "/") "/$categoryShortName" else "$currentDirectory/$categoryShortName"
            manager.insertCategory(Category(categoryFullName, parent))
        }
        return addCatButton
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

    fun tryRetrieveFromWhere() : FromWhere {
        try {
            val from = intent.getStringExtra("from")!!

            if (from == "directory_activity") {
                return FromWhere.FROMDIRECTORY
            }
            else if (from == "list_activity") {
                return FromWhere.FROMLIST
            }
        }
        catch (e: Exception) {
            failedRetrieveFrom()
        }
        //never make it here
        return FromWhere.FROMLIST
    }

    fun failedRetrieveFrom() {
        Toast.makeText(applicationContext, "Can't get from where, exiting activity", Toast.LENGTH_SHORT).show()
        finish()
    }
}