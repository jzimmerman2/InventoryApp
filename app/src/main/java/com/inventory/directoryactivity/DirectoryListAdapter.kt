package com.inventory.directoryactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.CheckBox
import android.widget.ImageView
import com.inventory.R
import com.inventory.directoryactivity.DirectoryListViewType.DirectoryListItem
import com.inventory.inventorymanager.InventoryManager
import com.inventory.inventorymanager.data.Item
import com.inventory.listactivity.InventoryListAdapter

class DirectoryListAdapter(private var listItems: List<DirectoryListViewType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_ITEM = 1
    val VIEW_TYPE_DIRECTORY = 2

    override fun getItemViewType(position: Int): Int {
        if (listItems[position] is DirectoryListItem) {
            return VIEW_TYPE_ITEM
        }
        return VIEW_TYPE_DIRECTORY
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            return DirectoryListItemViewHolder(
                LayoutInflater.from(
                    parent.context
                ).inflate(R.layout.item, parent, false)
            )
        }
        return DirectoryListDirectoryViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.directory, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listItems[position]
        if (holder is DirectoryListItemViewHolder && item is DirectoryListViewType.DirectoryListItem) {
            holder.itemName.text = item.name
            holder.itemCategory.text = item.category
            holder.itemQuantity.text = item.quantity.toString()
            holder.isPacked.isChecked = false
        }
        if (holder is DirectoryListDirectoryViewHolder && item is DirectoryListViewType.DirectoryListDirectory) {
            holder.directoryName.text = item.name
            holder.directoryImage.setImageResource(R.drawable.code_64x64)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    internal inner class DirectoryListItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

    internal inner class DirectoryListDirectoryViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

            val directoryImage: ImageView = itemView.findViewById(R.id.DirectoryImage)
            val directoryName: TextView = itemView.findViewById(R.id.DirectoryName)
    }
}
