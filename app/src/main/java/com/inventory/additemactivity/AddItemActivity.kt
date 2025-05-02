package com.inventory.additemactivity

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
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
    private lateinit var quantityEntry : EditText
    private lateinit var addItemButton: Button
    private lateinit var fromWhere: FromWhere
    private lateinit var currentDirectory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item)

        fromWhere = when (intent.getStringExtra("from")) {
            "list activity" -> FromWhere.FROMLIST
            "directory activity" -> FromWhere.FROMDIRECTORY
            else -> FromWhere.FROMLIST
        }
        currentDirectory = intent.getStringExtra("current directory")!!

        inventoryManager = InventoryManager(applicationContext)
        nameEntry = findViewById<EditText>(R.id.AddItemNameEntry)
        quantityEntry = findViewById<EditText>(R.id.AddItemQuantityEntry)
        addItemButton = findViewById<Button>(R.id.AddItemButton)

        addItemButton = setUpAddItemButton(
            addItemButton,
            nameEntry,
            currentDirectory,
            quantityEntry,
            inventoryManager
        )
    }



    fun setUpAddItemButton(addItemButton: Button, name: EditText, currentDirectory: String, quantity: EditText, manager : InventoryManager) :
        Button {

        addItemButton.setOnClickListener {
            val strName = name.text.toString()
            val strQuantity = quantity.text.toString()

            if (strName != "" &&
                strQuantity != "") {
                val newItem = Item(strName, currentDirectory, strQuantity.toInt())
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