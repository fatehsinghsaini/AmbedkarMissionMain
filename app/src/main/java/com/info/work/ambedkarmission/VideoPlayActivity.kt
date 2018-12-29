package com.info.work.ambedkarmission

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.MediaController
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory.newSimpleInstance
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.info.work.ambedkarmission.databinding.VideoPlayActivityBinding
import com.ois.todo.activity.BaseActivity
import java.io.File


class VideoPlayActivity : BaseActivity() {

    private var player: SimpleExoPlayer? = null
    var mView: VideoPlayActivityBinding? = null
    var uri: Uri? = null
    var playbackPosition: Long = 0
    var currentWindow: Int = 0
    var videoFile: String = ""
    var playWhenReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = DataBindingUtil.setContentView<VideoPlayActivityBinding>(this, R.layout.video_play_activity)

        if (intent.hasExtra("url")) {
            videoFile = intent.getStringExtra("url")

            if (!videoFile.contains("http")) {

                var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), videoFile)
                uri = Uri.fromFile(file)

                mView!!.myVideo.visibility = View.VISIBLE
                mView!!.videoView.visibility = View.GONE

                mView!!.myVideo.setMediaController(MediaController(this))
                mView!!.myVideo.setVideoURI(uri)
                mView!!.myVideo.requestFocus()
                mView!!.myVideo.start()


            } else {
                uri = Uri.parse(videoFile)
                mView!!.myVideo.visibility = View.GONE
                mView!!.videoView.visibility = View.VISIBLE
            }

        }

        mView!!.closeWindow.setOnClickListener(View.OnClickListener { finish() })


    }

    private fun initializePlayer() {
        player = newSimpleInstance(DefaultRenderersFactory(this),
                DefaultTrackSelector(), DefaultLoadControl())

        mView!!.videoView.setPlayer(player)

        var mediaSource: MediaSource? = null
        if (!videoFile.contains("http"))
            mediaSource = buildMediaSourceOffline(uri!!)
        else
            mediaSource = buildMediaSourceOnline(uri!!)

        player!!.prepare(mediaSource, true, false)
        player!!.setPlayWhenReady(playWhenReady)
        player!!.seekTo(currentWindow, playbackPosition)
    }

    private fun buildMediaSourceOffline(uri: Uri): MediaSource {

        val dashChunkSourceFactory = DefaultDashChunkSource.Factory(
                DefaultHttpDataSourceFactory("exoplayer-codelab", null))
        val manifestDataSourceFactory = DefaultHttpDataSourceFactory("exoplayer-codelab")
        return DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)


        //return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(uri)
    }

    private fun buildMediaSourceOnline(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(
                DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(uri)
    }


    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            if (intent.getStringExtra("url").contains("http"))
                initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {

            if (intent.getStringExtra("url").contains("http"))
                initializePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        mView!!.videoView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {

            try {
                releasePlayer()
            } catch (e: Exception) {
                Log.e("error", e.message)
            }

        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {

        try {
            if (player != null) {
                playbackPosition = player!!.getCurrentPosition()
                currentWindow = player!!.getCurrentWindowIndex()
                playWhenReady = player!!.getPlayWhenReady()
                player!!.release()
                player = null
            }

        } catch (e: Exception) {
            Log.e("error", e.message)
        }


    }


}

