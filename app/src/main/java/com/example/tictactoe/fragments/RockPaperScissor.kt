package com.example.tictactoe.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.tictactoe.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Random

class RockPaperScissor : Fragment() {

    companion object {
        private var winsCount = 0
        private var drawsCount = 0
        private var lossCount = 0
        private var userChoice = -1
        private var gameChoice = -1

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RockPaperScissor().apply {
                arguments = Bundle().apply {
                    putString("ARG_PARAM1", param1)
                    putString("ARG_PARAM2", param2)
                }
            }
    }

    private lateinit var ivRock: ImageView
    private lateinit var ivPaper: ImageView
    private lateinit var ivScissor: ImageView
    private lateinit var tvUserChoice: TextView
    private lateinit var tvGameChoice: TextView
    private lateinit var tvResult: TextView
    private lateinit var tvWins: TextView
    private lateinit var tvDraws: TextView
    private lateinit var tvLosses: TextView
    private lateinit var btnRestart: MaterialButton
    private lateinit var btnClear: MaterialButton
    private lateinit var fabProfile: FloatingActionButton
    private lateinit var resultCard: CardView

    private var originalWinColor = 0
    private var originalLossColor = 0
    private var originalDrawColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rock_paper_scissor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        setupClickListeners()
    }

    private fun init(view: View) {
        ivRock = view.findViewById(R.id.ivRock)
        ivPaper = view.findViewById(R.id.ivPaper)
        ivScissor = view.findViewById(R.id.ivSessior)
        tvUserChoice = view.findViewById(R.id.tvUserChoice)
        tvGameChoice = view.findViewById(R.id.tvGameChoice)
        tvResult = view.findViewById(R.id.tvResult)
        tvWins = view.findViewById(R.id.tvNoOfWins)
        tvDraws = view.findViewById(R.id.tvNoOfDraws)
        tvLosses = view.findViewById(R.id.tvNoOfLosses)
        btnClear = view.findViewById(R.id.btnClear)
        btnRestart = view.findViewById(R.id.btnRestart)
        resultCard = tvResult.parent as CardView
        originalWinColor = Color.parseColor("#4CAF50")
        originalLossColor = Color.parseColor("#F44336")
        originalDrawColor = Color.parseColor("#FF9800")

        updateStats()
    }

    private fun setupClickListeners() {
        ivRock.setOnClickListener { handleUserChoice(1, ivRock, "Rock") }
        ivPaper.setOnClickListener { handleUserChoice(2, ivPaper, "Paper") }
        ivScissor.setOnClickListener { handleUserChoice(3, ivScissor, "Scissors") }

        btnClear.setOnClickListener { clearGame() }
        btnRestart.setOnClickListener { restartGame() }
    }

    private fun handleUserChoice(choice: Int, selectedView: ImageView, choiceName: String) {
        userChoice = choice
        gameChoice = generateNumber()

        animateChoice(selectedView)
        tvUserChoice.text = "You chose: $choiceName"
        animateTextChange(tvUserChoice)

        val gameChoiceName = getChoiceName(gameChoice)
        tvGameChoice.text = "Computer chose: $gameChoiceName"
        animateTextChange(tvGameChoice)

        Handler(Looper.getMainLooper()).postDelayed({
            determineWinner()
        }, 800)
    }

    private fun animateChoice(selectedView: ImageView) {
        val cardView = selectedView.parent as CardView

        val scaleUpX = ObjectAnimator.ofFloat(selectedView, "scaleX", 1f, 1.3f)
        val scaleUpY = ObjectAnimator.ofFloat(selectedView, "scaleY", 1f, 1.3f)

        val scaleDownX = ObjectAnimator.ofFloat(selectedView, "scaleX", 1.3f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(selectedView, "scaleY", 1.3f, 1f)

        val elevation = ObjectAnimator.ofFloat(cardView, "cardElevation", 12f, 20f, 12f)

        val scaleUp = AnimatorSet().apply {
            playTogether(scaleUpX, scaleUpY, elevation)
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleDown = AnimatorSet().apply {
            playTogether(scaleDownX, scaleDownY)
            duration = 300
            interpolator = BounceInterpolator()
        }

        val fullAnimation = AnimatorSet().apply {
            playSequentially(scaleUp, scaleDown)
        }
        fullAnimation.start()

        addPulseEffect(selectedView)
    }

    private fun addPulseEffect(view: View) {
        val pulse = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.7f, 1f).apply {
            duration = 150
            repeatCount = 2
        }
        pulse.start()
    }

    private fun animateTextChange(textView: TextView) {
        val fadeOut = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f).apply {
            duration = 150
        }

        val fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f).apply {
            duration = 150
        }

        val textAnimation = AnimatorSet().apply {
            playSequentially(fadeOut, fadeIn)
        }
        textAnimation.start()
    }

    private fun determineWinner() {
        val result: String
        val resultColor: Int

        when {
            userChoice == gameChoice -> {
                result = "IT'S A DRAW!"
                drawsCount++
                resultColor = originalDrawColor
                animateStatChange(tvDraws)
            }
            (userChoice == 1 && gameChoice == 3) ||
                    (userChoice == 2 && gameChoice == 1) ||
                    (userChoice == 3 && gameChoice == 2) -> {
                result = "YOU WIN! 🎉"
                winsCount++
                resultColor = originalWinColor
                animateStatChange(tvWins)
                animateWinCelebration()
            }
            else -> {
                result = "YOU LOSE! 😔"
                lossCount++
                resultColor = originalLossColor
                animateStatChange(tvLosses)
                animateLossShake()
            }
        }

        animateResultText(result, resultColor)
        updateStats()
    }

    private fun animateResultText(result: String, color: Int) {
        tvResult.text = result

        val scaleX = ObjectAnimator.ofFloat(tvResult, "scaleX", 0.5f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(tvResult, "scaleY", 0.5f, 1.2f, 1f)
        val alpha = ObjectAnimator.ofFloat(tvResult, "alpha", 0f, 1f)

        val colorAnimator = ValueAnimator.ofArgb(Color.parseColor("#4CAF50"), color).apply {
            addUpdateListener { animation ->
                resultCard.setCardBackgroundColor(animation.animatedValue as Int)
            }
        }

        val resultAnimation = AnimatorSet().apply {
            playTogether(scaleX, scaleY, alpha, colorAnimator)
            duration = 600
            interpolator = BounceInterpolator()
        }
        resultAnimation.start()
    }

    private fun animateWinCelebration() {
        val bounce = ObjectAnimator.ofFloat(resultCard, "translationY", 0f, -30f, 0f).apply {
            duration = 500
            interpolator = BounceInterpolator()
        }
        bounce.start()
    }

    private fun animateLossShake() {
        val shake = ObjectAnimator.ofFloat(resultCard, "translationX", 0f, -25f, 25f, -25f, 25f, 0f).apply {
            duration = 500
        }
        shake.start()
    }

    private fun animateStatChange(statView: TextView) {
        val cardView = (statView.parent as View).parent as View

        val scaleX = ObjectAnimator.ofFloat(cardView, "scaleX", 1f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(cardView, "scaleY", 1f, 1.1f, 1f)

        val pulseAnimation = AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 300
            interpolator = DecelerateInterpolator()
        }
        pulseAnimation.start()
    }

    private fun clearGame() {
        val fadeOutUser = ObjectAnimator.ofFloat(tvUserChoice, "alpha", 1f, 0f)
        val fadeOutGame = ObjectAnimator.ofFloat(tvGameChoice, "alpha", 1f, 0f)
        val fadeOutResult = ObjectAnimator.ofFloat(tvResult, "alpha", 1f, 0f)

        val clearAnimation = AnimatorSet().apply {
            playTogether(fadeOutUser, fadeOutGame, fadeOutResult)
            duration = 300
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    userChoice = 0
                    gameChoice = 0
                    tvUserChoice.text = "Make your choice!"
                    tvGameChoice.text = "Computer is ready..."
                    tvResult.text = "Ready to Play?"
                    resultCard.setCardBackgroundColor(originalWinColor)

                    val fadeInUser = ObjectAnimator.ofFloat(tvUserChoice, "alpha", 0f, 1f)
                    val fadeInGame = ObjectAnimator.ofFloat(tvGameChoice, "alpha", 0f, 1f)
                    val fadeInResult = ObjectAnimator.ofFloat(tvResult, "alpha", 0f, 1f)

                    val fadeInAnimation = AnimatorSet().apply {
                        playTogether(fadeInUser, fadeInGame, fadeInResult)
                        duration = 300
                    }
                    fadeInAnimation.start()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
        clearAnimation.start()
    }

    private fun restartGame() {
        userChoice = 0
        gameChoice = 0
        winsCount = 0
        lossCount = 0
        drawsCount = 0

        val spinButton = ObjectAnimator.ofFloat(btnRestart, "rotation", 0f, 360f).apply {
            duration = 500
        }
        spinButton.start()

        tvUserChoice.text = "Make your choice!"
        tvGameChoice.text = "Computer is ready..."
        tvResult.text = "Game Restarted!"
        resultCard.setCardBackgroundColor(originalWinColor)

        animateStatsReset()

        Handler(Looper.getMainLooper()).postDelayed({
            tvResult.text = "Ready to Play?"
            updateStats()
        }, 1000)
    }

    private fun animateStatsReset() {
        val winsCard = (tvWins.parent as View).parent as View
        val drawsCard = (tvDraws.parent as View).parent as View
        val lossesCard = (tvLosses.parent as View).parent as View

        val resetWins = ObjectAnimator.ofFloat(winsCard, "scaleY", 1f, 0f, 1f)
        val resetDraws = ObjectAnimator.ofFloat(drawsCard, "scaleY", 1f, 0f, 1f)
        val resetLosses = ObjectAnimator.ofFloat(lossesCard, "scaleY", 1f, 0f, 1f)

        val resetAnimation = AnimatorSet().apply {
            playTogether(resetWins, resetDraws, resetLosses)
            duration = 400
        }
        resetAnimation.start()
    }

    private fun updateStats() {
        tvWins.text = winsCount.toString()
        tvDraws.text = drawsCount.toString()
        tvLosses.text = lossCount.toString()
    }

    private fun getChoiceName(choice: Int): String = when (choice) {
        1 -> "Rock"
        2 -> "Paper"
        3 -> "Scissors"
        else -> "Unknown"
    }

    private fun generateNumber(): Int = Random().nextInt(3) + 1
}