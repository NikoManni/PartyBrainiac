package com.group14.partybrainiac

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.group14.partybrainiac.databinding.FragmentSettingsPageBinding


class SettingsPageFragment : Fragment() {

    private var mp = MediaPlayer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSettingsPageBinding>(inflater, R.layout.fragment_settings_page, container, false)

        binding.BuSettingsPageToMainMenu.setOnClickListener{
                view : View -> view.findNavController().navigate(R.id.action_settingsPageFragment_to_mainMenuFragment)
            playAudio(R.raw.button_back)
        }
        //TODO: Following buttons might need to be real, not just placeholders...
        binding.BuSettingsAudio.setOnClickListener {
            (activity as MainActivity?)!!.notImpl("Audio Settings")
            playAudio(R.raw.button_select)
        }
        binding.BuSettingsGameplay.setOnClickListener {
            (activity as MainActivity?)!!.notImpl("Gameplay Settings")
            playAudio(R.raw.button_select)
        }
        return binding.root
    }

    private fun playAudio(audioFile: Int) {
        mp = MediaPlayer.create(context, audioFile)
        mp.start()
    }

}