package com.example.tictactoe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tictactoe.fragments.GuessMaster
import com.example.tictactoe.fragments.MemoryMatch
import com.example.tictactoe.fragments.RockPaperScissor
import com.example.tictactoe.fragments.TickTacToe

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        TickTacToe(),
        GuessMaster(),
        RockPaperScissor(),
        MemoryMatch()
    )

    private val pageTitles = listOf(
        "Tic Tac Toe",
        "Guess Master",
        "Rock Paper Scissor",
        "Memory Match"
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getPageTitle(position: Int): String = pageTitles[position]
}