package com.inventory.directoryactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inventory.R
import com.inventory.addcategoryactivity.AddCategoryActivity
import com.inventory.additemactivity.AddItemActivity
import com.inventory.additemactivity.FromWhere
import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Category
import com.inventory.inventorymanager.data.InventoryData
import com.inventory.inventorymanager.data.Item
import com.inventory.listactivity.InventoryListAdapter
import java.util.concurrent.RecursiveTask

class DirectoryActivity : ComponentActivity() {

    private lateinit var inventoryManager: InventoryManager
    private lateinit var inventoryListAdapter: DirectoryListAdapter
    private lateinit var inventoryList: RecyclerView
    private lateinit var currentDirectory: CurrentDirectory

    private lateinit var addItemButton: Button
    private lateinit var addCatButton: Button

    private lateinit var categoryText: TextView

    private lateinit var savedItem: Item
    private var cutData : InventoryData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory_directory_layout)

        inventoryManager = InventoryManager(applicationContext)
        currentDirectory = CurrentDirectory()
        inventoryListAdapter = setUpAdapter(inventoryManager)
        inventoryList = setUpInventoryList(findViewById<RecyclerView>(R.id.CategoryList), inventoryListAdapter)
        addItemButton = setUpAddItemButton(findViewById<Button>(R.id.CategoryAddItemButton))
        addCatButton = setUpAddCategoryButton(findViewById<Button>(R.id.CategoryAddCategoryButton))
        categoryText = setUpCategoryText(findViewById<TextView>(R.id.CategoryText))
    }

    fun deleteData(data : InventoryData) {
        when (data) {
            is Item -> inventoryManager.deleteItem(data)
            is Category -> inventoryManager.deleteCategory(data)
        }
        updateList(inventoryListAdapter)
    }

    fun setCutData(data : InventoryData) {
        cutData = data
    }

    fun getCutData() : InventoryData? {
        return if (cutData != null) cutData else null
    }

    fun setSavedItem(item: Item) {
        savedItem = item
    }

    fun getSavedItem() : Item {
        return savedItem
    }

    fun setUpCategoryText(catText: TextView) : TextView {
        catText.text = currentDirectory.pathToString()

        catText.setOnLongClickListener {
            val popup = PopupMenu(catText.context, catText)

            popup.apply {
                menuInflater.inflate(R.menu.paste_menu, menu)
            }


            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                    item ->
                when (item.itemId) {
                    R.id.paste -> {
                        val data = getCutData()
                        if (data != null){
                            when (data) {
                                is Item -> {
                                    inventoryManager.insertItem(Item(data.name, currentDirectory.pathToString(), data.quantity))
                                    inventoryManager.deleteItem(data)
                                    updateList(inventoryListAdapter)
                                }
                                is Category -> {
                                    inventoryManager.insertCategory(Category("${currentDirectory.pathToString()}/${data.getShortName()}", currentDirectory.pathToString()))
                                    inventoryManager.deleteCategory(data)
                                    Toast.makeText(this, "name: ${data.name} | parent: ${data.parent}", Toast.LENGTH_SHORT).show()
                                    updateList(inventoryListAdapter)
                                }
                            }
                        }
                        true
                    }
                    else -> false
                }
            })

            popup.show()
            true
        }
        return catText
    }

    fun setCategoryText(newText : String) {
        categoryText.text = newText
    }

    fun setUpAdapter(manager: InventoryManager) : DirectoryListAdapter{
        val listItems = manager.getEverythingInCategory(currentDirectory.pathToString())
        return DirectoryListAdapter(listItems, this)
    }

    fun setUpInventoryList(listView: RecyclerView, adapter: DirectoryListAdapter) : RecyclerView {
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter

        return listView
    }

    fun updateList(adapter: DirectoryListAdapter) {
        val updatedList = inventoryManager.getEverythingInCategory(currentDirectory.pathToString())
        adapter.updateList(updatedList)
    }

    fun setUpAddItemButton(addItemButton: Button): Button {
        addItemButton.setOnClickListener {
            val toOpenList = Intent(this, AddItemActivity::class.java)
            val from: FromWhere = FromWhere.FROMDIRECTORY
            toOpenList.putExtra("from", from)
            toOpenList.putExtra("current directory", currentDirectory.pathToString())
            startActivity(toOpenList)
        }
        return addItemButton
    }

    fun setUpAddCategoryButton(addCatButton: Button): Button {
        addCatButton.setOnClickListener {
            val toOpen = Intent(this, AddCategoryActivity::class.java)
            toOpen.putExtra("current directory", currentDirectory.pathToString())
            toOpen.putExtra("from", "directory_activity")
            startActivity(toOpen)
        }
        return addCatButton
    }

    override fun onResume() {
        super.onResume()
        updateList(inventoryListAdapter)
    }

    fun goToDir(subdir: String) {
        this.currentDirectory.addToPath(subdir)
        updateList(inventoryListAdapter)
        setCategoryText(currentDirectory.pathToString())
    }

    fun getCurrentDir() : String {
        return this.currentDirectory.pathToString()
    }
}