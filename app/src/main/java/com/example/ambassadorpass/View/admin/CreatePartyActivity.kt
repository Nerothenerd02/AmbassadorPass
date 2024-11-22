package com.example.ambassadorpass.view.admin

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
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
        val partyDescriptionEditText: TextInputEditText = findViewById(R.id.partyDescriptionEditText)
        val partyLocationEditText: TextInputEditText = findViewById(R.id.partyLocationEditText)
        val ticketsAvailableEditText: TextInputEditText = findViewById(R.id.availableTicketsEditText)
        val ticketPriceEditText: TextInputEditText = findViewById(R.id.TicketpriceEditText)
        val ambassadorMarkupEditText: TextInputEditText = findViewById(R.id.ambassadorMarkupEditText)


        selectPhotosButton.setOnClickListener {
            val partyName = partyNameEditText.text.toString()
            val partyDate = partyDateEditText.text.toString()
            val partyDescription = partyDescriptionEditText.text.toString()
            val partyLocation = partyLocationEditText.text.toString()
            val ticketsAvailableStr = ticketsAvailableEditText.text.toString()
            val ticketPriceStr = ticketPriceEditText.text.toString()
            val ambassadorMarkupStr = ambassadorMarkupEditText.text.toString()

            if (partyName.isEmpty() || partyDate.isEmpty() || partyDescription.isEmpty() ||
                partyLocation.isEmpty() || ticketsAvailableStr.isEmpty() || ticketPriceStr.isEmpty() ||
                ambassadorMarkupStr.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert numeric strings to appropriate types
            val ticketsAvailable = ticketsAvailableStr.toIntOrNull()
            val ticketPrice = ticketPriceStr.toDoubleOrNull()
            val ambassadorMarkup = ambassadorMarkupStr.toDoubleOrNull()

            if (ticketsAvailable == null || ticketPrice == null || ambassadorMarkup == null) {
                Toast.makeText(this, "Please enter valid numbers for tickets, price, and markup", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Store current values to ViewModel to prevent data loss
            viewModel.partyName = partyName
            viewModel.partyDate = partyDate
            viewModel.partyDescription = partyDescription
            viewModel.partyLocation = partyLocation
            viewModel.ticketsAvailable = ticketsAvailable
            viewModel.ticketPrice = ticketPrice
            viewModel.ambassadorMarkup = ambassadorMarkup

            // Navigate to Photo Preview Page
            navigateToPhotoPreview()
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
        val intent = Intent(this, PhotoPreviewActivity::class.java).apply {
            putExtra("partyName", viewModel.partyName)
            putExtra("partyDate", viewModel.partyDate)
            putExtra("partyDescription", viewModel.partyDescription)
            putExtra("partyLocation", viewModel.partyLocation)
            putExtra("ticketsAvailable", viewModel.ticketsAvailable)
            putExtra("ticketPrice", viewModel.ticketPrice)
            putExtra("ambassadorMarkup", viewModel.ambassadorMarkup)
        }
        startActivity(intent)
    }


}
