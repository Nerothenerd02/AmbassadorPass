package com.example.ambassadorpass

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import android.widget.Button

@UnstableApi
class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Set up the back button click listener
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // Navigate back to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close this activity
        }

        // Set up the "Login Admin" button click listener
        val loginAdminButton: Button = findViewById(R.id.loginAdminButton)
        loginAdminButton.setOnClickListener {
            // Navigate to LoginActivityAdmin
            val intent = Intent(this, LoginActivityAdmin::class.java)
            startActivity(intent)
        }


        // Set up the "Login Ambassador" button click listener
        val loginAmbassadorButton: Button = findViewById(R.id.loginAmbassadorButton)
        loginAmbassadorButton.setOnClickListener {
            val intent = Intent(this, LoginActivityAmbassador::class.java)
            startActivity(intent)
        }
    }
}
