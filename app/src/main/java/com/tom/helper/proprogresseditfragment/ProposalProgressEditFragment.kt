package com.tom.helper.proprogresseditfragment


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tom.helper.MainActivity

import com.tom.helper.R
import com.tom.helper.databinding.FragmentProposalEditBinding
import com.tom.helper.databinding.FragmentProposalProgressEditBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.source.Proposal
import com.tom.helper.source.ProposalProgressContent
import kotlinx.android.synthetic.main.fragment_proposal_progress_edit.*
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class ProposalProgressEditFragment : Fragment() {


    private val viewModel by viewModels<ProposalProgressEditViewModel> { getVmFactory(ProposalProgressEditFragmentArgs.fromBundle(arguments!!).proposal) }


//    private val viewModel by viewModels<ProposalProgressEditViewModel> { getVmFactory() }

    private lateinit var proposalProgressContent: ProposalProgressContent
    private lateinit var proposal: Proposal


    //upload pictures
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null






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
            it?.let {
                if (it) {
//                    findNavController().navigate(ProposalProgressEditFragmentDirections.actionGlobalProProgressFragment(proposal))
                    findNavController().navigateUp()
                }
            }
//            (activity as MainActivity).navigationView.selectedItemId = R.id.fragment_home
        })



        //  upload image

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        binding.imageButtonChooseImage.setOnClickListener {
            launchGallery()
        }

        (activity as MainActivity).setLogo(MainActivity.EnumCheck.PROPOSALPROGRESSEDIT)


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_proposal_progress_edit, container, false)
        return binding.root
    }




    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }



    //handle choosing images and display into imageview image_preview in fragment_proposal_progress_edit
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data

            try {
                Glide.with(this).load(filePath).into(imageView_preview)

                viewModel.taskPictureUri1.value =filePath
                Log.d("","viewModel.taskPictureUri1.value =filePath")
            } catch (e: IOException) {
                Log.d("","${e.printStackTrace()}")
                e.printStackTrace()
            }
        }

    }



}
