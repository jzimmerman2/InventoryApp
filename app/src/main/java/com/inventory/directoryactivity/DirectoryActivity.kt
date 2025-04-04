package com.inventory.directoryactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inventory.R
import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Item
import com.inventory.listactivity.InventoryListAdapter

class DirectoryActivity : ComponentActivity() {

    private lateinit var inventoryManager: InventoryManager
    private lateinit var inventoryListAdapter: DirectoryListAdapter
    private lateinit var inventoryList : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inventory_directory_layout)

        inventoryManager = InventoryManager(applicationContext)

        var itemList: ArrayList<DirectoryListViewType> = arrayListOf()
        for (i in 1..3) {
            val newDir = DirectoryListViewType.DirectoryListDirectory("category $i")
            itemList.add(newDir)
        }
        //var itemList: ArrayList<Item> = arrayListOf()
        for (i in 1..10) {
            val newItem = DirectoryListViewType.DirectoryListItem("item $i", "category", i)
            //val newItem = Item("item $i", "category", i)
            itemList.add(newItem)
        }

        inventoryListAdapter = DirectoryListAdapter(itemList)
        inventoryList = findViewById<RecyclerView>(R.id.CategoryList)
        inventoryList.layoutManager = LinearLayoutManager(this)
        inventoryList.adapter = inventoryListAdapter
    }
}