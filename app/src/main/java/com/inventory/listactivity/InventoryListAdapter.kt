package com.inventory.listactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inventory.inventorymanager.database.InventoryDb
import com.inventory.inventorymanager.data.Item
import com.inventory.R
import com.inventory.inventorymanager.InventoryManager

//Description: Adapter for dynamic inventory list.
class InventoryListAdapter (private var items: List<Item>) :
    RecyclerView.Adapter<InventoryListAdapter.ItemViewHolder>() {

    //generates viewholders
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryListAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ItemViewHolder(view)
    }

    //Binds viewholder to data in itemlist
    override fun onBindViewHolder(holder: InventoryListAdapter.ItemViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.itemCategory.text = item.category
        holder.itemQuantity.text = item.quantity.toString()
        holder.isPacked.isChecked = item.isPacked
    }

    //returns size of itemlist
    override fun getItemCount(): Int {
        return items.size
    }

    //Class to hold the item view
    class ItemViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {
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


    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }

}