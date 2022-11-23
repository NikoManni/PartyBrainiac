package com.group14.partybrainiac

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.database.*
import com.group14.partybrainiac.databinding.FragmentGameBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList
import android.text.format.DateUtils
import android.widget.ProgressBar
import nl.dionsegijn.konfetti.KonfettiView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult

class GameFragment : Fragment(), SensorEventListener {

    //  variable names for calling bound ui elements
    private lateinit var sensorManager: SensorManager
    private lateinit var wordTv: TextView

    private lateinit var wordClearCount: TextView
    private lateinit var wordSkipCount: TextView
    private lateinit var scoreCount: TextView

    //  Variables for gesture control
    private var gameStarted: Boolean = false
    private var nextWordAvailable: Boolean = false
    private var skipWord: Boolean = false

    //  Counter variables for words, skips and combo
    private var wordCount = 0
    private var skipCount = 0
    private var consecutiveWordCount = 0
    private var wordsUsedCount = 0
    private var firstWord = true

    //  Get words from WHERE: 1 = Firebase database, 2 = none, else local
    private var source = 1

    //var teamPoints = intArrayOf(0, 0, 0, 0)
    private var score: Int = 0

    //  List of used word-id's
    private var usedWords: MutableList<Int> = ArrayList()

    //  TODO: Check these for errors!
    private lateinit var timerTv: TextView
    private lateinit var viewConfetti: KonfettiView
    private lateinit var progressTimer: ProgressBar

    private var START_MILLI_SECONDS = 60000L
    private lateinit var countdown_timer: CountDownTimer
    private var isRunning: Boolean = false;
    private var time_in_milli_seconds = 0L

    //  TODO: Bring sensor limit values here for easier editing and fine-tuning
    //private val skipWordLimit = null
    //private val skipWordReturn = null
    //private val nextWordLimit = null
    //private val nextWordReturn = null

    private lateinit var database: DatabaseReference
    private var mp = MediaPlayer()


    //  TODO: Binding setup, Gesture control, UI element updates. Navigation placeholder buttons?
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(inflater,
            R.layout.fragment_game, container, false)

        //  TODO: Init-code for possible game modes: Party, language, learn, (Alias?), etc. Affects Button visibility, timer, pointCalc etc.

        //  All in-game controls can be added here, use this setup to initialize required functions, set those not required to invisible.

        //  For example, Alias-mode requires timer, score-counter and buttons for control, but does not need gestures.
        //  PartyBrainiac Party -mode requires timer, score-counter and gesture control but no buttons.
        //  Learn-mode requires either buttons or gestures, but timer and score might not be required in that mode
        //  Word-list used for game mode could be initialized from here too...

        //  Below how to connect "codenames" to ui elements
        wordTv = binding.TvWord
        wordClearCount = binding.TvWordClearedCount
        wordSkipCount = binding.TvWordsSkippedCount
        scoreCount = binding.TvPointCounterPoints
        timerTv = binding.TvTimer
        viewConfetti = binding.viewConfetti
        progressTimer = binding.PbWordTimer


        binding.BuPlaceHolderGameToGameOver.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)
            playAudio(R.raw.button_back)
            activity?.startService(Intent(context, BackgroundMusicService::class.java))
        }


        binding.BuNextWord.setOnClickListener {
            if(wordsUsedCount == 0) {
                startGame()
                nextWord()
            } else {
                startGame()
                scoreOnNoSkip()
            }

        }
        binding.BuSkipWord.setOnClickListener {
            if(wordsUsedCount == 0) {
                startGame()
                nextWord()
            } else {
                nextWord()
                scoreOnSkip()
            }
        }

        //  TODO: Any other setups to here (Timer init etc...)
        setUpSensor()

        scoreCount.text = score.toString()
        wordClearCount.text = wordCount.toString()
        wordSkipCount.text = skipCount.toString()

        return binding.root
    }

    // Get next Word from where
    private fun nextWord() {
        when (source) {
            1 -> readDatabase()
            else -> readLocal()
        }
        wordsUsedCount++
    }

    //  Next Word from database
    private fun readDatabase() {
        val wordId = randomNumber()
        database = FirebaseDatabase.getInstance().getReference("wordlist")
        database.get().addOnSuccessListener {
            val randomWord = it.child("$wordId").value
            wordTv.text = randomWord.toString()
        }
    }

    //  Next Word from local file
    private fun readLocal() {
        var text = ""
        var lineNumber = 1
        var used = true
        var randNum = 0

        try {
            val `file`: InputStream = this.resources.openRawResource(R.raw.words)
            val reader = BufferedReader(InputStreamReader(`file`))

            //  FIXME: Possible app freeze here, if you run "out of words".
            while (used) {
                randNum = randomNumber()
                if (!usedWords.contains(randNum)) {
                    used = false
                    usedWords.add(randNum)
                }
            }

            while (lineNumber < 399) {
                if (lineNumber == randNum) {

                    text = reader.readLine()
                    if (text != null) {
                        wordTv.text = text
                    }
                    break
                } else {
                    reader.readLine()
                    lineNumber++
                }


            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //  Score calc when skipping
    private fun scoreOnSkip() {
        if (firstWord) {

        } else {
            score -= 15
            consecutiveWordCount = 0

            skipCount++
            wordSkipCount.text = skipCount.toString()
            scoreCount.text = score.toString()
            playAudio(R.raw.fart)
        }

    }

    //  Score calc on normal
    private fun scoreOnNoSkip() {

        if (firstWord) {
            firstWord = false
            playAudio(R.raw.game_start)
        } else {

            score += 10 + consecutiveWordCount * 3
            consecutiveWordCount++

            wordCount++
            wordClearCount.text = wordCount.toString()
            scoreCount.text = score.toString()
            playAudio(R.raw.correct_sparkle)
        }
    }

    //  Sensor setup
    private fun setUpSensor() {
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    //  Gesture control
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val leftRight = event.values[2]
            println(leftRight)

            if (wordsUsedCount == 0 && (leftRight <= -8)) {
                nextWordAvailable = true
                gameStarted = true
            } else if (leftRight <= -8) {
                nextWordAvailable = true
                skipWord = false
            } else if (leftRight > 9) {
                if (gameStarted) {
                    nextWordAvailable = true
                    skipWord = true
                }
            } else if (leftRight >= -1 && leftRight < 3 && nextWordAvailable) {
                nextWord()
                if (gameStarted && wordCount == 0) {
                    startGame()
                }
                if (skipWord) {
                    scoreOnSkip()
                } else {
                    scoreOnNoSkip()
                }
                nextWordAvailable = false
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    //  Delete sensor listener, check sensor setup.
    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    //  Random number generator... What if wordlist os not exactly 400 rows?
    private fun randomNumber(): Int {

        return (1..399).shuffled().last()

    }

    private fun playAudio(audioFile: Int) {
        mp = MediaPlayer.create(context, audioFile)
        mp.start()
    }

    private fun startGame() {
        if (isRunning) {
            //  Do nothing?
        } else {
            val time = 1
            time_in_milli_seconds = time.toLong() * 60000L
            startTimer(time_in_milli_seconds)

            println("time_in_milli_seconds: " + time_in_milli_seconds)
        }
    }

    //  Timer code...
    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                val result = score.toString()
                setFragmentResult("requestKey", bundleOf("data" to result)) // FIXME: GAME CRASHES HERE ???
                playAudio(R.raw.applause)
                activity?.startService(Intent(context, BackgroundMusicService::class.java))

                gameStarted = false

                view?.findNavController()?.navigate(R.id.action_gameFragment_to_gameOverFragment)
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTime()
            }
        }
        countdown_timer.start()

        isRunning = true

    }

    //  Timer reset?
    private fun resetTimer() {
        time_in_milli_seconds = START_MILLI_SECONDS
        updateTime()

    }

    //  Timer update code, format to xx.yy?
    private fun updateTime() {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60
        val formattedMin = DateUtils.formatElapsedTime(minute).drop(3)
        val formattedSec = DateUtils.formatElapsedTime(seconds).drop(2)
        val formattedTimer = formattedMin.plus(formattedSec)

        progressTimer.progress += 1
        var progressColor = Color.GREEN
        if (progressTimer.progress in 30..44) {
            progressColor = Color.YELLOW
        } else if (progressTimer.progress >= 45) {
            progressColor = Color.RED
        }
        progressTimer.progressDrawable.setColorFilter(
            progressColor, android.graphics.PorterDuff.Mode.SRC_IN);

        timerTv.text = "$formattedTimer"
    }

}