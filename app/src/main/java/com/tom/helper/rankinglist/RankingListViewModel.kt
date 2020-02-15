package com.tom.helper.rankinglist

import android.util.Log
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Rank
import com.tom.helper.source.Result
import com.tom.helper.source.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// The [ViewModel] that is attached to the [RankingListFragment].

// private val repository: HelperRepository
class RankingListViewModel(private val repository: HelperRepository) : ViewModel() {


//    private val _ranks = MutableLiveData<List<Rank>>()
//
//    val ranks: LiveData<List<Rank>>
//        get() = _ranks



    //To test Mock data display of item_ranking_list on fragment_ranking_list.xml
//    fun addRankUser() {
//        _ranks.value = listOf(
//            Rank(
//                "123", "will", "burger king", 1000000),
//            Rank("456", "Tom", "king", 1000),
//            Rank("789", "Bill", "man", 20000),
//            Rank("101112", "James", "computer expert", 10000)
//
//        )
//
//
//    }



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
    fun prepareTaskTest(){
        _tasks.value = listOf(Task("123"))
    }




//    for all tasks of mine
    fun getTasksOfMineResult() {
//        Log.d("getTasksOfMineResult", "${getTasksOfMineResult()}")
        coroutineScope.launch {
            val result = repository.getTasksOfMine()

            when (result) {
                is Result.Success -> {
                    _tasks.value = result.data
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





}