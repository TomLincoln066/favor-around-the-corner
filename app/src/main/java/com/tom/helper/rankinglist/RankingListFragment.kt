package com.tom.helper.rankinglist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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


    private val viewModel by viewModels<RankingListViewModel>{getVmFactory()}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =FragmentRankingListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.manageMyTasksRecycler.layoutManager = LinearLayoutManager(context)
        binding.manageMyTasksRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.manageMyTasksRecycler.adapter =
            RankingRecyclerAdapter(RankingRecyclerAdapter.OnClickListener {
                //            Logger.d("click, it=$it")
                //            viewModel.delete(it)
            },viewModel)


//        viewModel.prepareTaskTest()
        viewModel.getTasksOfMineResult()




        //try to handle when button_mission_detail_proposal_total in item_request.xml is pressed, will navigate to ProposalListFragment(see HomeViewModel.kt)
        viewModel.shouldNavigateToProposalList.observe(this, Observer {
            it?.let {

                findNavController().navigate(
                    RankingListFragmentDirections.actionGlobalProposalListFragment(
                        it
                    )
                )

                viewModel.doneNavigatingToProposalList()

            }
        })











        //handle changing the title while selecting RankingListFragment
        (activity as MainActivity).setLogo(MainActivity.EnumCheck.RANKINGLIST)



        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_ranking_list, container, false)
        return binding.root
    }



}
