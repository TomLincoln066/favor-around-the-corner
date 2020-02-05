package com.tom.helper.proposallistfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.tom.helper.R
import com.tom.helper.databinding.FragmentProposalListBinding
import com.tom.helper.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class ProposalListFragment : Fragment() {


    private val viewModel by viewModels<ProposalListViewModel>{getVmFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding =FragmentProposalListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.proposalListRecycler.layoutManager = LinearLayoutManager(context)
        binding.proposalListRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.proposalListRecycler.adapter =
            ProposalListRecyclerAdapter(ProposalListRecyclerAdapter.OnClickListener {
                //            Logger.d("click, it=$it")
                //            viewModel.delete(it)
            })


        viewModel.addProposal()





        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_proposal_list, container, false)
        return binding.root
    }


}
