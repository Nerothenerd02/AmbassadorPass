package com.example.ambassadorpass.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.ambassadorpass.R
import com.example.ambassadorpass.databinding.ActivityMainBinding
import com.example.ambassadorpass.repository.KeycodeRepository
import com.example.ambassadorpass.viewmodel.KeycodeViewModel
import com.example.ambassadorpass.viewmodel.KeycodeViewModelFactory
import com.example.ambassadorpass.view.user.PartyPreviewActivity
import android.util.Log

@OptIn(UnstableApi::class)
class MainActivity : AppCompatActivity() {

    // ViewModel initialization with KeycodeRepository
    private val viewModel: KeycodeViewModel by viewModels {
        KeycodeViewModelFactory(KeycodeRepository())
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var videoPlayerManager: VideoPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Initialize the video player
        videoPlayerManager = VideoPlayerManager(this, binding.playerView)
        videoPlayerManager.initializePlayer()

        // Set up button click listeners
        setupKeycodeValidation()
        setupLoginButton()
    }

    private fun setupKeycodeValidation() {
        binding.validateKeycodeButton.setOnClickListener {
            val keycode = binding.keycodeEditText.text.toString().trim()
            if (keycode.isNotEmpty()) {
                showLoading(true) // Show the progress bar
                viewModel.validateKeycode(keycode) { isValid ->
                    runOnUiThread {
                        showLoading(false) // Hide the progress bar
                        if (isValid) {
                            navigateToPartyPreview(keycode)
                        } else {
                            showToast("Invalid keycode, please try again.")
                        }
                    }
                }
            } else {
                showToast("Please enter a keycode")
            }
        }
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToPartyPreview(keycode: String) {
        Log.d("MainActivity", "Navigating to PartyPreview with keycode: $keycode")
        val intent = Intent(this, PartyPreviewActivity::class.java).apply {
            putExtra("PARTY_LINK", keycode)
        }
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivityAdmin::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.validateKeycodeButton.isEnabled = !isLoading
        binding.loginButton.isEnabled = !isLoading
    }

    // Lifecycle management for video player
    override fun onStart() {
        super.onStart()
        videoPlayerManager.resumePlayer()
    }

    override fun onPause() {
        super.onPause()
        videoPlayerManager.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayerManager.releasePlayer()
    }
}
