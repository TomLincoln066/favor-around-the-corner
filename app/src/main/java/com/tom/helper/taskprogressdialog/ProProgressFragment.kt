package com.tom.helper.taskprogressdialog


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.tom.helper.R
import com.tom.helper.databinding.FragmentProProgressBinding
import com.tom.helper.source.Proposal

/**
 * A simple [Fragment] subclass.
 */
class ProProgressFragment : Fragment() {

//    private val viewModel by viewModels<ProposalEditViewModel> { getVmFactory(ProposalEditFragmentArgs.fromBundle(arguments!!).task) }

    private lateinit var proposal: Proposal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        proposal  = requireArguments().get("proposal") as Proposal

        val binding = FragmentProProgressBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.buttonEditProgressItem.setOnClickListener {

            findNavController().navigate(ProProgressFragmentDirections.actionGlobalProposalProgressEditFragment())

        }

//        binding.viewModel = viewModel


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_pro_progress, container, false)

        return binding.root


    }


}
