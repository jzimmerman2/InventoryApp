package com.inventory.listactivity

import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Item

class ItemSearchFilter(private val manager: InventoryManager) {

    data class SearchFilters(
        private var isPacked : Boolean = false,
        private var isNotPacked : Boolean = false,
        private var categoryEntry : String? = null,
        private var categoryRecurse : Boolean = false
    ) {
        fun getIsPacked() = isPacked
        fun getIsNotPacked() = isNotPacked
        fun getCategoryEntry() = categoryEntry
        fun getCategoryRecurse() = categoryRecurse
        fun setIsPacked(boolean: Boolean) {isPacked = boolean}
        fun setIsNotPacked(boolean: Boolean) {isNotPacked = boolean}
        fun setCategoryEntry(catEntry : String?) {categoryEntry = catEntry}
        fun setCategoryRecurse(boolean: Boolean) {categoryRecurse = boolean}
    }
    var filters : SearchFilters = SearchFilters()

    fun resultsNoCategoryFilter() : List<Item> {
        return filterResultsByIsPacked(filterResultsByIsNotPacked(manager.getAllItems()))
    }

    fun noQueryAllFilters() : List<Item> {
        var catFilter : String
        var results : List<Item>
        val catEntry : String? = filters.getCategoryEntry()

        if (catEntry != null && catEntry != "") {
            catFilter = catEntry.toString()
            results = if (filters.getCategoryRecurse()) {
                manager.searchItemsByCategoryRecursive(catFilter)
            }
            else {
                manager.searchItemsByCategoryNonRecursive(catFilter)
            }
        }
        else {
            results = resultsNoCategoryFilter()
        }
        return results
    }

    fun filterQueryByCategory(query : String) : List<Item> {
        var catFilter: String
        var results: List<Item>
        val catEntry : String? = filters.getCategoryEntry()

        if (catEntry != null && catEntry != "") {
            catFilter = catEntry.toString()
            results = if (filters.getCategoryRecurse()) {
                manager.searchItemsByNameInCategoryRecursive(query, catFilter)
            } else {
                manager.searchItemsByNameInCategory(query, catFilter)
            }
        } else {
            results = resultsNoCategoryFilter()
        }
        return results
    }

    fun filterResultsByIsPacked(unfiltered : List<Item>) : List<Item> {
        //set up results for filtering
        var mutableResults = unfiltered.toMutableList()

        //filter by isPacked
        if (filters.getIsPacked()) {
            for (item in unfiltered) {
                if (!item.isPacked) {
                    mutableResults.remove(item)
                }
            }
        }
        val results = mutableResults.toList()
        return results
    }

    fun filterResultsByIsNotPacked(unfiltered : List<Item>) : List<Item> {
        //set up results for filtering
        var mutableResults = unfiltered.toMutableList()

        //filter by isPacked
        if (filters.getIsNotPacked()) {
            for (item in unfiltered) {
                if (item.isPacked) {
                    mutableResults.remove(item)
                }
            }
        }
        val results = mutableResults.toList()
        return results
    }
}