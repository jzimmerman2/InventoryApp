package com.inventory.testactivity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ViewSwitcher
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.inventory.R

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_test)

        val viewSwitcher : ViewSwitcher = findViewById<ViewSwitcher>(R.id.ListViewSwitcher)
        val firstView = findViewById<LinearLayout>(R.id.test1_layout)
        val secondView = findViewById<LinearLayout>(R.id.test2_layout)

        val addItemButton : Button = findViewById<Button>(R.id.testAddItemListButton)
        addItemButton.setOnClickListener {
            if (viewSwitcher.currentView != secondView) {
                viewSwitcher.showNext()
            }
            else {
                viewSwitcher.showPrevious()
            }
        }
    }
}