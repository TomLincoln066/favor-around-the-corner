package com.tom.helper.proprogresseditfragment


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ProposalProgressEditFragment : Fragment() {


    lateinit var binding : FragmentProposalProgressEditBinding

    // camera
    lateinit var currentPhotoPath: String
    private val MY_PERMISSIONS_CAMERA =20
    private val TAKE_PHOTO_REQUEST =30

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


        binding.imageButtonOpenCamera.setOnClickListener{
            loadCamera()
        }




        (activity as MainActivity).setLogo(MainActivity.EnumCheck.PROPOSALPROGRESSEDIT)


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_proposal_progress_edit, container, false)
        return binding.root
    }

        // camera


    private var photoURI: Uri? = null
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context!!,
                        "com.tom.helper.fileprovider",
//                        "${applicationId}.fileprovider",
                        it
                    )
                    this.photoURI = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }






    //photo
    private fun loadCamera() {
        val loadCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (ContextCompat.checkSelfPermission(
                HelperApplication.instance,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (activity as MainActivity) ,
                    Manifest.permission.CAMERA
                )
            ) {
                AlertDialog.Builder(context!!)
                    .setMessage("需要開啟相機權限")
                    .setPositiveButton("前往設定") { _, _ ->
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.CAMERA
                            ),
                            MY_PERMISSIONS_CAMERA
                        )
                    }
                    .setNegativeButton("NO") { _, _ -> }
                    .show()
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA
                    ),
                    MY_PERMISSIONS_CAMERA
                )
            }
        } else {

            dispatchTakePictureIntent()
//            startActivityForResult(loadCameraIntent, TAKE_PHOTO_REQUEST)
        }
    }






    //camera



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



        //camera


        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }
            val bitmap = photoURI?.getBitmap(binding.imageViewPreview.width, binding.imageViewPreview.height)
            binding.imageViewPreview.setImageBitmap(bitmap)
            viewModel.imageBitmap.value = bitmap
        }


        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("Photo", "requestCode == TAKE_PHOTO_REQUEST")
            val pic: Bitmap = data!!.extras?.get("data") as Bitmap
            val uri = Uri.parse(
                MediaStore.Images.Media.insertImage(
                    context!!.contentResolver,
                    pic,
                    null,
                    null
                )
            )
            val newBitmap = uri.getBitmap(100, 100)
            val newUri = Uri.parse(
                MediaStore.Images.Media.insertImage(
                    context!!.contentResolver,
                    newBitmap,
                    "${System.currentTimeMillis()}",
                    null
                )
            )
//            val uri = Uri.fromFile(newSdcardTempFile)
//            binding.imageUpdate.setImageBitmap(pic)
            viewModel.taskPictureUri1.value = uri
            Glide.with(this).load(uri)
                .apply(RequestOptions().centerCrop())
                .into(imageView_preview)
        }













    }






    //camera


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (permissionsItem in permissions) {
                        Log.d("WillCamera", "permissions allow : $permissions")
                    }
                    loadCamera()
                } else {
                    for (permissionsItem in permissions) {
                        Log.d("WillCamera", "permissions reject : $permissionsItem")
                    }
                }
                return
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
