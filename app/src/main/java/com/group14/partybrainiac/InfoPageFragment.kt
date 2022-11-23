package com.group14.partybrainiac

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.group14.partybrainiac.databinding.FragmentInfoPageBinding


class InfoPageFragment : Fragment() {

    private var mp = MediaPlayer()

    //  TODO: Binding setup, Button codes.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentInfoPageBinding>(
            inflater,
            R.layout.fragment_info_page, container, false
        )

        binding.BuInfoPageToMainMenu.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_infoPageFragment_to_mainMenuFragment)

            playAudio(R.raw.button_back)
        }
        //TODO: Following buttons might need to be real, not just placeholders...
        binding.BuAbout.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_infoPageFragment_to_aboutFragment)

            playAudio(R.raw.button_select)
        }
        binding.BuCredits.setOnClickListener {view: View ->
            view.findNavController().navigate(R.id.action_infoPageFragment_to_creditsFragment)

            playAudio(R.raw.button_select)
        }
        return binding.root
    }

    private fun playAudio(audioFile: Int) {
        mp = MediaPlayer.create(context, audioFile)
        mp.start()
    }
}

