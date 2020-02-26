package com.tom.helper.postrequest


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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.Continuation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tom.helper.MainActivity
import com.tom.helper.databinding.FragmentPostRequestBinding
import com.tom.helper.ext.getVmFactory
import kotlinx.android.synthetic.main.fragment_post_request.*
import kotlinx.android.synthetic.main.item_request.*
import java.util.*
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.protobuf.compiler.PluginProtos
import com.tom.helper.HelperApplication
import com.tom.helper.R
import com.tom.helper.source.Task
import kotlinx.android.synthetic.main.fragment_pro_progress.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 */
class PostRequestFragment : Fragment() {

    lateinit var binding : FragmentPostRequestBinding

    // photo_v2
    private val REQUEST_TAKE_PHOTO = 1



    //  photo

    private val MY_PERMISSIONS_CAMERA =20
    private val TAKE_PHOTO_REQUEST =30


    lateinit var currentPhotoPath: String


    //  storage
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


//    var mStorageRef: StorageReference = FirebaseStorage.getInstance().getReference("")
//    lateinit var storageReference: StorageReference


    /**
     * Lazily initialize our [HomeViewModel].
     */
    private val viewModel by viewModels<PostRequestViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val binding = FragmentPostRequestBinding.inflate(inflater, container, false)
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_request ,container, false)


        //handle changing the title while selecting PostRequestFragment
        (activity as MainActivity).setLogo(MainActivity.EnumCheck.POSTREQUEST)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //  storage

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        binding.btnChooseImage.setOnClickListener {
            launchGallery()
        }

        binding.btnCamera.setOnClickListener {
            loadCamera()
//            dispatchTakePictureIntent()
        }

//        binding.btnUploadImage.setOnClickListener {
//            uploadImage()
//        }


        //try to handle when button_post_request_send is clicked in fragment_post_request.xml is pressed, will navigate to Home Fragment
//        viewModel.shouldNavigateToHomeFragment.observe(this, Observer {
//            it?.let {
//
//                findNavController().navigate(PostRequestFragmentDirections.actionGlobalHomeFragment())
//
//                viewModel.doneNavigatingToHomeFragment()
//
//            }
//        })


        viewModel.shouldNavigateToHomeFragment.observe(this, Observer {
            if (it) {
                findNavController().navigate(PostRequestFragmentDirections.actionGlobalHomeFragment())
            }
//            (activity as MainActivity).navigationView.selectedItemId = R.id.fragment_home
        })
//        viewModel.taskPictureUri1.observe(this, Observer {
//            Log.d("", "taskPictureUri1 = $it")
//        })

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_post_request, container, false)
        return binding.root


    }


    // send some mock data to fireBase
//    fun sendNewRequest() {
//
//
//        val task = FirebaseFirestore.getInstance().collection("task")
//
//        val document = task.document()
//
//        val data = hashMapOf(
//            "task_provider" to "Tom",
//            "task_title" to "我的電腦crashes \n" +
//                    "Help me!!! \"",
//            "task_content" to " 我的電腦突然沒辦法開機，徵求勇士一名，酬勞另計。 ",
//            "task_createTime" to Calendar.getInstance().timeInMillis,
//            "task_price" to "20000",
//            "task_id" to document.id
//
//        )
//
//        document.set(data as Map<String, Any>)
//
//
//    }

    //photo_v2

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



    //storage choose and upload pictures

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }


    //handle choosing images and display into imageview image_preview in fragment_post_request
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data



            //photo_v2



            val bitmap = filePath?.getBitmap(binding.imagePreview.width, binding.imagePreview.height)
            binding.imagePreview.setImageBitmap(bitmap)
            viewModel.imageBitmap.value = bitmap



            try {
                Glide.with(this).load(filePath).into(image_preview)

                viewModel.taskPictureUri1.value =filePath
                Log.d("","viewModel.taskPictureUri1.value =filePath")
            } catch (e: IOException) {
                Log.d("","${e.printStackTrace()}")
                e.printStackTrace()
            }
        }



        //photo_v2


        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }
            val bitmap = photoURI?.getBitmap(binding.imagePreview.width, binding.imagePreview.height)
            binding.imagePreview.setImageBitmap(bitmap)
            viewModel.imageBitmap.value = bitmap
        }






        //photo

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
                .into(image_preview)
        }





    }

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


//    private fun addUploadRecordToDb(uri: String) {
//        val db = FirebaseFirestore.getInstance()
//
//
//        val data = HashMap<String, Any>()
//        data["imageUrl"] = uri
//
//        db.collection("posts")
//
//            .add(data)
//            .addOnSuccessListener { documentReference ->
//                Toast.makeText(context, "Saved to DB", Toast.LENGTH_LONG).show()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(context, "Error saving to DB", Toast.LENGTH_LONG).show()
//            }
//    }
//
//    private fun uploadImage() {
//        if (filePath != null) {
//            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
//            val uploadTask = ref?.putFile(filePath!!)
//
//            val urlTask =
//                uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, com.google.android.gms.tasks.Task<Uri>> { task ->
//                    if (!task.isSuccessful) {
//                        task.exception?.let {
//                            throw it
//                        }
//                    }
//                    return@Continuation ref.downloadUrl
//                })?.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val downloadUri = task.result
//                        addUploadRecordToDb(downloadUri.toString())
//                    } else {
//                        // Handle failures
//                    }
//                }?.addOnFailureListener {
//
//                }
//        } else {
//            Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
//        }
//    }


    //photo


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



//    val storageRef = HelperApplication.container.fireStorage.reference
//    val byteArrayOutput = ByteArrayOutputStream()
//    Logger.i("imageBitmap.byteCount=${imageBitmap.byteCount}")
//    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutput)
//    val bytes = byteArrayOutput.toByteArray()
//    val uploadTask = storageRef.child(bookID + "/cover.jpg").putBytes(bytes)





}

