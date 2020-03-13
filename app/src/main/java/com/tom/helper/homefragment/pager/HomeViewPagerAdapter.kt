package com.tom.helper.homefragment.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tom.helper.homefragment.HomeFragment


class HomeViewPagerAdapter (fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager){
    override fun getItem(position: Int): Fragment {
        when(position){
            0-> return HomeFragment()
            1-> return taskOnGoingFragment()

            else -> return HomeFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }




}