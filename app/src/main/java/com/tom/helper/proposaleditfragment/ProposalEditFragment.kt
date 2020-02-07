package com.tom.helper.proposaleditfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tom.helper.databinding.FragmentProposalEditBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.source.Task

/**
 * A simple [Fragment] subclass.
 */
class ProposalEditFragment : Fragment() {

    private val viewModel by viewModels<ProposalEditViewModel> { getVmFactory(ProposalEditFragmentArgs.fromBundle(arguments!!).task) }

    private lateinit var task: Task


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        task  = requireArguments().get("task") as Task



        val binding = FragmentProposalEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //observe when shouldNavigateToProposalListFragment change from false to true then proceed findNavController().navigate(ProposalEditFragmentDirections.actionGlobalProposalListFragment())
        viewModel.shouldNavigateToProposalListFragment.observe(this, Observer {
            if(it){
                findNavController().navigate(ProposalEditFragmentDirections.actionGlobalProposalListFragment(task))
            }
        })



        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_proposal_edit, container, false)
        return binding.root
    }


}


//binding.buttonProposalSend.setOnClickListener {
//
//    //requireArguments().get("task") as Task) : handle getting the task argument from fragment_job_detail * should try navArg<>() instead
//    // findNavController().navigate(JobDetailFragmentDirections.actionGlobalProposalEditFragment(navArgs<Task>()))



//    findNavController().navigate(
//        ProposalEditFragmentDirections.actionGlobalProposalListFragment(
//
//        )
//    )
//
//
//}