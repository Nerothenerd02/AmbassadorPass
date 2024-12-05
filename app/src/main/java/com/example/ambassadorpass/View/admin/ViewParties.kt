package com.example.ambassadorpass.view.admin

import android.os.Bundle
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.ViewPartiesRepository
import com.example.ambassadorpass.viewmodel.ViewPartiesViewModel
import com.example.ambassadorpass.viewmodel.ViewPartiesViewModelFactory

class ViewParties : AppCompatActivity() {

    private val viewModel: ViewPartiesViewModel by viewModels {
        ViewPartiesViewModelFactory(ViewPartiesRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_parties)

        // Reference the back button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // Navigate back to the previous screen
            finish()
        }

        // Reference the ListView from the layout
        val listView: ListView = findViewById(R.id.partiesListView)

        // Observe the parties data and set up the adapter
        viewModel.getParties().observe(this) { parties ->
            val adapter = ViewPartiesAdapter(this, parties, viewModel)
            listView.adapter = adapter
        }
    }
}
