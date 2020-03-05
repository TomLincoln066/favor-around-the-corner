package com.tom.helper.proposallistfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.source.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// The [ViewModel] that is attached to the [ProposalListFragment].

// private val repository: HelperRepository
class ProposalListViewModel(private val repository: HelperRepository, val task: Task) :
    ViewModel() {
    private val _profile = MutableLiveData<User>()

    val profile: LiveData<User>
        get() = _profile


    //    for all tasks of mine
    fun getCurrentUserData() {

        coroutineScope.launch {
            val result = repository.getUserCurrent()

            when (result) {
                is com.tom.helper.source.Result.Success -> {
                    _profile.value = result.data
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


    private var _proposals = MutableLiveData<List<Proposal>>()

    val proposals: LiveData<List<Proposal>>
        get() = _proposals


    //once the button accept in item_proposal.xml is pressed, it'll change the proposal status(-1 -> 0) and get all proposals again. see HelperRemoteDataSource.kt for editOneProposal function.
    fun clickToGetOneProposalStatusAccepted(proposal: Proposal) {

        coroutineScope.launch {
            val result = repository.editOneProposal(task, proposal)

            when (result) {
                is Result.Success -> {
                    getProposalsResult()


                    //handle when Navigate To ProposalListFragment change the status of this task from -1 to 0

                    val status = FirebaseFirestore.getInstance()

                    val document1 = status.collection("tasks").document(task.id)

                    document1.update("status", 0)



                }

                is Result.Error -> {
                    result.exception
                }

                is Result.Fail -> {
                    _error.value = result.error
                }
            }

        }


//        when(proposal.status ){
//            -1-> proposal.id
//
//        }

    }

    //once the button unAccept in item_proposal.xml is pressed, it'll change the proposal status(0 -> -1) and get all proposals again. see HelperRemoteDataSource.kt for editOneProposalToUnaccepted function.
    fun clickToGetOneProposalStatusUnAccepted(proposal: Proposal) {

        coroutineScope.launch {
            val result = repository.editOneProposalToUnaccepted(task, proposal)

            when (result) {
                is Result.Success -> {
                    getProposalsResult()
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







    fun getProposalsResult() {

        coroutineScope.launch {
//            val result = repository.getProposals(task)
            val result = repository.getProposals(task)

            when (result) {
                is Result.Success -> {
                    _proposals.value = result.data
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



    //snapshot proposal

    fun getProposalsLiveSnapShot(){

        _proposals = repository.getProposalsLive(task) as MutableLiveData<List<Proposal>>

    }









    fun getProposalsOfMineResult() {

        coroutineScope.launch {
            val result = repository.getProposalsOfMine(task)

            when (result) {
                is Result.Success -> {
                    _proposals.value = result.data
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



    //try to handle when button_to_task_progress_sheet in item_proposal.xml is pressed, will navigate to ProProgressFragment(see ProposalListFragment.kt)
    private val _shouldNavigateToProProgressFragment = MutableLiveData<Proposal>()
    val shouldNavigateToProProgressFragment: LiveData<Proposal>
        get() = _shouldNavigateToProProgressFragment


    fun clickNavigateToProProgressFragment(proposal: Proposal?) {
        _shouldNavigateToProProgressFragment.value = proposal
    }

    fun doneNavigatingToProProgressFragment() {
        _shouldNavigateToProProgressFragment.value = null
    }









}