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
import com.google.firebase.storage.FirebaseStorage
import com.tom.helper.GlideApp
import com.tom.helper.databinding.FragmentJobDetailBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.homefragment.HomeViewModel
import com.tom.helper.source.Task
import kotlinx.android.synthetic.main.fragment_job_detail.*
import android.R.drawable
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.google.firebase.storage.StorageReference
import com.tom.helper.HelperApplication
import com.tom.helper.MainActivity


/**
 * A simple [Fragment] subclass.
 */
class JobDetailFragment : Fragment() {


//    var imageView: ImageView? = null
//
//    // WORKING CODE!
//    val storage = FirebaseStorage.getInstance()
//    // Create a reference to a file from a Google Cloud Storage URI
//    val gsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/project-help-db920.appspot.com/o/gift.jpg?alt=media&token=756aa4c2-1d6d-4b24-88fa-3c85a84dd083")
//
//    val GA = GlideApp.with(context!!).load(gsReference).into(imageView!!)



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

        binding.isTaskOwner = task.userId == HelperApplication.user.id

        binding.buttonJobDetailSendProposal.setOnClickListener {


            //requireArguments().get("task") as Task) : handle getting the task argument from fragment_job_detail * should try navArg<>() instead
            findNavController().navigate(JobDetailFragmentDirections.actionGlobalProposalEditFragment(task))
//            findNavController().navigate(JobDetailFragmentDirections.actionGlobalProposalEditFragment(navArgs<Task>()))

        }



        viewModel.getTasksResult()

        (activity as MainActivity).setLogo(MainActivity.EnumCheck.JOBDETAILS)

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_job_detail, container, false)
        return binding.root
    }




}
