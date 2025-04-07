package com.inventory.directoryactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.CheckBox
import android.widget.ImageView
import com.inventory.R
import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Category
import com.inventory.inventorymanager.data.InventoryData
import com.inventory.inventorymanager.data.Item
import com.inventory.listactivity.InventoryListAdapter

class DirectoryListAdapter(private var listItems: List<InventoryData>,
    private val owner: DirectoryActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_ITEM = 1
    val VIEW_TYPE_CATEGORY = 2
    val VIEW_TYPE_SUPERDIRECTORY = 3

    init {
        listItems = formatList(listItems)
    }

    override fun getItemViewType(position: Int): Int {
        if (listItems[position] is Item) {
            return VIEW_TYPE_ITEM
        }
        if (listItems[position] is Category) {
            return VIEW_TYPE_CATEGORY
        }
        return VIEW_TYPE_SUPERDIRECTORY
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            return ItemViewHolder(
                LayoutInflater.from(
                    parent.context
                ).inflate(R.layout.item, parent, false)
            )
        }
        return DirectoryViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.directory, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listItem = listItems[position]
        if (holder is ItemViewHolder && listItem is Item) {
            holder.itemName.text = listItem.name
            holder.itemCategory.text = listItem.category
            holder.itemQuantity.text = listItem.quantity.toString()
            holder.isPacked.isChecked = listItem.isPacked
        }
        if (holder is DirectoryViewHolder && listItem is Category) {
            holder.directoryName.text = listItem.category_name
            holder.directoryImage.setImageResource(R.drawable.code_64x64)
        }
        if (holder is DirectoryViewHolder && listItem is SuperDirectory){
            holder.directoryName.text = listItem.getName()
            holder.directoryImage.setImageResource(R.drawable.code_64x64)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun updateList(updatedData: List<InventoryData>) {
        listItems = formatList(updatedData)
        notifyDataSetChanged()
    }

    fun formatList(unformatted: List<InventoryData>) : List<InventoryData> {
        return addSuperDir(sortList(unformatted), getCurrentDir())
    }

    fun sortList(unsorted: List<InventoryData>) : List<InventoryData> {
        var directories: MutableList<Category> = mutableListOf()
        var items: MutableList<Item> = mutableListOf()

        for (thing in unsorted) {
            if (thing is Category) {
                directories.add(thing)
            }
            else if (thing is Item) {
                items.add(thing)
            }
        }

        directories.sortBy {it.category_name}
        items.sortBy {it.name}

        return directories + items
    }

    fun addSuperDir(categoryList: List<InventoryData>, currentDirectory: String) : List<InventoryData> {
        if (currentDirectory == "/" || categoryList.any { it is SuperDirectory }) return categoryList

        val superDir: List<InventoryData> = listOf(SuperDirectory())
        return superDir + categoryList
    }

    fun getCurrentDir() : String {
        return owner.getCurrentDir()
    }

    fun goToDir(newDir: String) {
        owner.goToDir(newDir)
    }

    internal inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val itemName: TextView = itemView.findViewById(R.id.ItemName)
            val itemCategory: TextView = itemView.findViewById(R.id.ItemCategory)
            val itemQuantity: TextView = itemView.findViewById(R.id.ItemQuantity)
            val isPacked: CheckBox = itemView.findViewById<CheckBox>(R.id.isPackedBox)
            val divider: View = itemView.findViewById(R.id.ItemDivider)
            init {
                isPacked.setOnClickListener {
                    InventoryManager(itemView.context.applicationContext)
                        .flipIsPacked(itemName.text.toString(), itemCategory.text.toString())
                }
            }

        }

    internal inner class DirectoryViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

            val directoryImage: ImageView = itemView.findViewById(R.id.DirectoryImage)
            val directoryName: TextView = itemView.findViewById(R.id.DirectoryName)

            init {
                directoryImage.setOnClickListener {
                    goToDir(directoryName.text.toString())
                }
            }
    }

    internal inner class SuperDirectory(private val name: String = "..") : InventoryData() {
        fun getName() : String {
            return name
        }
    }
}
