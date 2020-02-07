package com.tom.helper.proposallistfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Proposal
import com.tom.helper.source.Result
import com.tom.helper.source.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// The [ViewModel] that is attached to the [ProposalListFragment].

// private val repository: HelperRepository
class ProposalListViewModel(private val repository: HelperRepository, private val task: Task) : ViewModel() {


    private val _proposals = MutableLiveData<List<Proposal>>()

    val proposals: LiveData<List<Proposal>>
        get() = _proposals


    //To test Mock data display of item_proposal on fragment_proposal_list.xml
    fun addProposal() {
        _proposals.value = listOf(
            Proposal(
                "123", 20000L, "I got your back"),
            Proposal(
                "456", 10000000L),
            Proposal(),
            Proposal(),
            Proposal()
        )


    }


    fun getProposalsResult() {

        coroutineScope.launch {
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

}