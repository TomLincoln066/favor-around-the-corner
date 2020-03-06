package com.tom.helper.proprogresseditfragment


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tom.helper.HelperApplication
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


    lateinit var binding : FragmentProposalProgressEditBinding



    private val viewModel by viewModels<ProposalProgressEditViewModel> { getVmFactory(ProposalProgressEditFragmentArgs.fromBundle(arguments!!).proposal) }


//    private val viewModel by viewModels<ProposalProgressEditViewModel> { getVmFactory() }

    private lateinit var proposalProgressContent: ProposalProgressContent
    private lateinit var proposal: Proposal


    // photo_v2
    private val REQUEST_TAKE_PHOTO = 1


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

//        val binding = FragmentProposalProgressEditBinding.inflate(inflater,container,false)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_proposal_progress_edit, container, false)




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



            //photo_v2

            val bitmap = filePath?.getBitmap(binding.imageViewPreview.width, binding.imageViewPreview.height)
            binding.imageViewPreview.setImageBitmap(bitmap)
            viewModel.imageBitmap.value = bitmap



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


    fun Uri.getBitmap(width: Int, height: Int): Bitmap? {
        var rotatedDegree = 0
        var stream = HelperApplication.context.contentResolver.openInputStream(this)
        /** GET IMAGE ORIENTATION */
        if(stream != null) {
            val exif = ExifInterface(stream)
            rotatedDegree = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL).fromExifInterfaceOrientationToDegree()
            stream.close()
        }
        /** GET IMAGE SIZE */
        stream = HelperApplication.context.contentResolver.openInputStream(this)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(stream, null,options)
        try {
            stream?.close()
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return null
        }
        // The resulting width and height of the bitmap
        if(options.outWidth == -1 || options.outHeight == -1) return null
        var bitmapWidth = options.outWidth.toFloat()
        var bitmapHeight = options.outHeight.toFloat()
        if (rotatedDegree == 90) {
            // Side way -> options.outWidth is actually HEIGHT
            //          -> options.outHeight is actually WIDTH
            bitmapWidth = options.outHeight.toFloat()
            bitmapHeight = options.outWidth.toFloat()
        }
        var scale = 1
        while(true) {
            if(bitmapWidth / 2 < width || bitmapHeight / 2 < height)
                break;
            bitmapWidth /= 2
            bitmapHeight /= 2
            scale *= 2
        }
        val finalOptions = BitmapFactory.Options()
        finalOptions.inSampleSize = scale
        stream = HelperApplication.context.contentResolver.openInputStream(this)
        val bitmap = BitmapFactory.decodeStream(stream, null, finalOptions)
        try {
            stream?.close()
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return null
        }
        val matrix = Matrix()
        if (rotatedDegree != 0) {
            matrix.preRotate(rotatedDegree.toFloat())
        }
        var bmpWidth = 0
        try {
            if (bitmap == null) {
                return null
            }
            bmpWidth = bitmap.width
        } catch (e: Exception) {
            return null
        }
        var adjustedBitmap = bitmap
        if (bmpWidth > 0) {
            adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        return adjustedBitmap
    }


    fun Int.fromExifInterfaceOrientationToDegree(): Int {
        return when(this) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }




}
