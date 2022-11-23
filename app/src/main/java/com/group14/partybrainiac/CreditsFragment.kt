package com.group14.partybrainiac

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.group14.partybrainiac.databinding.FragmentCreditsBinding


class CreditsFragment : Fragment() {

    private var mp = MediaPlayer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCreditsBinding>(
            inflater,R.layout.fragment_credits,container,false
        )
        binding.buttonBackCredits.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_creditsFragment_to_infoPageFragment)
            playAudio(R.raw.button_back)
        }

        return binding.root
    }

    private fun playAudio(audioFile: Int) {
        mp = MediaPlayer.create(context, audioFile)
        mp.start()
    }
}