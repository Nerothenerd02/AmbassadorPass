package com.example.ambassadorpass

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.ambassadorpass.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Reference to PlayerView in layout
        val playerView = binding.playerView

        // Initialize ExoPlayer
        player = ExoPlayer.Builder(this).build()
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
