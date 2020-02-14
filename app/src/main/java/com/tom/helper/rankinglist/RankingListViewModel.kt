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


    private val _ranks = MutableLiveData<List<Rank>>()

    val ranks: LiveData<List<Rank>>
        get() = _ranks


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





    //for task of status -1 (posted)
    private val _tasks = MutableLiveData<List<Task>>()

    val tasks: LiveData<List<Task>>
        get() = _tasks

    //for task of status 0 (on_going)
    private val _tasks1 = MutableLiveData<List<Task>>()

    val tasks1: LiveData<List<Task>>
        get() = _tasks1

    //for task of status 1 (finished)
    private val _tasks2 = MutableLiveData<List<Task>>()

    val tasks2: LiveData<List<Task>>
        get() = _tasks2



    //for task of status 0 (posted)
    fun getTasksResult() {
        Log.d("getTasksResult", "getTasksResult")
        coroutineScope.launch {
            val result = repository.getTasks()

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


    //for task of status 1 (on_going)
    fun getOnGoingTasksResult() {
        Log.d("Will", "getOnGoingTasksResult()")
        coroutineScope.launch {
            val result = repository.getOnGoingTasks()

            when (result) {
                is Result.Success -> {
                    _tasks1.value = result.data
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

    //for task of status 2 (Finished)
    fun getFinishedTasksResult() {
        Log.d("getFinishedTasksResult", "getFinishedTasksResult")
        coroutineScope.launch {
            val result = repository.getFinishedTasks()

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

}