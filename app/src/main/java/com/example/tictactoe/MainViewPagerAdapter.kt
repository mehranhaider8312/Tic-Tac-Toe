package com.example.tictactoe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.tictactoe.fragments.GuessMaster
import com.example.tictactoe.fragments.MemoryMatch
import com.example.tictactoe.fragments.RockPaperScissor
import com.example.tictactoe.fragments.TickTacToe

class MainViewPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    val fragments = listOf(TickTacToe(), GuessMaster(), RockPaperScissor(), MemoryMatch())

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}