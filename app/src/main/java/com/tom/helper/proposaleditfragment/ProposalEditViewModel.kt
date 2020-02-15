package com.tom.helper.proposaleditfragment

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.HelperApplication
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Proposal
import com.tom.helper.source.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.tom.helper.source.User


class ProposalEditViewModel(private val repository: HelperRepository, private val task: Task) :
    ViewModel() {


    val proposalProvider = MutableLiveData<String>()
    val proposalContent = MutableLiveData<String>()
    val proposalAccepted = false


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


    //try to handle when button_proposal_send is clicked in fragment_proposal_edit.xml is pressed, will navigate to Proposal List Fragment
    val shouldNavigateToProposalListFragment = MutableLiveData<Boolean>()

    init {
        shouldNavigateToProposalListFragment.value = false
    }


    //get editText's data and send out to FireBase
    fun submitProposal() {

        _error.value = null

        //check whether taskTitle.value is not valid
        if (proposalContent.value == null || proposalContent.value?.isEmpty() == true) {
            _error.value = "Proposal Content cannot be empty"
            return
        }
        if (proposalProvider.value == null || proposalProvider.value?.isEmpty() == true) {
            _error.value = "Proposal Title cannot be empty"
            return
        }


//        val proposal = FirebaseFirestore.getInstance().collection("proposal")  have proposal input in the task collection
        val proposal = FirebaseFirestore.getInstance().collection("tasks")

        //handle inputting proposal into specific task
        val document = proposal.document(task.id).collection("proposal").document()

        val user = FirebaseAuth.getInstance().currentUser!!

        val taskId = task.id

        val userCurrent = user.uid

        val userCurrent1 = User(user!!.uid,user.displayName!!,user.email!!,0,0L)

        //handle when Navigate To ProposalListFragment change the status of this task from -1 to 0
        val status = FirebaseFirestore.getInstance()

        val document1 = status.collection("tasks").document(task.id)

        document1.update("status",0)




//        val data = hashMapOf(
//
//            "senderName" to proposalProvider.value!!,
//            "content" to proposalContent.value!!,
//            "id" to document.id,
//            "createdTime" to System.currentTimeMillis(),
//            "accepted" to proposalAccepted,
//            "user" to userCurrent
//
//        )

        val data = Proposal(
            document.id,
            System.currentTimeMillis(),
            proposalProvider.value!!,
            proposalContent.value!!,
            proposalProvider.value!!,
            proposalAccepted,
            userCurrent,
            userCurrent1,
            taskID = taskId
        )


//        document.set(data as Map<String, Any>)
        document.set(data)
            .addOnFailureListener {
                Log.i("EXCEPTIONX", "exc = ${it.message}")
            }.addOnSuccessListener {
                //after sending proposal successfully, set shouldNavigateToProposalListFragment.value = true
                shouldNavigateToProposalListFragment.value = true
                Toast.makeText(
                    HelperApplication.context,
                    "Add Proposal Success",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i("SUCCESS", "SU")
            }

    }



//    fun setTaskstatus(){
//
//        val status = FirebaseFirestore.getInstance()
//
//        val document = status.collection("tasks").document(task.id)
//
//        document.update("status",0)
//
//    }







    fun errorReceived() {

        _error.value = null
    }


}