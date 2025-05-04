package com.inventory.mainactivity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.graphics.painter.Painter
import com.inventory.directoryactivity.DirectoryActivity

import com.inventory.listactivity.ListActivity
import com.inventory.R
import com.inventory.inventorymanager.InventoryManager
import com.inventory.testactivity.TestActivity

//Description: Entrypoint for application. Displays main layout. Sets click handlers for buttons
//              in main layout.
//Resources: res/layout/main
class MainActivity : ComponentActivity() {

    private lateinit var listButton: Button
    private lateinit var directoryButton: Button
    private lateinit var clearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //display main layout
        setContentView(R.layout.main)
        //setContentView(R.layout.inventory_directory_layout)

        /*findViewById<Button>(R.id.ListButton).setOnClickListener {
            val toOpenList = Intent(this, ListActivity::class.java)
            startActivity(toOpenList)
        }*/

        //set up list button
        listButton = setUpListButton(findViewById<Button>(R.id.ListButton))

        //set up directory button
        directoryButton = setUpDirectoryButton(findViewById<Button>(R.id.DirectoryButton))

        clearButton = setUpClearButton(findViewById<Button>(R.id.ClearButton))
    }

    fun setUpListButton(listButton: Button) : Button {
        listButton.setOnClickListener {
            //val toOpenList = Intent(this, ListActivity::class.java)
            val toOpenList = Intent(this, ListActivity::class.java)
            startActivity(toOpenList)
        }
        return listButton
    }

    fun setUpDirectoryButton(directoryButton: Button) : Button {
        directoryButton.setOnClickListener {
            val toOpenDirectory = Intent(this, DirectoryActivity::class.java)
            startActivity(toOpenDirectory)
        }
        return directoryButton
    }

    fun setUpClearButton(clearButton : Button) : Button {
        clearButton.setOnClickListener {
            val manager = InventoryManager(applicationContext)
            manager.DELETEALLITEMS()
            manager.DELETEALLCATEGORIES()
        }
        return clearButton
    }
}