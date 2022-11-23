package com.group14.partybrainiac

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.findNavController
import com.group14.partybrainiac.databinding.FragmentMainMenuBinding

private var mp = MediaPlayer()

class MainMenuFragment : Fragment() {

    private lateinit var logoText: ImageView
    private lateinit var buMainMenuToGameMode: Button


    //  TODO: Rest of code for Buttons.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentMainMenuBinding>(inflater,
            R.layout.fragment_main_menu, container, false)

        logoText = binding.partyBrainiacText
        buMainMenuToGameMode = binding.BuMainMenuToGameMode

        animateButton(buMainMenuToGameMode, 1000)

        binding.BuMainMenuToGameMode.setOnClickListener{
                view : View -> view.findNavController().navigate(R.id.action_mainMenuFragment_to_gameModeFragment)
            playAudio(R.raw.button_select)
        }
        binding.BuMainMenuToSettingsPage.setOnClickListener{
                view : View -> view.findNavController().navigate(R.id.action_mainMenuFragment_to_settingsPageFragment)
            playAudio(R.raw.button_select)
        }
        binding.BuMainMenuToInfoPage.setOnClickListener{
                view : View -> view.findNavController().navigate(R.id.action_mainMenuFragment_to_infoPageFragment)
            playAudio(R.raw.button_select)
        }

        binding.BuMainMenuQuit.setOnClickListener{//TODO: Quit app code...
            playAudio(R.raw.button_back)
            (activity as MainActivity?)!!.QuitApp()
        }
        return binding.root
    }

    private fun playAudio(audioFile: Int) {
        mp = MediaPlayer.create(context, audioFile)
        mp.start()
    }
}



private fun animateButton(button: Button, speed: Long) {
    val animations = arrayOf(20f, -20f).map { translation ->
        ObjectAnimator.ofFloat(button, "translationY", translation).apply {
            duration = speed
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
    }

    val set = AnimatorSet()
    set.playTogether(animations)
    set.start()
}