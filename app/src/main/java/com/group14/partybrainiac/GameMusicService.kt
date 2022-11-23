package com.group14.partybrainiac

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

open class GameMusicService : Service() {


    internal lateinit var mp: MediaPlayer
    override fun onBind(arg0: Intent?): IBinder? {
        TODO("Return the communication channel to the service.")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mp = MediaPlayer.create(this, R.raw.backgroundmusic)
        //val afd = applicationContext.assets.openFd("backgroundmusic") as AssetFileDescriptor
        //mp.setDataSource(afd.fileDescriptor)
        mp.isLooping = true
        mp.setVolume(100f,100f)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mp.start()
        return START_STICKY
    }

    override fun onStart(intent: Intent?, startId: Int) {

    }

    fun onUnBind(arg0: Intent?): IBinder? {
        return null
    }

    fun onStop() {

    }

    protected fun onPause() {
        if (mp.isPlaying()) {
            mp.pause()
        }
    }

    protected fun onResume() {
        mp.start()
    }

    override fun onDestroy() {
        mp.stop()
        mp.release()
    }

    override fun onLowMemory() {

    }

    companion object{
        private val TAG: String? = null
    }
}