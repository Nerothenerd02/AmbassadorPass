package com.example.ambassadorpass.view.admin

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.ambassadorpass.R
import com.example.ambassadorpass.viewmodel.AssignAmbassadorViewModel
import com.example.ambassadorpass.viewmodel.AssignAmbassadorViewModelFactory
import com.example.ambassadorpass.adapter.PartyAdapter

class AssignAmbassadorsActivity : AppCompatActivity() {

    // Create an instance of ViewModelFactory
    private val factory = AssignAmbassadorViewModelFactory(this)
    private val viewModel: AssignAmbassadorViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_ambassadors)

        // Reference the back button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // Navigate back to the previous screen
            finish()
        }

        // Reference the ListView and set up the adapter
        val listView: ListView = findViewById(R.id.listview)
        val adapter = PartyAdapter(this, mutableListOf(), viewModel)
        listView.adapter = adapter

        // Observe changes in the party list and update the adapter
        viewModel.partyList.observe(this, Observer { parties ->
            adapter.updateData(parties)
        })

        // Fetch parties from the ViewModel
        viewModel.fetchParties()
    }
}
