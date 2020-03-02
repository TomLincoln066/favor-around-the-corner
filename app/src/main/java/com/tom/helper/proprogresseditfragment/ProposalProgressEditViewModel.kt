package com.tom.helper.proprogresseditfragment

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.tom.helper.HelperApplication
import com.tom.helper.LoadApiStatus
import com.tom.helper.source.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*


class ProposalProgressEditViewModel(
    private val repository: HelperRepository,
    private val proposal: Proposal
) :
    ViewModel() {


    val proposalProgressItemContent = MutableLiveData<String>()
    val proposalProgressItemContentAccepted = false


    //upload image
    val taskPictureUri1 = MutableLiveData<Uri>()

    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null



    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status



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


    val shouldNavigateBackToProposalProgressDisplayFragment = MutableLiveData<Boolean>()


    init {
        shouldNavigateBackToProposalProgressDisplayFragment.value = false
    }


    //get editText's data and send out to FireBase
    fun submitProposalProgressItemContent() {

        _error.value = null

        _status.value = LoadApiStatus.LOADING

        //check whether proposalProgressItemContent.value is not valid
        if (proposalProgressItemContent.value == null || proposalProgressItemContent.value?.isEmpty() == true) {
            _error.value = "proposalProgressItemContent cannot be empty"
            return
        }

        //upload image
        storageReference = FirebaseStorage.getInstance().reference
        val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
        val uploadTask = ref?.putFile(taskPictureUri1.value!!)


        val proposalProgressItem =
            FirebaseFirestore.getInstance().collection("tasks").document(proposal.taskID)
                .collection("proposal")

        val document =
            proposalProgressItem.document(proposal.id).collection("progressItems").document()

        val user = FirebaseAuth.getInstance().currentUser!!
        val userCurrent = user.uid
        val userCurrent1 = User(user!!.uid, user.displayName!!, user.email!!, 0, 0L)
        val proposalId = proposal.id


        //upload image
        val urlTask =
            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { ImageTask ->
                if (!ImageTask.isSuccessful) {
                    ImageTask.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { CompleteTask ->
                if (CompleteTask.isSuccessful) {
                    val downloadUri = CompleteTask.result


                    val data = ProposalProgressContent(
                        proposalProgressItemContent.value!!,
                        -1,
                        false,
                        document.id,
                        System.currentTimeMillis(),
                        userCurrent,
                        userCurrent1,
                        null,
                        null,
                        proposalId, downloadUri.toString()
                    )

                    document.set(data)
                        .addOnFailureListener {
                            Log.i("EXCEPTIONX", "exc = ${it.message}")
                        }.addOnSuccessListener {

                            shouldNavigateBackToProposalProgressDisplayFragment.value = true
                            Toast.makeText(
                                HelperApplication.context,
                                "新增進度項目成功",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.i("SUCCESS", "SU")
                        }


                }
            }?.addOnFailureListener {

            }


    }


}


//
//fun submitProposalProgressItemContent() {
//
//    _error.value = null
//
//    //check whether proposalProgressItemContent.value is not valid
//    if (proposalProgressItemContent.value == null || proposalProgressItemContent.value?.isEmpty() == true) {
//        _error.value = "proposalProgressItemContent cannot be empty"
//        return
//    }
//
//
//
//
//    val proposalProgressItem =
//        FirebaseFirestore.getInstance().collection("tasks").document(proposal.taskID)
//            .collection("proposal")
//
//    val document =
//        proposalProgressItem.document(proposal.id).collection("progressItems").document()
//
//    val user = FirebaseAuth.getInstance().currentUser!!
//    val userCurrent = user.uid
//    val userCurrent1 = User(user!!.uid, user.displayName!!, user.email!!, 0, 0L)
//    val proposalId = proposal.id
//
//
//
//
//    val data = ProposalProgressContent(
//        proposalProgressItemContent.value!!,
//        -1,
//        false,
//        document.id,
//        System.currentTimeMillis(),
//        userCurrent,
//        userCurrent1,
//        null,
//        null,
//        proposalId
//    )
//
//
//
//    document.set(data)
//        .addOnFailureListener {
//            Log.i("EXCEPTIONX", "exc = ${it.message}")
//        }.addOnSuccessListener {
//
//            shouldNavigateBackToProposalProgressDisplayFragment.value = true
//            Toast.makeText(
//                HelperApplication.context,
//                "Add Progress Item Success",
//                Toast.LENGTH_SHORT
//            ).show()
//            Log.i("SUCCESS", "SU")
//        }
//
//}