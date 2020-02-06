package com.tom.helper.proposaleditfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tom.helper.R
import com.tom.helper.databinding.FragmentProposalEditBinding
import com.tom.helper.source.Task

/**
 * A simple [Fragment] subclass.
 */
class ProposalEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentProposalEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.buttonProposalSend.setOnClickListener {

            //requireArguments().get("task") as Task) : handle getting the task argument from fragment_job_detail * should try navArg<>() instead
            findNavController().navigate(
                ProposalEditFragmentDirections.actionGlobalProposalListFragment(

                )
            )
//            findNavController().navigate(JobDetailFragmentDirections.actionGlobalProposalEditFragment(navArgs<Task>()))

        }


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_proposal_edit, container, false)
        return binding.root
    }


}
