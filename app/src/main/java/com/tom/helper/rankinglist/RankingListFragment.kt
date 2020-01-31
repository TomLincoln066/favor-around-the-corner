package com.tom.helper.rankinglist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tom.helper.MainActivity

import com.tom.helper.R

/**
 * A simple [Fragment] subclass.
 */
class RankingListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //handle changing the title while selecting RankingListFragment
        (activity as MainActivity).setLogo(MainActivity.EnumCheck.RANKINGLIST)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking_list, container, false)
    }


}
