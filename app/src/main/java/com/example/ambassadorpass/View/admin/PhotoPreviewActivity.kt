package com.example.ambassadorpass.view.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.CreatePartyRepository
import com.example.ambassadorpass.viewmodel.CreatePartyViewModel
import com.example.ambassadorpass.viewmodel.CreatePartyViewModelFactory

class PhotoPreviewActivity : AppCompatActivity() {

    private lateinit var viewModel: CreatePartyViewModel
    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var progressBar: ProgressBar

    // Data structure to store one URI per ImageView (i.e., one image per box)
    private val imagesMap = mutableMapOf<Int, Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_preview)

        // Set up the Repository
        val repository = CreatePartyRepository()

        // Set up the ViewModel using the custom Factory
        val factory = CreatePartyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CreatePartyViewModel::class.java]

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

        val photo1: ImageView = findViewById(R.id.photo1)
        val photo2: ImageView = findViewById(R.id.photo2)
        val photo3: ImageView = findViewById(R.id.photo3)
        val photo4: ImageView = findViewById(R.id.photo4)

        // Initialize the photo picker launcher
        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data!!
                val selectedUri = if (data.clipData != null) {
                    data.clipData!!.getItemAt(0).uri
                } else {
                    data.data!!
                }

                // Set the URI for the clicked ImageView
                val selectedImageViewId = viewModel.selectedImageViewId
                if (selectedImageViewId != null) {
                    imagesMap[selectedImageViewId] = selectedUri
                    updateImageView(selectedImageViewId)

                    // Store the selected image in the ViewModel
                    viewModel.updateSelectedImage(selectedImageViewId, selectedUri)
                }
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up image click listeners to open the photo picker
        val imageClickListener = { imageViewId: Int ->
            // Store which ImageView is being updated
            viewModel.selectedImageViewId = imageViewId
            // Intent to open gallery to pick an image
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            photoPickerLauncher.launch(Intent.createChooser(intent, "Select Picture"))
        }

        // Assign listeners to image views
        photo1.setOnClickListener { imageClickListener(R.id.photo1) }
        photo2.setOnClickListener { imageClickListener(R.id.photo2) }
        photo3.setOnClickListener { imageClickListener(R.id.photo3) }
        photo4.setOnClickListener { imageClickListener(R.id.photo4) }

        // Retrieve data from Intent extras
        viewModel.partyName = intent.getStringExtra("partyName")
        viewModel.partyDate = intent.getStringExtra("partyDate")
        viewModel.partyDescription = intent.getStringExtra("partyDescription")
        viewModel.partyLocation = intent.getStringExtra("partyLocation")
        viewModel.ticketsAvailable = intent.getIntExtra("ticketsAvailable", 0)
        viewModel.ticketPrice = intent.getDoubleExtra("ticketPrice", 0.0)
        viewModel.ambassadorMarkup = intent.getDoubleExtra("ambassadorMarkup", 0.0)

        // Set up Create Party button
        val createPartyButton: Button = findViewById(R.id.createPartyButton)
        createPartyButton.setOnClickListener {
            Log.d("PhotoPreviewActivity", "Create Party button clicked.")
            progressBar.visibility = View.VISIBLE

            viewModel.createParty()
            viewModel.partyCreationStatus.observe(this) { success ->
                progressBar.visibility = View.GONE
                if (success) {
                    Toast.makeText(this, "Party created successfully!", Toast.LENGTH_SHORT).show()
                    // Navigate back to Admin Dashboard after creating the party successfully
                    val intent = Intent(this, AdminDashboard::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to create party", Toast.LENGTH_SHORT).show()
                    Log.e("PhotoPreviewActivity", "Failed to create party.")
                }
            }
        }

        // Display previously selected images if any
        displaySelectedImages()
    }

    private fun displaySelectedImages() {
        viewModel.selectedImages.forEach { (imageViewId, uri) ->
            imagesMap[imageViewId] = uri
            updateImageView(imageViewId)
        }
    }

    private fun updateImageView(imageViewId: Int) {
        val imageView: ImageView = findViewById(imageViewId)
        val currentUri = imagesMap[imageViewId]

        if (currentUri != null) {
            imageView.setImageURI(currentUri)
        }
    }
}
