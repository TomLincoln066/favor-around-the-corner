package com.tom.helper.proprogresseditfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tom.helper.R
import com.tom.helper.databinding.FragmentProposalEditBinding
import com.tom.helper.databinding.FragmentProposalProgressEditBinding
import com.tom.helper.source.Proposal

/**
 * A simple [Fragment] subclass.
 */
class ProposalProgressEditFragment : Fragment() {


//    private val viewModel by viewModels<JobDetailViewModel> { getVmFactory() }

    private lateinit var proposal: Proposal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


//        proposal = requireArguments().get("proposal") as Proposal

        val binding = FragmentProposalProgressEditBinding.inflate(inflater,container,false)


        binding.lifecycleOwner = this
//        binding.viewModel = viewModel
//        binding.proposal = proposal




        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_proposal_progress_edit, container, false)
        return binding.root
    }


}
