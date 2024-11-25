package com.example.ambassadorpass.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.ambassadorpass.R

class PageOneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_one)

        // Initialize the back button
        val backButton: ImageButton = findViewById(R.id.backButton)

        // Set a click listener to finish the activity
        backButton.setOnClickListener {
            finish()
        }
    }
}
