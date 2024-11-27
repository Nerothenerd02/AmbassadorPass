package com.example.ambassadorpass.view.user

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ambassadorpass.R
import com.example.ambassadorpass.viewmodel.PageThreeViewModel
import com.example.ambassadorpass.viewmodel.PageThreeViewModelFactory
import com.example.ambassadorpass.repository.PartyRegistrationRepository
import kotlin.concurrent.thread

class PageThreeActivity : AppCompatActivity() {

    private val viewModel: PageThreeViewModel by viewModels {
        PageThreeViewModelFactory(PartyRegistrationRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_three)

        // Retrieve details from the intent
        val partyLink = intent.getStringExtra("partyLink") ?: "Unknown"
        val identification = intent.getStringExtra("identification") ?: "Unknown"

        // Initialize UI components
        val backButton: ImageButton = findViewById(R.id.backButton)
        val priceNameTextView: TextView = findViewById(R.id.pricename)
        val typewriterTextView: TextView = findViewById(R.id.typewriter)
        val payButton: Button = findViewById(R.id.payButton)

        // Typewriter effect messages
        val messages = listOf("Here's the hard part", "You need to pay.")

        // Back button functionality
        backButton.setOnClickListener {
            finish() // Navigate back to the previous page
        }

        // Observe ViewModel LiveData
        observeViewModel(priceNameTextView)

        // Fetch ticket price via ViewModel
        viewModel.fetchTicketPrice(partyLink)

        // Handle payment process
        payButton.setOnClickListener {
            val qrCodeData = generateQRCodeData(partyLink, identification)
            Log.d("PageThreeActivity", "Initiating payment process.")
            viewModel.processPayment(partyLink, identification, qrCodeData)
        }

        // Typewriter effect
        thread {
            while (true) {
                for (message in messages) {
                    val stringBuilder = StringBuilder()
                    for (letter in message) {
                        stringBuilder.append(letter)
                        Thread.sleep(100)
                        runOnUiThread {
                            typewriterTextView.text = stringBuilder.toString()
                        }
                    }
                    Thread.sleep(1000)
                }
            }
        }
    }

    private fun observeViewModel(priceNameTextView: TextView) {
        // Observe ticket price
        viewModel.ticketPrice.observe(this) { ticketPrice ->
            priceNameTextView.text = if (ticketPrice != null) {
                "Price: $ticketPrice"
            } else {
                "Price: Not Available"
            }
        }

        // Observe error messages
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                Log.e("PageThreeActivity", "Error: $errorMessage")
            }
        }

        // Observe operation status
        viewModel.operationStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            Log.d("PageThreeActivity", status)
        }
    }

    private fun generateQRCodeData(partyLink: String, identification: String): String {
        val qrCodeData = "PartyLink: $partyLink\nIdentification: $identification\nPaid: Yes"
        Log.d("PageThreeActivity", "Generated QR Code data: $qrCodeData")
        return qrCodeData
    }
}
