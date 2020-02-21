package com.tom.helper.taskprogressdialog

import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.helper.LoadApiStatus
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Proposal
import com.tom.helper.source.ProposalProgressContent
import com.tom.helper.source.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProProgressViewModel(private val repository: HelperRepository, private val proposal: Proposal) : ViewModel() {


    private val _proposalProgressContents = MutableLiveData<List<ProposalProgressContent>>()

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


    //save the proposal into proposalLive in the first place
    val proposalLive = MutableLiveData<Proposal>().apply {
        value = proposal
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


    fun clickToEditOneProposalProgressItemStatusFinished(proposal: Proposal,proposalProgressContent: ProposalProgressContent){

        coroutineScope.launch {
            val result = repository.editOneProposalProgressItemToFinished(proposal,proposalProgressContent)

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



}