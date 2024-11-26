package com.example.ambassadorpass.view.user

import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.PartyRegistrationRepository
import com.example.ambassadorpass.viewmodel.PageOneViewModel
import com.example.ambassadorpass.viewmodel.PageOneViewModelFactory
import kotlin.concurrent.thread
import android.util.Log

class PageOneActivity : AppCompatActivity() {

    private val viewModel: PageOneViewModel by viewModels {
        PageOneViewModelFactory(PartyRegistrationRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_one)

        // Initialize the back button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener { finish() }

        // Get the partyLink passed from the previous page
        val partyLink = intent.getStringExtra("PARTY_LINK") ?: ""
        Log.d("PageOneActivity", "Received partyLink: $partyLink")

        // Initialize the party name TextView
        val partyNameTextView = findViewById<TextView>(R.id.partyname)

        // Observe the party name from the ViewModel
        viewModel.partyName.observe(this) { partyName ->
            partyNameTextView.text = partyName
        }

        // Fetch party name using the partyLink
        viewModel.fetchPartyName(partyLink)

        // Initialize the typewriter TextView
        val textView = findViewById<TextView>(R.id.typewriter)

        // List of messages to be displayed with typewriter effect
        val messages = listOf("Wanna Party?", "Wanna Make New Friends?", "Join us now!")

        // Start displaying typewriter messages in an infinite loop
        thread {
            while (true) {
                for (message in messages) {
                    val stringBuilder = StringBuilder()
                    for (letter in message) {
                        stringBuilder.append(letter)
                        Thread.sleep(100)
                        runOnUiThread {
                            textView.text = stringBuilder.toString()
                        }
                    }
                    // Wait for 1 second after each message is fully displayed
                    Thread.sleep(1000)
                }
            }
        }

        // Initialize the "Proceed" button
        val proceedButton: Button = findViewById(R.id.proceedButton)
        proceedButton.setOnClickListener {
            navigateToPageTwo(partyLink)
        }
    }

    private fun navigateToPageTwo(partyLink: String) {
        val intent = Intent(this, PageTwoActivity::class.java).apply {
            putExtra("PARTY_LINK", partyLink)
        }
        startActivity(intent)
    }
}
