package com.example.ambassadorpass.view.ambassador

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.AmbassadorRepository
import com.example.ambassadorpass.viewmodel.AmbassadorViewModel
import com.example.ambassadorpass.viewmodel.AmbassadorViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class AmbassadorDashboard : AppCompatActivity() {

    private val viewModel: AmbassadorViewModel by viewModels {
        AmbassadorViewModelFactory(AmbassadorRepository()) // Pass the repository instance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambassadordashboard)

        // Reference UI components
        val backButton: ImageButton = findViewById(R.id.backButton)
        val ambassadorNamesTextView: TextView = findViewById(R.id.ambassadorNamesTextView)
        val listView: ListView = findViewById(R.id.listview)

        // Back button functionality
        backButton.setOnClickListener {
            finish() // Close this activity and return to the previous screen
        }

        // Fetch currently logged-in user's UID
        val currentUser = FirebaseAuth.getInstance().currentUser
        val ambassadorId = currentUser?.uid

        if (ambassadorId != null) {
            // Display ambassador's name
            viewModel.getAmbassadorName(ambassadorId).observe(this, Observer { name ->
                ambassadorNamesTextView.text = name.joinToString(separator = "\n")
            })

            // Fetch and display parties for the ambassador
            viewModel.getPartiesForAmbassador(ambassadorId).observe(this, Observer { parties ->
                if (parties.isNotEmpty()) {
                    val adapter = PartyAdapter(this, parties)
                    listView.adapter = adapter
                } else {
                    Toast.makeText(this, "No active parties found for this ambassador.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            ambassadorNamesTextView.text = "User Not Logged In"
            Toast.makeText(this, "Please log in to view your dashboard.", Toast.LENGTH_SHORT).show()
        }
    }
}
