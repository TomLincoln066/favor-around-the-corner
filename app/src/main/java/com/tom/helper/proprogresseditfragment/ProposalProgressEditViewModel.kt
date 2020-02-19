package com.tom.helper.proprogresseditfragment

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.HelperApplication
import com.tom.helper.source.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


class ProposalProgressEditViewModel(
    private val repository: HelperRepository,
    private val proposal: Proposal
) :
    ViewModel() {


    val proposalProgressItemContent = MutableLiveData<String>()
    val proposalProgressItemContentAccepted = false


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

        //check whether proposalProgressItemContent.value is not valid
        if (proposalProgressItemContent.value == null || proposalProgressItemContent.value?.isEmpty() == true) {
            _error.value = "proposalProgressItemContent cannot be empty"
            return
        }


//        val proposal = FirebaseFirestore.getInstance().collection("proposal")  have proposal input in the task collection

        val proposalProgressItem =
            FirebaseFirestore.getInstance().collection("tasks").document(proposal.taskID)
                .collection("proposal")

        val document =
            proposalProgressItem.document(proposal.id).collection("progressItems").document()

        val user = FirebaseAuth.getInstance().currentUser!!
        val userCurrent = user.uid
        val userCurrent1 = User(user!!.uid, user.displayName!!, user.email!!, 0, 0L)
        val proposalId = proposal.id

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
            proposalId
        )


//        document.set(data as Map<String, Any>)
        document.set(data)
            .addOnFailureListener {
                Log.i("EXCEPTIONX", "exc = ${it.message}")
            }.addOnSuccessListener {
                //after sending proposal successfully, set shouldNavigateToProposalListFragment.value = true
                shouldNavigateBackToProposalProgressDisplayFragment.value = true
                Toast.makeText(
                    HelperApplication.context,
                    "Add Progress Item Success",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i("SUCCESS", "SU")
            }

    }














}