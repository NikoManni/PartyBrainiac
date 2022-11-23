package com.group14.partybrainiac

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import com.group14.partybrainiac.databinding.FragmentGameOverBinding
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size


class GameOverFragment : Fragment() {

    private var mp = MediaPlayer()
    private lateinit var viewConfetti: KonfettiView
    private lateinit var scoretv: TextView
    private lateinit var btnPlayAgain: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentGameOverBinding>(inflater, R.layout.fragment_game_over,container,false)
        viewConfetti = binding.viewConfetti
        scoretv = binding.textView3
        btnPlayAgain = binding.BuGameOverToGame

        loadConfetti()
        animateButton(btnPlayAgain, 1000)

        binding.BuGameOverToMainMenu.setOnClickListener {
                view : View -> view.findNavController().navigate(R.id.action_gameOverFragment_to_mainMenuFragment)
            playAudio(R.raw.button_back)
        }
        binding.BuGameOverToGame.setOnClickListener {
            //TODO: RePlay code, just navigating back causes app crash!
                view : View -> view.findNavController().navigate(R.id.action_gameOverFragment_to_gameModeFragment)
            playAudio(R.raw.button_select)
        }

        return binding.root
    }

    //  ConfettiCannon !! :D
    private fun loadConfetti() {
        viewConfetti.build()
            .addColors(Color.YELLOW, Color.rgb(250, 43, 93), Color.GREEN)
            .setDirection(0.0, 365.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(3000L)
            .addShapes(Shape.RECT, Shape.CIRCLE)
            .addSizes(Size(12))
            .setPosition(2200f, viewConfetti.width + 5f, -50f, -50f)
            .streamFor(300, 5000L)
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

    private fun playAudio(audioFile: Int) {
        mp = MediaPlayer.create(context, audioFile)
        mp.start()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestKey") { key, bundle ->
            val result = bundle.getString("data")
            scoretv.text = result.toString()

        }
    }
}