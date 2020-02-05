package com.tom.helper.jobdetailfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.tom.helper.databinding.FragmentJobDetailBinding
import com.tom.helper.source.Task

/**
 * A simple [Fragment] subclass.
 */
class JobDetailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val binding = FragmentJobDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.buttonJobDetailSendProposal.setOnClickListener {

            //requireArguments().get("task") as Task) : handle getting the task argument from fragment_job_detail * should try navArg<>() instead
            findNavController().navigate(JobDetailFragmentDirections.actionGlobalProposalEditFragment(requireArguments().get("task") as Task))
//            findNavController().navigate(JobDetailFragmentDirections.actionGlobalProposalEditFragment(navArgs<Task>()))

        }


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_job_detail, container, false)
        return binding.root
    }


}
