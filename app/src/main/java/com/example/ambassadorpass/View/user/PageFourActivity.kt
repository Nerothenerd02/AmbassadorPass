package com.example.ambassadorpass.view.user


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.PartyRegistrationRepository
import com.example.ambassadorpass.viewmodel.PageFourViewModel
import com.example.ambassadorpass.viewmodel.PageFourViewModelFactory
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class PageFourActivity : AppCompatActivity() {

    private val viewModel: PageFourViewModel by viewModels {
        PageFourViewModelFactory(PartyRegistrationRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_four)

        // Retrieve details from the intent
        val partyLink = intent.getStringExtra("partyLink") ?: ""
        val identification = intent.getStringExtra("identification") ?: ""

        // Initialize UI components
        val backButton: ImageButton = findViewById(R.id.backButton)
        val qrCodeImageView: ImageView = findViewById(R.id.qrCodeImageView)
        val partyNameTextView: TextView = findViewById(R.id.partynameTextView)
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val emailTextView: TextView = findViewById(R.id.emailTextView)
        val phoneTextView: TextView = findViewById(R.id.phoneTextView)
        val identificationTextView: TextView = findViewById(R.id.identificationTextView)
        val nextButton: Button = findViewById(R.id.nextButton)

        // Back button functionality
        backButton.setOnClickListener {
            finish()
        }

        // Observe ViewModel LiveData
        observeViewModel(
            qrCodeImageView,
            partyNameTextView,
            nameTextView,
            emailTextView,
            phoneTextView,
            identificationTextView
        )

        // Fetch attendee details via ViewModel
        viewModel.fetchAttendeeDetails(partyLink, identification)

        // Next button functionality
        nextButton.setOnClickListener {
            navigateToPageFive(partyLink)
        }
    }

    private fun observeViewModel(
        qrCodeImageView: ImageView,
        partyNameTextView: TextView,
        nameTextView: TextView,
        emailTextView: TextView,
        phoneTextView: TextView,
        identificationTextView: TextView
    ) {
        viewModel.attendeeDetails.observe(this) { details ->
            details?.let {
                partyNameTextView.text = "Party Name: ${it["partyName"] ?: "N/A"}"
                nameTextView.text = "Name: ${it["name"] ?: "N/A"}"
                emailTextView.text = "Email: ${it["email"] ?: "N/A"}"
                phoneTextView.text = "Phone: ${it["phone"] ?: "N/A"}"
                identificationTextView.text = "Identification: ${it["identification"] ?: "N/A"}"

                // Generate QR Code
                val qrCodeData = it["qrCode"]?.toString() ?: "N/A"
                val bitmap = generateQRCode(qrCodeData)
                qrCodeImageView.setImageBitmap(bitmap)
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateQRCode(data: String): Bitmap {
        val size = 512 // QR Code size
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bitmap
    }

    private fun navigateToPageFive(partyLink: String) {
        val intent = Intent(this, PageFiveActivity::class.java).apply {
            putExtra("partyLink", partyLink) // Pass the partyLink to PageFiveActivity
        }
        startActivity(intent)
    }
}
