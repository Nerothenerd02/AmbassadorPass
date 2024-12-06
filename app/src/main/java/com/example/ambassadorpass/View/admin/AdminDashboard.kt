package com.example.ambassadorpass.view.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.ViewPartiesRepository
import com.example.ambassadorpass.utils.HorizontalSpaceItemDecoration

class AdminDashboard : AppCompatActivity() {

    private lateinit var recentPartiesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admindashboard)

        // Initialize the RecyclerView
        recentPartiesRecyclerView = findViewById(R.id.recentPartiesRecyclerView)

        // Set up horizontal RecyclerView with spacing
        recentPartiesRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Add spacing between items in RecyclerView
        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.recycler_item_spacing)
        recentPartiesRecyclerView.addItemDecoration(HorizontalSpaceItemDecoration(horizontalSpacing))

        // Fetch and display recent parties
        val repository = ViewPartiesRepository()
        repository.getParties().observe(this) { parties ->
            val adapter = RecentPartiesAdapter(parties)
            recentPartiesRecyclerView.adapter = adapter
        }

        // Set up the back button functionality
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish() // Go back to the previous activity
        }

        // Set up the create party button functionality
        findViewById<Button>(R.id.createPartyButton).setOnClickListener {
            startActivity(Intent(this, CreatePartyActivity::class.java))
        }

        // Set up the assign ambassadors button functionality
        findViewById<Button>(R.id.assignAmbassadorsButton).setOnClickListener {
            startActivity(Intent(this, AssignAmbassadorsActivity::class.java))
        }

        // Set up the create admin button functionality
        findViewById<Button>(R.id.createAdmin).setOnClickListener {
            startActivity(Intent(this, CreateAdminActivity::class.java))
        }

        // Set up the view parties button functionality
        findViewById<Button>(R.id.viewPartiesButton).setOnClickListener {
            startActivity(Intent(this, ViewParties::class.java))
        }
    }
}
