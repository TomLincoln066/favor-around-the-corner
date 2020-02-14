package com.tom.helper.rankinglist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.helper.MainActivity

import com.tom.helper.R
import com.tom.helper.databinding.FragmentRankingListBinding
import com.tom.helper.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class RankingListFragment : Fragment() {


//    private val viewModel by viewModels<RankingListViewModel>{getVmFactory()}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =FragmentRankingListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
//        binding.viewModel = viewModel

//        binding.rankingListRecycler.layoutManager = LinearLayoutManager(context)
//        binding.rankingListRecycler.addItemDecoration(
//            DividerItemDecoration(
//                context,
//                LinearLayoutManager.VERTICAL
//            )
//        )
//
//        binding.rankingListRecycler.adapter =
//            RankingRecyclerAdapter(RankingRecyclerAdapter.OnClickListener {
//                //            Logger.d("click, it=$it")
//                //            viewModel.delete(it)
//            })


//        viewModel.addRankUser()


        //handle changing the title while selecting RankingListFragment
        (activity as MainActivity).setLogo(MainActivity.EnumCheck.RANKINGLIST)



        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_ranking_list, container, false)
        return binding.root
    }



}
