package com.example.ambassadorpass.view.ambassador

import android.os.Bundle
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
        val ambassadorNamesTextView: TextView = findViewById(R.id.ambassadorNamesTextView)
        val listView: ListView = findViewById(R.id.listview)

        // Fetch currently logged-in user's UID
        val currentUser = FirebaseAuth.getInstance().currentUser
        val ambassadorId = currentUser?.uid

        // Display ambassador's name based on their ID
        if (ambassadorId != null) {
            viewModel.getAmbassadorName(ambassadorId).observe(this, Observer { name ->
                ambassadorNamesTextView.text = name.joinToString(separator = "\n") // Convert list to string
            })
        } else {
            ambassadorNamesTextView.text = "User Not Logged In"
            Toast.makeText(this, "Please log in to view your dashboard.", Toast.LENGTH_SHORT).show()
        }

        // Populate Party List
        viewModel.getParties().observe(this, Observer { parties ->
            val adapter = PartyAdapter(this, parties)
            listView.adapter = adapter

            // Handle party selection to display associated ambassadors
            listView.setOnItemClickListener { _, _, position, _ ->
                val selectedParty = parties[position]
                val partyId = selectedParty.partyId

                viewModel.getAmbassadorName(partyId).observe(this, Observer { names ->
                    ambassadorNamesTextView.text = names.joinToString(separator = "\n") // Convert list to string
                })
            }
        })
    }
}
