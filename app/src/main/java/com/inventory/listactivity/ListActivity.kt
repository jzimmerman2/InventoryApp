package com.inventory.listactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inventory.inventorymanager.InventoryManager

import com.inventory.R
import com.inventory.additemactivity.AddItemActivity
import com.inventory.inventorymanager.data.Item


//Description: Displays inventory_list_layout layout. Implements recyclerview (dynamic list) for
//      displaying inventory items. Implements search bar to search for items in inventory.
//Resources: res/layout/inventory_list_layout
class ListActivity : ComponentActivity() {

    private lateinit var inventoryManager: InventoryManager
    private lateinit var inventoryListAdapter: InventoryListAdapter
    private lateinit var inventoryList : RecyclerView
    private lateinit var searchBar : SearchView
    private lateinit var addItemFromListButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //display inventory_list_layout
        setContentView(R.layout.inventory_list_layout)

        inventoryManager = InventoryManager(applicationContext)

        inventoryListAdapter = setUpAdapter(inventoryManager)
        inventoryList = setUpInventoryList(findViewById<RecyclerView>(R.id.InventoryList),
            inventoryListAdapter)

        addItemFromListButton = setUpAddItemListButton(findViewById<Button>(R.id.AddItemListButton))

        searchBar = setUpSearchBar(findViewById<SearchView>(R.id.SearchBar), inventoryListAdapter,
            inventoryManager)
    }

    override fun onResume() {
        super.onResume()
        inventoryListAdapter = updateInventoryList(inventoryListAdapter, inventoryManager)
    }

    fun setUpAddItemListButton(addItemListButton: Button) : Button {
        addItemListButton.setOnClickListener {
            val toOpenList = Intent(this, AddItemActivity::class.java)
            toOpenList.putExtra("from", "list activity")
            toOpenList.putExtra("current directory", "/")
            startActivity(toOpenList)
        }
        return addItemListButton
    }

    fun setUpSearchBar(searchBar: SearchView, inventoryListAdapter: InventoryListAdapter, manager : InventoryManager) : SearchView {

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val results = manager.searchByName(query)
                    inventoryListAdapter.updateItems(results)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })

        return searchBar
    }

    fun updateInventoryList(inventoryListAdapter: InventoryListAdapter, manager: InventoryManager) : InventoryListAdapter {
        val newItems = manager.getAllItems()
        inventoryListAdapter.updateItems(newItems)
        return inventoryListAdapter
    }

    fun setUpAdapter(manager: InventoryManager) : InventoryListAdapter {
        val items = manager.getAllItems()
        return InventoryListAdapter(items)
    }

    fun setUpInventoryList(inventoryList : RecyclerView, inventoryListAdapter: InventoryListAdapter) : RecyclerView {
        inventoryList.layoutManager = LinearLayoutManager(this)
        inventoryList.adapter = inventoryListAdapter
        return inventoryList
    }
}