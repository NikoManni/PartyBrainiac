package com.group14.partybrainiac


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.group14.partybrainiac.databinding.FragmentInstructionsBinding

class InstructionsFragment : Fragment() {

    private var mp = MediaPlayer()
    private lateinit var continueButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentInstructionsBinding>(inflater,
            R.layout.fragment_instructions,container,false)

        continueButton = binding.continueButton

        animateButton(continueButton, 1000)

        binding.buttonBackInstructions.setOnClickListener {
                view: View -> view.findNavController().navigate(R.id.action_instructionsFragment_to_gameModeFragment)
            playAudio(R.raw.button_back)
        }

        binding.continueButton.setOnClickListener {
                view: View -> view.findNavController().navigate(R.id.action_instructionsFragment_to_gameFragment)
            playAudio(R.raw.button_select)
                activity?.stopService(Intent(context, BackgroundMusicService::class.java))
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