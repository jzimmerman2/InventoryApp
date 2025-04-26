package com.inventory.additemactivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

            //category check
            if (manager.getCategoryByName(strCategory).isEmpty()) {
                var parent : String = "/"
                if (strCategory.contains("/")) {
                    val parentTrial = strCategory.substringBeforeLast("/")
                    if (parentTrial != "") parent = parentTrial
                }
                manager.insertCategory(Category(strCategory, parent))
            }

            if (strName != "" &&
                strCategory != ""  &&
                strQuantity != "") {
                val newItem = Item(strName, strCategory, strQuantity.toInt())
                manager.insertItem(newItem)
            }
        }
        return addItemButton
    }
}