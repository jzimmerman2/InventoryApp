package com.inventory.directoryactivity

open class DirectoryListViewType {
    class DirectoryListItem(val name: String, val category: String, val quantity: Int) : DirectoryListViewType()
    class DirectoryListDirectory(val name: String) : DirectoryListViewType()
}