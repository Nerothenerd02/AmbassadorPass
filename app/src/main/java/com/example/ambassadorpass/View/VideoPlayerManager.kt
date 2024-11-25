package com.example.ambassadorpass.view

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.common.C
import androidx.media3.ui.PlayerView
import com.example.ambassadorpass.R
import androidx.annotation.OptIn

@OptIn(androidx.media3.common.util.UnstableApi::class)
class VideoPlayerManager(private val context: Context, private val playerView: PlayerView) {

    private lateinit var player: ExoPlayer

    fun initializePlayer() {
        // Initialize a DefaultTrackSelector and disable audio tracks
        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setRendererDisabled(C.TRACK_TYPE_AUDIO, true))
        }

        // Initialize ExoPlayer with the track selector
        player = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build().apply {
                // Prepare the media item
                val mediaItem = MediaItem.fromUri(Uri.parse("android.resource://${context.packageName}/${R.raw.videoone}"))
                setMediaItem(mediaItem)

                // Set to loop the video indefinitely
                repeatMode = Player.REPEAT_MODE_ONE

                // Prepare and start playback
                prepare()
                playWhenReady = true
            }

        // Attach player to PlayerView
        playerView.player = player

        // Set the PlayerView's resize mode to fill the screen
        playerView.resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    fun resumePlayer() {
        player.playWhenReady = true
    }

    fun pausePlayer() {
        player.playWhenReady = false
    }

    fun releasePlayer() {
        player.release()
    }
}
