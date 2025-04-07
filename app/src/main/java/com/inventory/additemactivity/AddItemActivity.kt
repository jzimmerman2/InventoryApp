package com.inventory.additemactivity

import android.os.Bundle
import android.text.Layout
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintSet
import com.inventory.R
import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Category
import com.inventory.inventorymanager.data.Item

class AddItemActivity : ComponentActivity() {

    private var inventoryManager: InventoryManager = InventoryManager(applicationContext)
    private lateinit var nameEntry : EditText
    private var categoryEntry : EditText? = null
    private lateinit var quantityEntry : EditText
    private lateinit var addItemButton: Button

    private lateinit var cameFrom : FromWhere
    private lateinit var currentDirectory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        cameFrom = tryRetrieveFromWhere()

        currentDirectory = tryRetrieveCurrentDirectory()

        initializeViews(cameFrom)

        addItemButton = setUpAddItemButton(addItemButton, nameEntry, categoryEntry, currentDirectory, quantityEntry, inventoryManager, cameFrom)
    }

    fun setUpAddItemButton(addItemButton: Button, name: EditText, category: EditText?, currentDir: String, quantity: EditText, manager : InventoryManager, fromWhere: FromWhere) :
        Button {

        when(fromWhere) {
            FromWhere.FROMLIST -> {
                addItemButton.setOnClickListener {
                    if (category != null) {
                        val strName = name.text.toString()
                        if (containsForwardSlash(strName)) {
                            Toast.makeText(applicationContext, "Item names can't contain forward slashes", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        val pathListCategory = categoryPathStrToList(category.text.toString())
                        var pathListParentCategory: List<String> =
                            if (pathListCategory.isEmpty()) listOf() else pathListCategory.dropLast(1)

                        val strQuantity = quantity.text.toString()

                        if (strName != "" && strQuantity != ""){
                            val strCategory = categoryPathListToStr(pathListCategory)
                            val strCategoryParent = categoryPathListToStr(pathListParentCategory)
                            manager.insertCategory(Category(strCategory, strCategoryParent))

                            val newItem = Item(strName, strCategory, strQuantity.toInt())
                            manager.insertItem(newItem)
                        }
                    }
                }
            }
            FromWhere.FROMDIRECTORY -> {
                addItemButton.setOnClickListener {
                    val strName = name.text.toString()
                    if (containsForwardSlash(strName)) {
                        Toast.makeText(applicationContext, "Item names can't contain forward slashes", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val strQuantity = quantity.text.toString()

                    if(strName != "" && strQuantity != "") {
                        val newItem = Item(strName, currentDir, strQuantity.toInt())
                        manager.insertItem(newItem)
                    }
                }
            }
        }
        return addItemButton
    }

    fun initializeViews(cameFrom: FromWhere){
        when (cameFrom) {
            FromWhere.FROMLIST -> {
                setContentView(R.layout.add_item_from_list)
                nameEntry = findViewById<EditText>(R.id.AddItemNameEntry)
                categoryEntry = findViewById<EditText>(R.id.AddItemCategoryEntry)
                quantityEntry = findViewById<EditText>(R.id.AddItemQuantityEntry)
                addItemButton = findViewById<Button>(R.id.AddItemButton)
            }
            FromWhere.FROMDIRECTORY -> {
                setContentView(R.layout.add_item_from_directory)
                nameEntry = findViewById<EditText>(R.id.AddItemFromDirectoryNameEntry)
                quantityEntry = findViewById<EditText>(R.id.AddItemFromDirectoryQuantityEntry)
                addItemButton = findViewById<Button>(R.id.AddItemFromDirectoryButton)
            }
        }
    }

    fun containsForwardSlash(text: String) : Boolean {
        return text.indexOf("/") > -1
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
        return try {
            intent.getStringExtra("current directory")!!
        }
        catch (e: Exception) {
            defaultFromList()
            ""
        }
    }

    fun tryRetrieveFromWhere(): FromWhere {
        return try {
            intent.getSerializableExtra("from")!! as FromWhere
        } catch (e: Exception) {
            FromWhere.FROMLIST
        }
    }

    fun defaultFromList() {
        cameFrom = FromWhere.FROMLIST
    }
}