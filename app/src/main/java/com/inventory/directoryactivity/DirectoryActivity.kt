package com.inventory.directoryactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inventory_directory_layout)

        inventoryManager = InventoryManager(applicationContext)
        currentDirectory = CurrentDirectory()
        inventoryListAdapter = setUpAdapter(inventoryManager)
        inventoryList = setUpInventoryList(findViewById<RecyclerView>(R.id.CategoryList), inventoryListAdapter)
        addItemButton = setUpAddItemButton(findViewById<Button>(R.id.CategoryAddItemButton))
        addCatButton = setUpAddCategoryButton(findViewById<Button>(R.id.CategoryAddCategoryButton))
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
    }

    fun getCurrentDir() : String {
        return this.currentDirectory.pathToString()
    }
}