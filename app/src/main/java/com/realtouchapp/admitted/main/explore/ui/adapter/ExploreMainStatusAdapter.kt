package com.realtouchapp.admitted.main.explore.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.realtouchapp.admitted.main.explore.ui.fragment.ExtracurricularsMainFragment
import com.realtouchapp.admitted.main.explore.ui.fragment.SchoolMainFragment

/*
    作者: RobertChou
    時間: 2023/3/16 上午 01:45
    信箱: didi31139@gmail.com
    說明:
*/
class ExploreMainStatusAdapter (fragmentActivity: FragmentActivity, private var totalCount: Int) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SchoolMainFragment()
            1 -> ExtracurricularsMainFragment()
            else -> ExtracurricularsMainFragment()
        }
    }
}