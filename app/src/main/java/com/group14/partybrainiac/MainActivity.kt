package com.group14.partybrainiac

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.group14.partybrainiac.databinding.ActivityMainBinding
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private var currentApiVersion: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        startService(Intent(applicationContext, BackgroundMusicService::class.java))

        currentApiVersion = Build.VERSION.SDK_INT
        val flags: Int = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = flags
            val decorView: View = window.decorView
            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN === 0) {
                    decorView.systemUiVisibility = flags
                }
            }
        }


    }

    fun notImpl(call : String) {
        Toast.makeText(this, call + " not implemented yet.", Toast.LENGTH_SHORT).show()
        //Use following to show this toast anywhere in the app!
        //  (activity as MainActivity?)!!.notImpl("Action")
    }

    fun QuitApp() {

        this.finish()
        exitProcess(0)
    }
}