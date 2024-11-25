package com.example.ambassadorpass.view.user

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.ambassadorpass.R

class PartyPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_preview)

        // Initialize the back button and set its click listener
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}
