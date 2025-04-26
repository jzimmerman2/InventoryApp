package com.inventory.directoryactivity

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inventory.R
import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Category
import com.inventory.inventorymanager.data.InventoryData
import com.inventory.inventorymanager.data.Item
import com.inventory.listactivity.InventoryListAdapter

class DirectoryListAdapter (private var inventoryItems: List<InventoryData>,
    private val owner: DirectoryActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        inventoryItems = formatList(inventoryItems)
    }

    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_CATEGORY = 1
    val VIEW_TYPE_SUPERCATEGORY = 2

    override fun getItemViewType(position: Int): Int {
        return when (inventoryItems[position]) {
            is Item -> VIEW_TYPE_ITEM
            is Category -> VIEW_TYPE_CATEGORY
            is SuperCategory -> VIEW_TYPE_SUPERCATEGORY
            else -> throw error("bad item")
        }
    }

    override fun getItemCount(): Int {
        return inventoryItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                ItemViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item, parent, false))
            }
            VIEW_TYPE_CATEGORY -> {
                CategoryViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.category, parent, false))
            }
            VIEW_TYPE_SUPERCATEGORY -> {
                SuperCategoryViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.category, parent, false))
            }
            else -> throw error("bad item")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val inventoryItem = inventoryItems[position]

        if (holder is ItemViewHolder && inventoryItem is Item) {
            holder.itemName.text = inventoryItem.name
            holder.itemCategory.text = inventoryItem.category
            holder.itemQuantity.text = inventoryItem.quantity.toString()
            holder.isPacked.isChecked = inventoryItem.isPacked
        }
        if (holder is CategoryViewHolder && inventoryItem is Category) {
            holder.categoryName.text = inventoryItem.getShortName()
            holder.categoryImage.setImageResource(R.drawable.folder)
        }
        if (holder is SuperCategoryViewHolder && inventoryItem is SuperCategory) {
            holder.name.text = ".."
            holder.image.setImageResource(R.drawable.folder)
        }
    }

    fun updateList(updatedData: List<InventoryData>) {
        inventoryItems = formatList(updatedData)
        notifyDataSetChanged()
    }

    fun formatList(unformatted: List<InventoryData>) : List<InventoryData> {
        return addSuperDir(sortList(unformatted), getCurrentDir())
    }

    fun sortList(unsorted: List<InventoryData>) : List<InventoryData> {
        var directories: MutableList<Category> = mutableListOf()
        var items: MutableList<Item> = mutableListOf()

        for (inventoryData in unsorted) {
            if (inventoryData is Category) {
                directories.add(inventoryData)
            }
            else if (inventoryData is Item) {
                items.add(inventoryData)
            }
        }

        directories.sortBy { it.name }
        items.sortBy { it.name }

        return directories + items
    }

    fun addSuperDir(categoryList: List<InventoryData>, currentDirectory: String) : List<InventoryData> {
        if (currentDirectory == "/" || categoryList.any { it is SuperCategory }) return categoryList

        val superDir: List<InventoryData> = listOf(SuperCategory())
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

    internal inner class CategoryViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val categoryImage: ImageView = itemView.findViewById(R.id.CategoryImage)
        val categoryName: TextView = itemView.findViewById(R.id.CategoryName)

        init {
            categoryImage.setOnClickListener {
                goToDir(categoryName.text.toString())
            }
        }
    }

    internal inner class SuperCategory(private val name: String = "..") : InventoryData() {
        fun getName() : String {
            return name
        }
    }

    internal inner class SuperCategoryViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val image : ImageView = itemView.findViewById(R.id.CategoryImage)
        val name : TextView = itemView.findViewById(R.id.CategoryName)

        init {
            image.setOnClickListener {
                goToDir("..")
            }
        }
    }
}