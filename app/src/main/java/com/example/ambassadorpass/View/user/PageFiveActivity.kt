package com.example.ambassadorpass.view.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.PartyRegistrationRepository
import com.example.ambassadorpass.view.MainActivity
import com.example.ambassadorpass.viewmodel.PageFiveViewModel
import com.example.ambassadorpass.viewmodel.PageFiveViewModelFactory
import kotlin.concurrent.thread

class PageFiveActivity : AppCompatActivity() {

    private val viewModel: PageFiveViewModel by viewModels {
        PageFiveViewModelFactory(PartyRegistrationRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_five)

        // Initialize UI components
        val backButton: ImageButton = findViewById(R.id.backButton)
        val confirmButton: Button = findViewById(R.id.confirmButton)
        val additionalInfoTextView: TextView = findViewById(R.id.additionalInfoTextView)
        val partyDetailsTextView: TextView = findViewById(R.id.partyDetailsTextView)
        val userDetailsTextView: TextView = findViewById(R.id.userDetailsTextView)

        // Fetch data from intent
        val partyLink = intent.getStringExtra("partyLink") ?: ""

        // Observe ViewModel LiveData
        observeViewModel(partyDetailsTextView, userDetailsTextView)

        // Fetch party details
        viewModel.fetchPartyDetails(partyLink)

        // Back button functionality
        backButton.setOnClickListener {
            finish()
        }

        // Confirm button functionality
        confirmButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Typewriter effect messages
        val messages = listOf(
            "We did not forget to give you the location.",
            "We did not forget to give you the date and time.",
            "You will receive an email with updates."
        )

        // Apply the typewriter effect
        applyTypewriterEffect(additionalInfoTextView, messages)
    }

    private fun observeViewModel(
        partyDetailsTextView: TextView,
        userDetailsTextView: TextView
    ) {
        viewModel.partyDetails.observe(this) { details ->
            details?.let {
                partyDetailsTextView.text = "Location: ${it["partyLocation"]}"
                userDetailsTextView.text = "Time: ${it["partyDate"]}"
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyTypewriterEffect(textView: TextView, messages: List<String>) {
        thread {
            while (true) {
                for (message in messages) {
                    val stringBuilder = StringBuilder()
                    for (letter in message) {
                        stringBuilder.append(letter)
                        Thread.sleep(100) // Adjust speed of typewriting
                        runOnUiThread {
                            textView.text = stringBuilder.toString()
                        }
                    }
                    Thread.sleep(1000) // Pause between messages
                }
            }
        }
    }
}
