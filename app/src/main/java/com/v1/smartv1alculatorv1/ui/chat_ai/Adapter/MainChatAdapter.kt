package com.v1.smartv1alculatorv1.ui.chat_ai.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.v1.smartv1alculatorv1.ui.chat_ai.Fragment.ChatFragment
import com.v1.smartv1alculatorv1.ui.chat_ai.Fragment.HistoryFragment


// Adapter for the main ViewPager2
class MainChatAdapter  // Constructor
    (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    // Create the fragment based on position
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChatFragment()
            1 -> HistoryFragment()

            else -> ChatFragment()// Return null for invalid position
        }
    }

    // Return the total number of fragments
    override fun getItemCount(): Int {
        return 2
    }
}