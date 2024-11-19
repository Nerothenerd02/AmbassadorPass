package com.example.ambassadorpass.view.admin

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.CreatePartyRepository
import com.example.ambassadorpass.viewmodel.CreatePartyViewModel
import com.example.ambassadorpass.viewmodel.CreatePartyViewModelFactory

class PhotoPreviewActivity : AppCompatActivity() {

    private lateinit var viewModel: CreatePartyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_preview)

        // Set up the Repository
        val repository = CreatePartyRepository()

        // Set up the ViewModel using the custom Factory
        val factory = CreatePartyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CreatePartyViewModel::class.java]

        val photo1: ImageView = findViewById(R.id.photo1)
        val photo2: ImageView = findViewById(R.id.photo2)
        val photo3: ImageView = findViewById(R.id.photo3)

        // Display selected photos
        val images = viewModel.selectedImages
        if (images.isNotEmpty()) {
            photo1.setImageURI(images[0])
        }
        if (images.size > 1) {
            photo2.setImageURI(images[1])
        }
        if (images.size > 2) {
            photo3.setImageURI(images[2])
        }

        // Set up Create Party button
        val createPartyButton: Button = findViewById(R.id.createPartyButton)
        createPartyButton.setOnClickListener {
            viewModel.createParty()
            viewModel.partyCreationStatus.observe(this) { success ->
                if (success) {
                    Toast.makeText(this, "Party created successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Finish after creating party
                } else {
                    Toast.makeText(this, "Failed to create party", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
