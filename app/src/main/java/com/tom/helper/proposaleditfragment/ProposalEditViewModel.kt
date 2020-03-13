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
import com.tom.helper.LoadApiStatus
import com.tom.helper.source.User
import kotlinx.coroutines.launch


class ProposalEditViewModel(private val repository: HelperRepository, private val task: Task) :
    ViewModel() {


    val proposalProvider = MutableLiveData<String>()
    val proposalContent = MutableLiveData<String>()

    val proposalFirstStep = MutableLiveData<String>()
    val proposalSecondStep = MutableLiveData<String>()
    val proposalThirdStep = MutableLiveData<String>()
    val proposalFourthStep = MutableLiveData<String>()

    val proposalAccepted = false


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


    //try to handle when button_proposal_send is clicked in fragment_proposal_edit.xml is pressed, will navigate to Proposal List Fragment
    val shouldNavigateToProposalListFragment = MutableLiveData<Boolean>()


    init {

        shouldNavigateToProposalListFragment.value = false


    }


    //get editText's data and send out to FireBase
    fun submitProposal() {

        // to prevent situation when user clicks send request button too many times when Loading.
        if (_status.value == LoadApiStatus.LOADING) {
            return
        }

        _error.value = null

        _status.value = LoadApiStatus.LOADING

        //check whether taskTitle.value is not valid
        if (proposalContent.value == null || proposalContent.value?.isEmpty() == true) {
            _error.value = "Proposal Content cannot be empty"
            return
        }
        if (proposalProvider.value == null || proposalProvider.value?.isEmpty() == true) {
            _error.value = "Proposal Provider cannot be empty"
            return
        }
        if (proposalFirstStep.value == null || proposalFirstStep.value?.isEmpty() == true) {
            _error.value = "proposalFirstStep cannot be empty"
            return
        }
        if (proposalSecondStep.value == null || proposalSecondStep.value?.isEmpty() == true) {
            _error.value = "proposalSecondStep cannot be empty"
            return
        }
        if (proposalThirdStep.value == null || proposalThirdStep.value?.isEmpty() == true) {
            _error.value = "proposalThirdStep cannot be empty"
            return
        }
        if (proposalFourthStep.value == null || proposalFourthStep.value?.isEmpty() == true) {
            _error.value = "proposalFourthStep cannot be empty"
            return
        }


//        val proposal = FirebaseFirestore.getInstance().collection("proposal")  have proposal input in the task collection
        val proposal = FirebaseFirestore.getInstance().collection("tasks")

        val user = FirebaseAuth.getInstance().currentUser!!
        val userCurrent = user.uid

        //handle inputting proposal into specific task
        val document = proposal.document(task.id).collection("proposal").document(userCurrent)


        val taskId = task.id

        val taskOnwerId = task.userId

        val taskPrice = task.price

        val userCurrent1 = User(user!!.uid, user.displayName!!, user.email!!, 0, 0L)


        val data = Proposal(

            document.id,

            System.currentTimeMillis(),
            proposalProvider.value!!,
            proposalContent.value!!,
            proposalProvider.value!!,
            proposalAccepted,
            userCurrent,
            userCurrent1,
            taskID = taskId,
            taskOnwerID = taskOnwerId,
            taskPrice = taskPrice,
            firstStep = proposalFirstStep.value!!,
            secondStep = proposalSecondStep.value!!,
            thirdStep = proposalThirdStep.value!!,
            fourthStep = proposalFourthStep.value!!
        )




        document.set(data)
            .addOnFailureListener {
                Log.i("EXCEPTIONX", "exc = ${it.message}")
            }.addOnSuccessListener {


                //                shouldNavigateToHomeFragment.value = true

                shouldNavigateToProposalListFragment.value = true

                addTaskProposalOwnerID()

                Toast.makeText(
                    HelperApplication.context,
                    "新增提案成功",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i("SUCCESS", "SU")
            }

    }


    fun addTaskProposalOwnerID(userID: String = HelperApplication.user.id) {

        coroutineScope.launch {

            when (val result = repository.addTaskProposalOwnerID(task, userID)) {
                is com.tom.helper.source.Result.Success -> {
//                    _proposals.value = result.data
                }

                is com.tom.helper.source.Result.Error -> {
                    result.exception
                }

                is com.tom.helper.source.Result.Fail -> {
                    _error.value = result.error
                }
            }

        }

    }


    fun errorReceived() {

        _error.value = null
    }


}