package com.example.ambassadorpass.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.common.C
import com.example.ambassadorpass.R
import com.example.ambassadorpass.databinding.ActivityMainBinding
import androidx.annotation.OptIn

@OptIn(androidx.media3.common.util.UnstableApi::class)
class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Reference to PlayerView in layout
        val playerView = binding.playerView

        // Initialize a DefaultTrackSelector and disable audio tracks
        val trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(
            trackSelector.buildUponParameters().setRendererDisabled(C.TRACK_TYPE_AUDIO, true)
        )

        // Initialize ExoPlayer with the track selector
        player = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
        playerView.player = player

        // Prepare the media item
        val mediaItem = MediaItem.fromUri(Uri.parse("android.resource://$packageName/${R.raw.videoone}"))
        player.setMediaItem(mediaItem)

        // Set the player to loop the video indefinitely
        player.repeatMode = Player.REPEAT_MODE_ONE

        // Set the PlayerView's resize mode to fill the screen
        playerView.resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        // Prepare and start playback
        player.prepare()
        player.playWhenReady = true

        // Keycode validation button
        val validateKeycodeButton: Button = binding.validateKeycodeButton
        val keycodeEditText: EditText = binding.keycodeEditText
        validateKeycodeButton.setOnClickListener {
            val keycode = keycodeEditText.text.toString()
            if (keycode.isNotEmpty()) {
                Toast.makeText(this, "Keycode entered: $keycode", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a keycode", Toast.LENGTH_SHORT).show()
            }
        }

        // Login button - Navigate to LoginActivityAdmin
        val loginButton: Button = binding.loginButton
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivityAdmin::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        player.playWhenReady = true // Resume playback
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false // Pause playback
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release() // Release ExoPlayer resources
    }
}
