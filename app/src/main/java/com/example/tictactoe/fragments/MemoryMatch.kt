package com.example.tictactoe.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tictactoe.R

class MemoryMatch : Fragment() {

    private lateinit var imagesGrid: GridLayout
    private lateinit var tvScoreText: TextView
    private lateinit var tvMoves: TextView
    private lateinit var btnRestart: Button
    private lateinit var btnReset: Button

    private val cardImages = listOf(
        R.drawable.ic_heart,
        R.drawable.fireball,
        R.drawable.sword,
        R.drawable.shield,
        R.drawable.flag,
        R.drawable.gem,
        R.drawable.coin,
        R.drawable.treasure
    )

    private val cardBack = R.drawable.ic_question

    private var cards = mutableListOf<Card>()
    private var flippedCards = mutableListOf<Card>()
    private var matchedPairs = 0
    private var totalMoves = 0
    private var score = 0
    private var canFlip = true

    data class Card(
        val imageRes: Int,
        var imageView: ImageView? = null,
        var isFlipped: Boolean = false,
        var isMatched: Boolean = false
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_memory_match, container, false)
        initViews(view)
        setupGame()
        setClickListeners()
        return view
    }

    private fun initViews(view: View) {
        imagesGrid = view.findViewById(R.id.imagesGrid)
        tvScoreText = view.findViewById(R.id.tvScoreText)
        tvMoves = view.findViewById(R.id.tvMoves)
        btnRestart = view.findViewById(R.id.btnRestart)
        btnReset = view.findViewById(R.id.btnReset)
    }

    private fun setClickListeners() {
        btnRestart.setOnClickListener {
            restartGame()
        }

        btnReset.setOnClickListener {
            resetScore()
        }
    }

    private fun setupGame() {
        imagesGrid.removeAllViews()
        cards.clear()
        flippedCards.clear()

        matchedPairs = 0
        totalMoves = 0
        canFlip = true

        updateUI()

        val cardDeck = mutableListOf<Int>()
        cardImages.forEach { image ->
            cardDeck.add(image)
            cardDeck.add(image)
        }

        cardDeck.shuffle()

        for (i in 0 until 16) {
            val card = Card(cardDeck[i])
            cards.add(card)

            val imageView = createCardImageView(i, card)
            card.imageView = imageView
            imagesGrid.addView(imageView)
        }
    }

    private fun createCardImageView(index: Int, card: Card): ImageView {
        return ImageView(requireContext()).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = dpToPx(80)
                height = dpToPx(80)
                setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
            }
            setImageResource(cardBack)
            scaleType = ImageView.ScaleType.CENTER_CROP
            isClickable = true
            setOnClickListener {
                if (canFlip && !card.isFlipped && !card.isMatched && flippedCards.size < 2) {
                    flipCard(card)
                }
            }
        }
    }

    private fun flipCard(card: Card) {
        if (card.isFlipped || card.isMatched) return

        flipAnimation(card.imageView!!, card.imageRes)
        card.isFlipped = true
        flippedCards.add(card)

        if (flippedCards.size == 2) {
            totalMoves++
            checkForMatch()
        }

        updateUI()
    }

    private fun flipAnimation(imageView: ImageView, newImageRes: Int) {
        val firstHalfFlip = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 0.0f)
        val secondHalfFlip = ObjectAnimator.ofFloat(imageView, "scaleX", 0.0f, 1.0f)

        firstHalfFlip.duration = 150
        secondHalfFlip.duration = 150

        firstHalfFlip.addUpdateListener {
            if (it.animatedFraction > 0.5f) {
                imageView.setImageResource(newImageRes)
            }
        }

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(firstHalfFlip, secondHalfFlip)
        animatorSet.start()
    }

    private fun checkForMatch() {
        canFlip = false

        val card1 = flippedCards[0]
        val card2 = flippedCards[1]

        if (card1.imageRes == card2.imageRes) {
            card1.isMatched = true
            card2.isMatched = true
            matchedPairs++
            score += 10

            flippedCards.clear()
            canFlip = true

            if (matchedPairs == 8) {
                score += 50
                showGameComplete()
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                flipBackCards(card1, card2)
            }, 1000)
        }

        updateUI()
    }

    private fun flipBackCards(card1: Card, card2: Card) {
        flipBackAnimation(card1.imageView!!)
        flipBackAnimation(card2.imageView!!)

        card1.isFlipped = false
        card2.isFlipped = false
        flippedCards.clear()
        canFlip = true
    }

    private fun flipBackAnimation(imageView: ImageView) {
        val firstHalfFlip = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 0.0f)
        val secondHalfFlip = ObjectAnimator.ofFloat(imageView, "scaleX", 0.0f, 1.0f)

        firstHalfFlip.duration = 150
        secondHalfFlip.duration = 150

        firstHalfFlip.addUpdateListener {
            if (it.animatedFraction > 0.5f) {
                imageView.setImageResource(cardBack)
            }
        }

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(firstHalfFlip, secondHalfFlip)
        animatorSet.start()
    }

    private fun updateUI() {
        tvScoreText.text = "Your Score: $score"
        tvMoves.text = "Moves: $totalMoves"
    }

    private fun showGameComplete() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Congratulations!")
            .setMessage("You completed the game in $totalMoves moves!\nFinal Score: $score")
            .setPositiveButton("Play Again") { dialog, _ ->
                dialog.dismiss()
                restartGame()
            }
            .setCancelable(false)
            .show()
    }

    private fun restartGame() {
        setupGame()
    }

    private fun resetScore() {
        score = 0
        updateUI()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    companion object {
        fun newInstance(): MemoryMatch {
            return MemoryMatch()
        }
    }
}