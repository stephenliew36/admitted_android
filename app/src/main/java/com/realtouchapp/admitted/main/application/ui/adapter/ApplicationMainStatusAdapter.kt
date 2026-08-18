package com.realtouchapp.admitted.main.application.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.realtouchapp.admitted.common.ui.fragment.WebViewFragment
import com.realtouchapp.admitted.main.application.ui.fragment.ApplicationsListFragment
import com.realtouchapp.admitted.main.consult.ui.fragment.ConsultMainFragment
import com.realtouchapp.admitted.main.explore.ui.fragment.ExtracurricularsMainFragment
import com.realtouchapp.admitted.main.explore.ui.fragment.SchoolMainFragment

/*
    作者: RobertChou
    時間: 2023/3/16 上午 01:45
    信箱: didi31139@gmail.com
    說明:
*/
class ApplicationMainStatusAdapter (fragmentActivity: FragmentActivity, private var totalCount: Int) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ApplicationsListFragment()
            1 -> WebViewFragment()
            else -> ConsultMainFragment()
        }
    }
}