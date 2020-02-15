package com.tom.helper.myproposal


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.tom.helper.R
import com.tom.helper.databinding.FragmentMyProposalsBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.proposallistfragment.ProposalListFragmentArgs
import com.tom.helper.proposallistfragment.ProposalListViewModel

/**
 * A simple [Fragment] subclass.
 */
class MyProposalsFragment : Fragment() {


    private val viewModel by viewModels<ProposalListViewModel>{getVmFactory(ProposalListFragmentArgs.fromBundle(arguments!!).task)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding =FragmentMyProposalsBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
//        binding.viewModel = viewModel

        binding.myProposalsRecycler.layoutManager = LinearLayoutManager(context)
        binding.myProposalsRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

//        binding.myProposalsRecycler.adapter =
//            MyProposalsAdapter(MyProposalsAdapter.OnClickListener {
//
//            },viewModel)


//        viewModel.addProposal()
//        viewModel.getProposalsResult()
//        viewModel.getProposalsOfMineResult()







        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_proposal_list, container, false)
        return binding.root
    }


}
