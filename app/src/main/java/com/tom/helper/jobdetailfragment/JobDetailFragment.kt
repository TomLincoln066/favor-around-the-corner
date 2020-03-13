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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.storage.StorageReference
import com.tom.helper.HelperApplication
import com.tom.helper.MainActivity
import com.tom.helper.source.Message


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

    private val _message = MutableLiveData<Message>()

    val message: LiveData<Message>
        get() = _message





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



            findNavController().navigate(JobDetailFragmentDirections.actionGlobalProposalEditFragment(task))


        }



        viewModel.shouldNavigateToChatListFragment.observe(this, Observer {
            it?.let { task ->

                when (task.userId == HelperApplication.user.id) {

                    true -> {

                        findNavController().navigate(

                            JobDetailFragmentDirections.actionGlobalChatRoomFragment(it)
                        )
                        viewModel.doneNavigatingToChatList()



                    }



                    false -> {


                        findNavController().navigate(

                            JobDetailFragmentDirections.actionGlobalChatRoomFragment(it)
                        )
                        viewModel.doneNavigatingToChatList()




                    }


                }


            }
        })







        viewModel.getTasksResult()

        (activity as MainActivity).setLogo(MainActivity.EnumCheck.JOBDETAILS)

        // Inflate the layout for this fragment

        return binding.root
    }




}
