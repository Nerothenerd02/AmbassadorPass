package com.example.ambassadorpass.view.admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
        val partyDateTimeEditText: TextInputEditText = findViewById(R.id.partyDateEditText)
        val partyDescriptionEditText: TextInputEditText = findViewById(R.id.partyDescriptionEditText)
        val partyLocationEditText: TextInputEditText = findViewById(R.id.partyLocationEditText)
        val ticketsAvailableEditText: TextInputEditText = findViewById(R.id.availableTicketsEditText)
        val ticketPriceEditText: TextInputEditText = findViewById(R.id.TicketpriceEditText)
        val ambassadorMarkupEditText: TextInputEditText = findViewById(R.id.ambassadorMarkupEditText)

        selectPhotosButton.setOnClickListener {
            val partyName = partyNameEditText.text.toString()
            val partyDateTimeStr = partyDateTimeEditText.text.toString()
            val partyDescription = partyDescriptionEditText.text.toString()
            val partyLocation = partyLocationEditText.text.toString()
            val ticketsAvailableStr = ticketsAvailableEditText.text.toString()
            val ticketPriceStr = ticketPriceEditText.text.toString()
            val ambassadorMarkupStr = ambassadorMarkupEditText.text.toString()

            if (partyName.isEmpty() || partyDateTimeStr.isEmpty() || partyDescription.isEmpty() ||
                partyLocation.isEmpty() || ticketsAvailableStr.isEmpty() || ticketPriceStr.isEmpty() ||
                ambassadorMarkupStr.isEmpty()
            ) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert date and time string to Timestamp
            val dateTimeFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date: Date? = dateTimeFormatter.parse(partyDateTimeStr)
            if (date == null) {
                Toast.makeText(this, "Invalid date/time format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val partyDate = Timestamp(date)
            val partyDateMillis = date.time

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
            viewModel.partyDateMillis = partyDateMillis
            viewModel.partyDescription = partyDescription
            viewModel.partyLocation = partyLocation
            viewModel.ticketsAvailable = ticketsAvailable
            viewModel.ticketPrice = ticketPrice
            viewModel.ambassadorMarkup = ambassadorMarkup

            // Navigate to Photo Preview Page
            navigateToPhotoPreview()
        }

        // Set up click listener for party date edit text to open a date and time picker dialog
        partyDateTimeEditText.setOnClickListener {
            val calendar = Calendar.getInstance()

            // Date picker dialog
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)

                    // Time picker dialog
                    TimePickerDialog(
                        this,
                        { _, hour, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                            calendar.set(Calendar.MINUTE, minute)

                            val dateTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(calendar.time)
                            partyDateTimeEditText.setText(dateTime)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true // 24-hour format
                    ).show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Restore values from ViewModel when returning to this activity
        viewModel.partyName?.let { partyNameEditText.setText(it) }
        viewModel.partyDate?.toDate()?.let { date ->
            val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date)
            partyDateTimeEditText.setText(formattedDate)
        }
        viewModel.partyDescription?.let { partyDescriptionEditText.setText(it) }
        viewModel.partyLocation?.let { partyLocationEditText.setText(it) }
        viewModel.ticketsAvailable?.let { ticketsAvailableEditText.setText(it.toString()) }
        viewModel.ticketPrice?.let { ticketPriceEditText.setText(it.toString()) }
        viewModel.ambassadorMarkup?.let { ambassadorMarkupEditText.setText(it.toString()) }
    }

    private fun navigateToPhotoPreview() {
        val intent = Intent(this, PhotoPreviewActivity::class.java).apply {
            putExtra("partyName", viewModel.partyName)
            putExtra("partyDateMillis", viewModel.partyDateMillis)
            putExtra("partyDescription", viewModel.partyDescription)
            putExtra("partyLocation", viewModel.partyLocation)
            putExtra("ticketsAvailable", viewModel.ticketsAvailable)
            putExtra("ticketPrice", viewModel.ticketPrice)
            putExtra("ambassadorMarkup", viewModel.ambassadorMarkup)
        }
        startActivity(intent)
    }
}
