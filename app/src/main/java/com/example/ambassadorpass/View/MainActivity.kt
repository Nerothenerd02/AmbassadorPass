package com.example.ambassadorpass.view

import android.content.Intent
import android.os.Bundle
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
                viewModel.validateKeycode(keycode) { isValid ->
                    runOnUiThread {
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
        val intent = Intent(this, PartyPreviewActivity::class.java).apply {
            putExtra("KEYCODE", keycode)
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
