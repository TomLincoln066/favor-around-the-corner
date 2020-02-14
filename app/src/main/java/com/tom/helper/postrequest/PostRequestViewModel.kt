package com.tom.helper.postrequest

import android.net.Uri
import android.widget.Toast
import java.util.concurrent.TimeUnit
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Continuation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.tom.helper.HelperApplication
import com.tom.helper.HelperApplication.Companion.context
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Task
import com.tom.helper.source.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.newFixedThreadPoolContext
import java.text.SimpleDateFormat
import java.util.*

class PostRequestViewModel(private val repository: HelperRepository) : ViewModel() {


    //  storage

    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    val taskTitle = MutableLiveData<String>()
    val taskProvider = MutableLiveData<String>()
    val taskPrice = MutableLiveData<Long>()
    val taskContent = MutableLiveData<String>()
    val taskStatus = -1


    val taskPictureUri1 = MutableLiveData<Uri>()


    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    //try to handle when button_post_request_send is clicked in fragment_post_request.xml is pressed, will navigate to Home Fragment
    val shouldNavigateToHomeFragment = MutableLiveData<Boolean>()

    init {
        shouldNavigateToHomeFragment.value = false
    }

    //get editText's data and send out to firebase
//    fun submitTask() {
//
//        _error.value = null
//
//        //check whether taskTitle.value is not valid
//        if (taskContent.value == null || taskContent.value?.isEmpty() == true) {
//            _error.value = "Task Content cannot be empty"
//            return
//        }
//        if (taskTitle.value == null || taskTitle.value?.isEmpty() == true) {
//            _error.value = "Task Title cannot be empty"
//            return
//        }
//        if (taskTitle.value == null || taskTitle.value?.isEmpty() == true) {
//            _error.value = "Task Title not complete"
//            return
//        }
//
//        val db = FirebaseFirestore.getInstance()
//
//        val task = FirebaseFirestore.getInstance().collection("tasks")
//
//        val user = FirebaseAuth.getInstance().currentUser
////        val user = FirebaseAuth.getInstance().currentUser
//
//        if (user == null || user.displayName == null || user.email == null) {
//            return
//        }
//        val userCurrent = User(user.uid,user.displayName!!,user.email!!,0,0L)
////        val userCurrent = user.uid
//
//        val document = task.document()
//
//
//
//        val data = Task(
//            document.id,
//            taskPrice.value!!,
//            System.currentTimeMillis(),
//            taskTitle.value!!,
//            taskContent.value!!,
//            userCurrent,
//            taskProvider.value!!,
//            taskStatus,
//            listOf(),
//            taskPictureUri1.value.toString()
//        )
//
////        document.set(data as Map<String, Any>)
//        document.set(data)
//            //try to handle when button_post_request_send in fragment_post_request.xml is pressed, will show toast that says "Add Success"
//            .addOnSuccessListener {
//                shouldNavigateToHomeFragment.value = true
//                Toast.makeText(HelperApplication.context, "Add Success", Toast.LENGTH_SHORT).show()
//            }
//
//
//
//
//
//    }


    fun submitTask() {


        _error.value = null

        //check whether taskTitle.value is not valid
        if (taskContent.value == null || taskContent.value?.isEmpty() == true) {
            _error.value = "Task Content cannot be empty"
            return
        }
        if (taskTitle.value == null || taskTitle.value?.isEmpty() == true) {
            _error.value = "Task Title cannot be empty"
            return
        }
        if (taskTitle.value == null || taskTitle.value?.isEmpty() == true) {
            _error.value = "Task Title not complete"
            return
        }

        val db = FirebaseFirestore.getInstance()

        val task = FirebaseFirestore.getInstance().collection("tasks")

        val user = FirebaseAuth.getInstance().currentUser
//        val user = FirebaseAuth.getInstance().currentUser

        if (user == null || user.displayName == null || user.email == null) {
            return
        }
        val userCurrent = User(user.uid, user.displayName!!, user.email!!, 0, 0L)
//        val userCurrent = user.uid

        val document = task.document()

        storageReference = FirebaseStorage.getInstance().reference

        val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
        val uploadTask = ref?.putFile(taskPictureUri1.value!!)

        val urlTask =
            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, com.google.android.gms.tasks.Task<Uri>> { ImageTask ->
                if (!ImageTask.isSuccessful) {
                    ImageTask.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { CompleteTask ->
                if (CompleteTask.isSuccessful) {
                    val downloadUri = CompleteTask.result

                    val data = Task(
                        document.id,
                        taskPrice.value!!,
                        System.currentTimeMillis(),
                        taskTitle.value!!,
                        taskContent.value!!,
                        userCurrent,
                        taskProvider.value!!,
                        taskStatus,
                        listOf(),
                        downloadUri.toString(),
                        user.uid
                    )

                    document.set(data)
                        //try to handle when button_post_request_send in fragment_post_request.xml is pressed, will show toast that says "Add Success"
                        .addOnSuccessListener {
                            shouldNavigateToHomeFragment.value = true
                            Toast.makeText(
                                HelperApplication.context,
                                "Add Success",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
            }?.addOnFailureListener {

            }


//        document.set(data as Map<String, Any>)


    }


    fun errorReceived() {

        _error.value = null
    }


    //try to handle when button_post_request_send is clicked in fragment_post_request.xml is pressed, will navigate to Home Fragment
//    private val _shouldNavigateToHomeFragment = MutableLiveData<Boolean>()
//    val shouldNavigateToHomeFragment: LiveData<Boolean>
//        get() = _shouldNavigateToHomeFragment
//
//    fun navToHomeFragment() {
//        _shouldNavigateToHomeFragment.value = true
//    }
//
//    fun doneNavigatingToHomeFragment() {
//        _shouldNavigateToHomeFragment.value = null
//    }


    // handle taskPrice input type convert problem( Long to String and String to Long)
    @InverseMethod("convertLongToString")
    fun convertStringToLong(value: String): Long {
        return try {
            value.toLong().let {
                when (it) {
                    0L -> 0
                    else -> it
                }
            }
        } catch (e: NumberFormatException) {
            1
        }
    }

    fun convertLongToString(value: Long): String {
        return value.toString()
    }


    private fun convertLongToDateString(systemTime: Long): String {
        return SimpleDateFormat("MMM-dd-yyyy HH:mm:ss")
            .format(systemTime).toString()
    }


    fun convertLongToTimeAgo(systemTime: Long): String {
        val currentTime = Calendar.getInstance().timeInMillis
        val diff = currentTime - systemTime
        var minutes = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)
        val hours = minutes / 60
        val t = diff / hours
        return "$t + 分鐘前"
    }

}


