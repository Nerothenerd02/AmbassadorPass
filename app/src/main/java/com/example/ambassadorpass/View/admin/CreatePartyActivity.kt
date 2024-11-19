package com.example.ambassadorpass.view.admin

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.CreatePartyRepository
import com.example.ambassadorpass.viewmodel.CreatePartyViewModel
import com.example.ambassadorpass.viewmodel.CreatePartyViewModelFactory
import java.util.Calendar

class CreatePartyActivity : AppCompatActivity() {

    private lateinit var viewModel: CreatePartyViewModel
    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_party)

        // Set up the Repository
        val repository = CreatePartyRepository()

        // Set up the ViewModel using the custom Factory
        val factory = CreatePartyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CreatePartyViewModel::class.java]

        // Set up the back button functionality
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

        // Initialize UI elements
        val selectPhotosButton: Button = findViewById(R.id.selectPhotosButton)
        val partyNameEditText: TextInputEditText = findViewById(R.id.partyNameEditText)
        val partyDateEditText: TextInputEditText = findViewById(R.id.partyDateEditText)

        // Initialize the photo picker launcher
        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data!!
                viewModel.selectedImages.clear()

                if (data.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until minOf(count, 3)) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        viewModel.selectedImages.add(imageUri)
                    }
                } else if (data.data != null) {
                    val imageUri = data.data!!
                    viewModel.selectedImages.add(imageUri)
                }

                // Navigate to the Photo Preview Page
                if (viewModel.selectedImages.isNotEmpty()) {
                    navigateToPhotoPreview()
                } else {
                    Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        selectPhotosButton.setOnClickListener {
            val partyName = partyNameEditText.text.toString()
            val partyDate = partyDateEditText.text.toString()

            if (partyName.isEmpty() || partyDate.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Store current values to ViewModel to prevent data loss
            viewModel.partyName = partyName
            viewModel.partyDate = partyDate

            // Intent to open gallery to pick images
            val intent = Intent().apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                action = Intent.ACTION_GET_CONTENT
            }
            photoPickerLauncher.launch(Intent.createChooser(intent, "Select Pictures"))
        }

        // Set up click listener for party date edit text to open a date picker dialog
        partyDateEditText.setOnClickListener {
            // Show date picker dialog
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    partyDateEditText.setText(date)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Restore values from ViewModel when returning to this activity
        viewModel.partyName?.let { partyNameEditText.setText(it) }
        viewModel.partyDate?.let { partyDateEditText.setText(it) }
    }

    private fun navigateToPhotoPreview() {
        val intent = Intent(this, PhotoPreviewActivity::class.java)
        startActivity(intent)
    }
}
