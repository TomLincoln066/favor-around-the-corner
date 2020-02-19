package com.tom.helper.proprogresseditfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.tom.helper.R
import com.tom.helper.databinding.FragmentProposalEditBinding
import com.tom.helper.databinding.FragmentProposalProgressEditBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.source.Proposal
import com.tom.helper.source.ProposalProgressContent

/**
 * A simple [Fragment] subclass.
 */
class ProposalProgressEditFragment : Fragment() {


    private val viewModel by viewModels<ProposalProgressEditViewModel> { getVmFactory(ProposalProgressEditFragmentArgs.fromBundle(arguments!!).proposal) }

//    getVmFactory(ProposalProgressEditFragmentArgs.fromBundle(arguments!!).task)
//    private val viewModel by viewModels<ProposalProgressEditViewModel> { getVmFactory() }

    private lateinit var proposalProgressContent: ProposalProgressContent
    private lateinit var proposal: Proposal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        proposal = requireArguments().get("proposal") as Proposal

        val binding = FragmentProposalProgressEditBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
//        binding.proposal = proposal


        viewModel.shouldNavigateBackToProposalProgressDisplayFragment.observe(this, Observer {
            if (it) {
                findNavController().navigate(ProposalProgressEditFragmentDirections.actionGlobalProProgressFragment(proposal))
            }
//            (activity as MainActivity).navigationView.selectedItemId = R.id.fragment_home
        })




        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_proposal_progress_edit, container, false)
        return binding.root
    }


}
