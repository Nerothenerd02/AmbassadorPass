package com.example.ambassadorpass.view.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.PartyPreviewRepository
import com.example.ambassadorpass.viewmodel.PartyPreviewViewModel
import com.example.ambassadorpass.viewmodel.PartyPreviewViewModelFactory
import android.util.Log
import com.example.ambassadorpass.view.user.PageOneActivity

class PartyPreviewActivity : AppCompatActivity() {

    private val viewModel: PartyPreviewViewModel by viewModels {
        PartyPreviewViewModelFactory(PartyPreviewRepository())
    }

    private lateinit var slideshowImageView: ImageView
    private lateinit var backButton: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var skipButton: Button

    private val handler = Handler(Looper.getMainLooper())
    private var imageUrls: List<String> = emptyList()
    private var currentIndex = 0
    private val slideshowInterval = 5000L // 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_preview)

        // Initialize views
        slideshowImageView = findViewById(R.id.slideshowImageView)
        backButton = findViewById(R.id.backButton)
        progressBar = findViewById(R.id.progressBar)
        previousButton = findViewById(R.id.previousButton)
        nextButton = findViewById(R.id.nextButton)
        skipButton = findViewById(R.id.skipButton)

        // Back button functionality
        backButton.setOnClickListener { finish() }

        // Skip button functionality
        skipButton.setOnClickListener { navigateToIntroduction() }

        // Previous button functionality
        previousButton.setOnClickListener {
            if (imageUrls.isNotEmpty()) {
                currentIndex = if (currentIndex - 1 < 0) imageUrls.size - 1 else currentIndex - 1
                updateSlideshowImage()
            }
        }

        // Next button functionality
        nextButton.setOnClickListener {
            if (imageUrls.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % imageUrls.size
                updateSlideshowImage()
            }
        }

        // Show progress bar initially
        showLoading(true)

        // Fetch images immediately
        fetchImages()
    }

    private fun fetchImages() {
        viewModel.imageUrls.observe(this) { urls ->
            if (urls != null && urls.isNotEmpty()) {
                imageUrls = urls
                startSlideshow()
                showLoading(false)
            } else {
                showLoading(false)
                Toast.makeText(this, "No images available for this party.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                showLoading(false)
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        val partyLink = intent.getStringExtra("PARTY_LINK") ?: ""
        Log.d("PartyPreviewActivity", "Received partyLink: $partyLink")
        viewModel.fetchImageUrls(partyLink)
    }

    private fun startSlideshow() {
        handler.post(object : Runnable {
            override fun run() {
                if (imageUrls.isNotEmpty()) {
                    updateSlideshowImage()
                    if (currentIndex == imageUrls.size - 1) {
                        // Navigate to PageOneActivity when the slideshow ends
                        navigateToIntroduction()
                        return
                    }
                    currentIndex = (currentIndex + 1) % imageUrls.size
                    handler.postDelayed(this, slideshowInterval)
                }
            }
        })
    }

    private fun updateSlideshowImage() {
        Glide.with(this).load(imageUrls[currentIndex]).into(slideshowImageView)
    }

    private fun navigateToIntroduction() {
        // Stop the slideshow
        handler.removeCallbacksAndMessages(null)

        val partyLink = intent.getStringExtra("PARTY_LINK") ?: ""
        val intent = Intent(this, PageOneActivity::class.java).apply {
            putExtra("PARTY_LINK", partyLink)
        }
        startActivity(intent)
        finish()
    }



    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        slideshowImageView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
