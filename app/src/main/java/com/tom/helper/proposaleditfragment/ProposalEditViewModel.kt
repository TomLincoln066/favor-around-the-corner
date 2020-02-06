package com.tom.helper.proposaleditfragment

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.HelperApplication
import com.tom.helper.source.HelperRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


class ProposalEditViewModel(private val repository: HelperRepository) : ViewModel() {


    val proposalProvider = MutableLiveData<String>()
    val proposalContent = MutableLiveData<String>()


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


        val proposal = FirebaseFirestore.getInstance().collection("proposal")

        val document = proposal.document()

        val data = hashMapOf(

            "senderName" to proposalProvider.value!!,
            "content" to proposalContent.value!!,
            "id" to document.id,
            "createdTime" to System.currentTimeMillis()

        )


        document.set(data as Map<String, Any>)
            .addOnFailureListener {
                Log.i("EXCEPTIONX", "exc = ${it.message}")
            }.addOnSuccessListener {
                //after sending proposal successfully, set shouldNavigateToProposalListFragment.value = true
                shouldNavigateToProposalListFragment.value = true
                Toast.makeText(HelperApplication.context, "Add Proposal Success", Toast.LENGTH_SHORT).show()
                Log.i("SUCCESS", "SU")
            }
        
    }


    fun errorReceived() {

        _error.value = null
    }


}