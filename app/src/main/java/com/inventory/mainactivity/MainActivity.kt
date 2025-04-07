package com.inventory.mainactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.inventory.directoryactivity.DirectoryActivity

import com.inventory.listactivity.ListActivity
import com.inventory.R

//Description: Entrypoint for application. Displays main layout. Sets click handlers for buttons
//              in main layout.
//Resources: res/layout/main
class MainActivity : ComponentActivity() {

    private lateinit var listButton: Button
    private lateinit var directoryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //display main layout
        //setContentView(R.layout.main)
        setContentView(R.layout.main)

        //set up list button
        listButton = setUpListButton(findViewById<Button>(R.id.ListButton))

        //set up directory button
        directoryButton = setUpDirectoryButton(findViewById<Button>(R.id.DirectoryButton))
    }

    fun setUpListButton(listButton: Button) : Button {
        listButton.setOnClickListener {
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


}