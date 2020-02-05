package com.tom.helper.proposallistfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

// The [ViewModel] that is attached to the [ProposalListFragment].

// private val repository: HelperRepository
class ProposalListViewModel(private val repository: HelperRepository) : ViewModel() {


    private val _tasks = MutableLiveData<List<Task>>()

    val tasks: LiveData<List<Task>>
        get() = _tasks


    //To test Mock data display of item_proposal on fragment_proposal_list.xml
    fun addProposal() {
        _tasks.value = listOf(
            Task(
                "123", 20000L, 1000000),
            Task(
                "456", 10000000L)
        )


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