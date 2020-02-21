package com.tom.helper.proposallistfragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.helper.databinding.FragmentProposalListBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.rankinglist.RankingListFragmentDirections

/**
 * A simple [Fragment] subclass.
 */
class ProposalListFragment : Fragment() {


    private val viewModel by viewModels<ProposalListViewModel>{getVmFactory(ProposalListFragmentArgs.fromBundle(arguments!!).task)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding =FragmentProposalListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.proposalListRecycler.layoutManager = LinearLayoutManager(context)
//        binding.proposalListRecycler.addItemDecoration(
//            DividerItemDecoration(
//                context,
//                LinearLayoutManager.VERTICAL
//            )
//        )

        binding.proposalListRecycler.adapter =
            ProposalListRecyclerAdapter(ProposalListRecyclerAdapter.OnClickListener {
                viewModel.clickNavigateToProProgressFragment(it)
                //            Logger.d("click, it=$it")
                //            viewModel.delete(it)
            },viewModel)







        //try to handle when button_to_task_progress_sheet in item_proposal.xml is pressed, will navigate to ProProgressViewModel(see ProposalListViewModel.kt)
        viewModel.shouldNavigateToProProgressFragment.observe(this, Observer {
            Log.d("shouldNavigateToProProgressFragment","${it}")
            it?.let {

                findNavController().navigate(
                    ProposalListFragmentDirections.actionGlobalProProgressFragment(
                        it
                    )
                )

                viewModel.doneNavigatingToProProgressFragment()

            }
        })













//        viewModel.addProposal()
        viewModel.getProposalsResult()





        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_proposal_list, container, false)
        return binding.root
    }


}
