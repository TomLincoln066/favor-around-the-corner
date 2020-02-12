package com.tom.helper.postrequest


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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
import com.tom.helper.source.Task
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class PostRequestFragment : Fragment() {


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

        val binding = FragmentPostRequestBinding.inflate(inflater, container, false)


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

            try {
                Glide.with(this).load(filePath).into(image_preview)

                viewModel.taskPictureUri1.value =filePath
                Log.d("","viewModel.taskPictureUri1.value =filePath")
            } catch (e: IOException) {
                Log.d("","${e.printStackTrace()}")
                e.printStackTrace()
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


}

