package com.tom.helper.jobdetailfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.InverseMethod
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.tom.helper.databinding.FragmentJobDetailBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.homefragment.HomeViewModel
import com.tom.helper.source.Task

/**
 * A simple [Fragment] subclass.
 */
class JobDetailFragment : Fragment() {


    private val viewModel by viewModels<JobDetailViewModel> { getVmFactory() }

    private lateinit var task: Task

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        task  = requireArguments().get("task") as Task


        val binding = FragmentJobDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.task = task
        binding.buttonJobDetailSendProposal.setOnClickListener {


            //requireArguments().get("task") as Task) : handle getting the task argument from fragment_job_detail * should try navArg<>() instead
            findNavController().navigate(JobDetailFragmentDirections.actionGlobalProposalEditFragment(task))
//            findNavController().navigate(JobDetailFragmentDirections.actionGlobalProposalEditFragment(navArgs<Task>()))

        }



        viewModel.getTasksResult()

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_job_detail, container, false)
        return binding.root
    }




}
