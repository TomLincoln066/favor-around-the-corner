package com.tom.helper.taskprogressdialog

import android.util.Log
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.HelperApplication
import com.tom.helper.LoadApiStatus
import com.tom.helper.source.*
import com.tom.helper.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProProgressViewModel(
    private val repository: HelperRepository,
    private val proposal: Proposal
) : ViewModel() {


    private var _proposalProgressContents = MutableLiveData<List<ProposalProgressContent>>()

    val proposalProgressContents: LiveData<List<ProposalProgressContent>>
        get() = _proposalProgressContents


    //for mock progress item
    fun prepareMockProgress() {

        _proposalProgressContents.value = listOf(
            ProposalProgressContent("1111", -1, false, "", -1, "", null, null, null, "", "")
            ,
            ProposalProgressContent("1111", -1, false, "", -1, "", null, null, null, "", "")
            , ProposalProgressContent("1111", -1, false, "", -1, "", null, null, null, "", "")
            , ProposalProgressContent("1111", -1, false, "", -1, "", null, null, null, "", "")
            , ProposalProgressContent("1111", -1, false, "", -1, "", null, null, null, "", "")

        )

    }


    private val _proposals = MutableLiveData<List<Proposal>>()

    val proposals: LiveData<List<Proposal>>
        get() = _proposals

    private val _profile = MutableLiveData<User>()

    val profile: LiveData<User>
        get() = _profile

    //save the proposal into proposalLive in the first place
    val proposalLive = MutableLiveData<Proposal>().apply {
        value = proposal
        Log.d("UserId", "proposalLive = ${proposal.userID}")
//        Log.d("UserId", "${}")

    }

    //save the proposal into proposalLive in the first place
    val proposalLive_taskOwnerID = MutableLiveData<Proposal>().apply {
        value = proposal
        Log.d("UserId", "proposalLive_taskOwnerID  = ${proposal.taskOnwerID}")
//        Log.d("UserId", "${}")

    }
    // viewModel.profile.id == viewModel.proposalLive_taskOwnerID.taskOnwerID
    val mediator = MediatorLiveData<Boolean>().apply {
        addSource(proposalLive_taskOwnerID) {
            val taskOwnerID = proposalLive_taskOwnerID.value?.taskOnwerID

            val profileID = profile.value?.id


            Logger.d("UserID_PT=${taskOwnerID}, PROFILE_ID=${profileID}")

            value = taskOwnerID == profileID
        }
        addSource(profile) {
            val taskOwnerID = proposalLive_taskOwnerID.value?.taskOnwerID

            val profileID = profile.value?.id


            Logger.d("UserID_PT=${taskOwnerID}, PROFILE_ID=${profileID}")

            value = taskOwnerID == profileID

        }
    }


    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status


    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

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


    fun getProposalProgressItem(proposal: Proposal) {

        coroutineScope.launch {
            val result = repository.getProposalProgressItem(proposal)

            _status.value = LoadApiStatus.LOADING

            when (result) {
                is Result.Success -> {


                    _status.value = LoadApiStatus.DONE

                    _proposalProgressContents.value = result.data
                }

                is Result.Error -> {
                    result.exception
                }

                is Result.Fail -> {
                    _error.value = result.error
                }
            }

        }

    }


    fun clickToEditOneProposalProgressItemStatusFinished(
        proposal: Proposal,
        proposalProgressContent: ProposalProgressContent
    ) {

        coroutineScope.launch {
            val result =
                repository.editOneProposalProgressItemToFinished(proposal, proposalProgressContent)

            when (result) {
                is Result.Success -> {
                    getProposalProgressItem(proposal)
                }

                is Result.Error -> {
                    result.exception
                }

                is Result.Fail -> {
                    _error.value = result.error
                }
            }

        }


    }


    //    for all tasks of mine
    fun getCurrentUserData() {

        coroutineScope.launch {
            val result = repository.getUserCurrent()

            when (result) {
                is com.tom.helper.source.Result.Success -> {
                    _profile.value = result.data
                    Log.d("UserId-", "profile = ${result.data.id}")
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


    //MediatorLiveData track liveData....
    val ableToNavToProgress = MediatorLiveData<Boolean>().apply {
        addSource(_profile) {
            value = (it.id == proposalLive.value?.userID)
        }
        addSource(proposalLive) {
            value = (_profile.value?.id == it.userID)
        }
    }


    //MediatorLiveData track liveData....
    val ableToCheckProgressItem = MediatorLiveData<Boolean>().apply {
        addSource(_profile) {
            value = (it.id == proposalLive.value?.userID)
        }
        addSource(proposalLive) {
            value = (_profile.value?.id == it.userID)
        }
    }


    //handle when button_finish_this_task is clicked, and it change the status of this task from 0 to 1
    fun clickFinishThisTask(proposal: Proposal) {
        Log.d("clickFinishThisTask().ProProgressViewModel", "clickFinishThisTask")

        val status = FirebaseFirestore.getInstance()

        val document = status.collection("tasks").document(proposal.taskID)

        document.update("status", 1).addOnSuccessListener {



        }


    }



    //snapshot proposalItemList

    fun getProposalItemsLiveSnapShot(){

        _proposalProgressContents = repository.getProposalProgressItemLive(proposal) as MutableLiveData<List<ProposalProgressContent>>

    }







}