package com.example.ambassadorpass.view.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.ambassadorpass.R

class AdminDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admindashboard)

        // Set up the back button functionality
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

        // Set up the create party button functionality
        val createPartyButton: Button = findViewById(R.id.createPartyButton)
        createPartyButton.setOnClickListener {
            val intent = Intent(this, CreatePartyActivity::class.java)
            startActivity(intent)
        }

        // Set up the assign ambassadors button functionality (optional)
        val assignAmbassadorsButton: Button = findViewById(R.id.assignAmbassadorsButton)
        assignAmbassadorsButton.setOnClickListener {
            val intent = Intent(this, AssignAmbassadorsActivity::class.java)
            startActivity(intent)
        }

        // Set up the create admin button functionality (optional)
        val createAdminButton: Button = findViewById(R.id.createAdmin)
        createAdminButton.setOnClickListener {
            val intent = Intent(this, CreateAdminActivity::class.java)
            startActivity(intent)
        }
    }
}
