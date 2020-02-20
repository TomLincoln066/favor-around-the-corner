package com.tom.helper.profile

import android.util.Log
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Proposal
import com.tom.helper.source.Task
import com.tom.helper.source.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class ProfileViewModel(private val repository: HelperRepository) : ViewModel() {


    private val _profile = MutableLiveData<User>()

    val profile: LiveData<User>
        get() = _profile


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


    // handle taskPrice input type convert problem( Long to String and String to Long)
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


    // handle status input type convert problem( Int to String and String to Int)
    @InverseMethod("convertIntToString")
    fun convertStringToInt(value: String): Int {
        return try {
            value.toInt().let {
                when (it) {
                    0 -> 0
                    else -> it
                }
            }
        } catch (e: NumberFormatException) {
            1
        }
    }

    fun convertIntToString(value: Int): String {
        return value.toString()
    }


    //for all task of mine
    private val _tasks = MutableLiveData<List<Task>>()

    val tasks: LiveData<List<Task>>
        get() = _tasks


    //mock data
    fun prepareTaskTest() {
        _tasks.value = listOf(Task("123"))
    }


    //    for all tasks of mine
    fun getTasksOfMineResult() {

        coroutineScope.launch {
            val result = repository.getTasksOfMine()

            when (result) {
                is com.tom.helper.source.Result.Success -> {
                    _tasks.value = result.data
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

    //try to handle when button_mission_detail_proposal_total in item_task_of_mine.xml is pressed, will navigate to ProposalListFragment(see RankingListFragment.kt)
    private val _shouldNavigateToProposalList = MutableLiveData<Task>()
    val shouldNavigateToProposalList: LiveData<Task>
        get() = _shouldNavigateToProposalList


    fun clickNavigateToProposalList(task: Task) {
        _shouldNavigateToProposalList.value = task
    }

    fun doneNavigatingToProposalList() {
        _shouldNavigateToProposalList.value = null
    }


    //handle converting date to string
    fun convertLongToDateString(systemTime: Long): String {
        return SimpleDateFormat("MMM-dd-yyyy HH:mm:ss")
            .format(systemTime).toString()
    }


    //    try to handle when button_item_request_close in item_request.xml is pressed, will have this task status set from 0 to 1 (see HomeFragment.kt)
    private val _shouldFinishThisTask = MutableLiveData<Task>()
    val shouldFinishThisTask: LiveData<Task>
        get() = _shouldFinishThisTask

    //handle when button_item_request_close is clicked, and it change the status of this task from 0 to 1
    fun clickFinishThisTask(task: Task) {


        val status = FirebaseFirestore.getInstance()

        val document = status.collection("tasks").document(task.id)

        document.update("status", 1)


        //from function getTasksOfMineResult()
        coroutineScope.launch {
            val result = repository.getProposalsOfStatusEqualToZero(task)

            when (result) {
                is com.tom.helper.source.Result.Success -> {
//                    _tasks.value = result.data
                }

                is com.tom.helper.source.Result.Error -> {
                    result.exception
                }

                is com.tom.helper.source.Result.Fail -> {
                    _error.value = result.error
                }
            }

        }

        getTasksOfMineResult()

    }


    // to handle whether the recycler expand or not, see fragment_profile.xml
    val shouldExpandThisRecycler = MutableLiveData<Boolean>().apply {
        value = false
    }


    fun clickExpandThisRecycler() {
        //if shouldExpandThisRecycler is true, function sets it false, Vice Versa.
        shouldExpandThisRecycler.value?.let {
            shouldExpandThisRecycler.value = !it
        }

    }


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


}







