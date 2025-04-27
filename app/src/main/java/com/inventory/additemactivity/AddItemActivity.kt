package com.inventory.additemactivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.inventory.R
import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Category
import com.inventory.inventorymanager.data.Item

class AddItemActivity : ComponentActivity() {

    private lateinit var inventoryManager: InventoryManager
    private lateinit var nameEntry : EditText
    private lateinit var categoryEntry : EditText
    private lateinit var quantityEntry : EditText
    private lateinit var addItemButton: Button
    private lateinit var fromWhere: FromWhere

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item)

        inventoryManager = InventoryManager(applicationContext)
        nameEntry = findViewById<EditText>(R.id.AddItemNameEntry)
        categoryEntry = findViewById<EditText>(R.id.AddItemCategoryEntry)
        quantityEntry = findViewById<EditText>(R.id.AddItemQuantityEntry)
        addItemButton = findViewById<Button>(R.id.AddItemButton)

        addItemButton = setUpAddItemButton(addItemButton, nameEntry, categoryEntry, quantityEntry, inventoryManager)
    }

    fun setUpAddItemButton(addItemButton: Button, name: EditText, category: EditText, quantity: EditText, manager : InventoryManager) :
        Button {

        addItemButton.setOnClickListener {
            val strName = name.text.toString()
            val strCategory = category.text.toString()
            val strQuantity = quantity.text.toString()

            if (strName != "" &&
                strCategory != ""  &&
                strQuantity != "") {
                val newItem = Item(strName, strCategory, strQuantity.toInt())
                manager.insertItem(newItem)
            }
        }
        return addItemButton
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