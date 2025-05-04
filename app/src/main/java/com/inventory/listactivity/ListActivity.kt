package com.inventory.listactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SearchView
import android.widget.ViewSwitcher
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

    //UTILITIES
    private lateinit var manager: InventoryManager
    private lateinit var adapter: InventoryListAdapter

    //MASTER VIEW
    private lateinit var viewSwitcher: ViewSwitcher

    //INVENTORY LIST LAYOUT
    private lateinit var list : RecyclerView
    private lateinit var searchBar : SearchView
    private lateinit var addItemFromListButton : Button
    private lateinit var addFilterButton : Button

    //SEARCH FILTERS LAYOUT
    private lateinit var isPackedBox : CheckBox
    private lateinit var isNotPackedBox : CheckBox
    private lateinit var categoryFilterEntry : EditText
    private lateinit var categoryRecurseBox : CheckBox
    private lateinit var setFilterButton : Button

    //DATACLASS
    data class SearchFilters(
        var isPacked : Boolean = false,
        var isNotPacked : Boolean = false,
        var categoryEntry : String? = null,
        var categoryRecurse : Boolean = false
    )
    private lateinit var filters : SearchFilters

    //COMPONENT ACTIVITY OVERRIDES
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //display inventory_list_layout
        setContentView(R.layout.list_activity_view_switcher)

        //set up utilities
        manager = InventoryManager(applicationContext)
        adapter = setUpAdapter(manager)
        filters = SearchFilters()

        //set up master view
        viewSwitcher = findViewById<ViewSwitcher>(R.id.ListViewSwitcher)

        //set up list layout
        list = setUpInventoryList(findViewById<RecyclerView>(R.id.InventoryList), adapter)
        searchBar = setUpSearchBar(findViewById<SearchView>(R.id.ListActivitySearchBar), adapter, manager)
        addItemFromListButton = setUpAddItemButton(findViewById<Button>(R.id.AddItemListButton))
        addFilterButton = setUpAddFilterButton(findViewById<Button>(R.id.ListActivityAddFilterButton))

        //set up filters layout
        isPackedBox = setUpIsPackedBox(findViewById<CheckBox>(R.id.IsPackedBox))
        isNotPackedBox = setUpIsNotPackedBox(findViewById<CheckBox>(R.id.IsNotPackedBox))
        categoryRecurseBox = setUpCategoryRecurse(findViewById<CheckBox>(R.id.CategoryRecurseBox))
        categoryFilterEntry = findViewById<EditText>(R.id.ListSearchCategoryFilterEntry)
        setFilterButton = setUpSetFilterButton(findViewById<Button>(R.id.ListSearchSetFilterButton))
    }

    override fun onResume() {
        super.onResume()
        updateList(adapter, manager)
    }

    //DATA MANAGEMENT

    fun updateList(adapter: InventoryListAdapter, manager: InventoryManager) {
        val newItems = manager.getAllItems()
        adapter.updateItems(newItems)
    }


    //SET UP UTILITIES
    fun setUpAdapter(manager : InventoryManager) : InventoryListAdapter {
        val items = manager.getAllItems()
        return InventoryListAdapter(items)
    }

    //SET UP LIST LAYOUT

    fun setUpInventoryList(listView : RecyclerView, adapter : InventoryListAdapter) : RecyclerView {
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter
        return listView
    }



    fun setUpSearchBar(searchBar : SearchView, adapter: InventoryListAdapter, manager: InventoryManager) : SearchView {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    var catFilter : String
                    var results : List<Item>
                    if (filters.categoryEntry != null && filters.categoryEntry != "") {
                        catFilter = filters.categoryEntry.toString()
                        results = if (filters.categoryRecurse) {
                            manager.searchItemsByNameInCategoryRecursive(query, catFilter)
                        } else {
                            manager.searchItemsByNameInCategory(query, catFilter)
                        }
                    }
                    else {
                        results = manager.searchByName(query)
                    }

                    //set up results for filtering
                    var mutableResults = results.toMutableList()

                    //filter by isPacked
                    if (filters.isPacked) {
                        for (result in results) {
                            if (!result.isPacked) {
                                mutableResults.remove(result)
                            }
                        }
                    }
                    results = mutableResults.toList()

                    //filter by isNotPacked
                    if (filters.isNotPacked) {
                        for (result in results) {
                            if(result.isPacked) {
                                mutableResults.remove(result)
                            }
                        }
                    }
                    results = mutableResults.toList()

                    //update items
                    adapter.updateItems(results)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null && query == "") {
                    val results = manager.getAllItems()
                    adapter.updateItems(results)
                    return true
                }
                return false
            }
        })

        return searchBar
    }


    fun setUpAddFilterButton(addFilter : Button) : Button {
        addFilter.setOnClickListener {
            viewSwitcher.showNext()
        }
        return addFilter
    }


    fun setUpAddItemButton(addItem : Button) : Button {
        addItem.setOnClickListener {
            val toOpenList = Intent(this, AddItemActivity::class.java)
            toOpenList.putExtra("from", "list activity")
            toOpenList.putExtra("current directory", "/")
            startActivity(toOpenList)
        }
        return addItem
    }

    //SET UP FILTERS LAYOUT

    fun setUpIsPackedBox(isPackedBox : CheckBox) : CheckBox {
        isPackedBox.setOnClickListener {
            filters.isPacked = !filters.isPacked
        }
        return isPackedBox
    }



    fun setUpIsNotPackedBox(isNotPackedBox : CheckBox) : CheckBox {
        isNotPackedBox.setOnClickListener {
            filters.isNotPacked = !filters.isNotPacked
        }
        return isNotPackedBox
    }


    fun setUpCategoryRecurse(categoryRecurseBox : CheckBox) : CheckBox {
        categoryRecurseBox.setOnClickListener {
            filters.categoryRecurse = !filters.categoryRecurse
        }
        return categoryRecurseBox
    }



    fun setUpSetFilterButton(setFilter : Button) : Button {
        setFilter.setOnClickListener {
            filters.categoryEntry = categoryFilterEntry.text.toString()
            viewSwitcher.showPrevious()
        }
        return setFilter
    }


}