package com.group14.partybrainiac

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.group14.partybrainiac.databinding.FragmentGameModeBinding

private lateinit var BuGameModeLanguage: Button
private lateinit var BuGameModeParty: Button
private lateinit var BuGameModeLearn: Button

class GameModeFragment : Fragment() {

    private var mp = MediaPlayer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentGameModeBinding>(inflater,
            R.layout.fragment_game_mode,container,false)

        BuGameModeLanguage = binding.BuGameModeLanguage
        BuGameModeParty = binding.BuGameModeParty
        BuGameModeLearn = binding.BuGameModeLearn

        animateButton(BuGameModeLanguage, 800)
        animateButton(BuGameModeParty, 1000)
        animateButton(BuGameModeLearn, 900)

        binding.BuGameModeToMainMenu.setOnClickListener{
            view : View -> view.findNavController().navigate(R.id.action_gameModeFragment_to_mainMenuFragment)
            playAudio(R.raw.button_back)
        }
        binding.BuGameModeLanguage.setOnClickListener {
            //TODO: Everything! Quick game settings dialog-popup to select language (and time) to be used in next game.
            view: View -> view.findNavController().navigate(R.id.action_gameModeFragment_to_languageGameFragment)
            playAudio(R.raw.button_select)
        }
        binding.BuGameModeParty.setOnClickListener {
            //TODO: Quick game settings dialog-popup to select word list and time to be used in next game.
            view : View -> view.findNavController().navigate(R.id.action_gameModeFragment_to_instructionsFragment)
            playAudio(R.raw.button_select)
        }
        binding.BuGameModeLearn.setOnClickListener {
            playAudio(R.raw.button_select)
            //TODO: Everything! Quick game settings dialog-popup to select topic, game mode (Alias/reverse-Alias) (and time) to be used in next game.
            (activity as MainActivity?)!!.notImpl("Learn Mode")
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

