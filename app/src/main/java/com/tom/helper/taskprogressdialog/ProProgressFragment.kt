package com.tom.helper.taskprogressdialog


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.helper.MainActivity

import com.tom.helper.R
import com.tom.helper.databinding.FragmentProProgressBinding
import com.tom.helper.ext.getVmFactory

import com.tom.helper.source.Proposal
import com.tom.helper.source.ProposalProgressContent
import com.tom.helper.source.Task

/**
 * A simple [Fragment] subclass.
 */
class ProProgressFragment : Fragment() {

    private val viewModel by viewModels<ProProgressViewModel> {
        getVmFactory(
            ProProgressFragmentArgs.fromBundle(
                arguments!!
            ).proposal
        )
    }


    private lateinit var proposalProgressContent: ProposalProgressContent


    private lateinit var proposal: Proposal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        proposal = requireArguments().get("proposal") as Proposal

        val binding = FragmentProProgressBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.progressItemRecyclerView.layoutManager = LinearLayoutManager(context)


        val proProgressRecyclerAdapter =
            ProProgressRecyclerAdapter(ProProgressRecyclerAdapter.OnClickListener {

            }, viewModel)

        binding.progressItemRecyclerView.adapter = proProgressRecyclerAdapter


        //  this is replaced by val ableToNavToProgress in ProProgressViewModel.kt
        binding.buttonEditProgressItem.setOnClickListener {

            findNavController().navigate(
                ProProgressFragmentDirections.actionGlobalProposalProgressEditFragment(
                    proposal
                )
            )


        }

        binding.buttonNavBackProposalList.setOnClickListener {

            findNavController().navigateUp()

        }


        //snapshot proProgressItem list

        viewModel.getProposalItemsLiveSnapShot()
        viewModel.proposalProgressContents.observe(this, Observer {
            proProgressRecyclerAdapter.submitList(it)
        })






        viewModel.shouldNavigateToProfilePage.observe(this, Observer {
            if (it) {
                findNavController().navigate(ProProgressFragmentDirections.actionGlobalProfileFragment())
            }

        })








        (activity as MainActivity).setLogo(MainActivity.EnumCheck.PROPOSALPROGRESSLIST)


        viewModel.getCurrentUserData()


        return binding.root


    }


}
