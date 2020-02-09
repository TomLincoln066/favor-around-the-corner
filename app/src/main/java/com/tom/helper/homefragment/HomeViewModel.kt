package com.tom.helper.homefragment
import android.util.Log
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Result
import com.tom.helper.source.Task
import com.tom.helper.source.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


// The [ViewModel] that is attached to the [HomeFragment].

// private val repository: HelperRepository
class HomeViewModel(private val repository: HelperRepository) : ViewModel() {

//    private val _selelctedTask = MutableLiveData<Task>()
//    val selectedTask: LiveData<Task>
//        get() = _selelctedTask
//
//    fun selectTask(task: Task) {
//        _selelctedTask.value = task
//    }
//
//    fun doneNavigatingToTask() {
//        _selelctedTask.value = null
//    }


    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user


    //for task of status 0 (posted)
    private val _tasks = MutableLiveData<List<Task>>()

    val tasks: LiveData<List<Task>>
        get() = _tasks

    //for task of status 1 (on_going)
    private val _tasks1 = MutableLiveData<List<Task>>()

    val tasks1: LiveData<List<Task>>
        get() = _tasks1

    //for task of status 2 (finished)
    private val _tasks2 = MutableLiveData<List<Task>>()

    val tasks2: LiveData<List<Task>>
        get() = _tasks2





    //To test Mock data display of item_request on fragment_home.xml
    fun prepareTasks() {
        _tasks.value = listOf(
//            Task("123", title = "My Computer Crashes",status = 1,createdTime = 20200130,taskCreator = "Tom", price = 20100,subContent = listOf("smash it","sit on it","let's do that again", "oh god it works now")),
//            Task("456", title = "I need a ride to interview", subContent = listOf("kill it","sit on it","let's do that again", "oh god it works now")),
//            Task("789", title = "Who want's to play UNO", subContent = listOf("go for it","sit on it","let's do that again", "oh god it works now"))
            Task("789", title = "Who want's to play UNO")
        )

    }



    //try to handle when button_mission_detail in item_request.xml is pressed, will navigate to JobDetailFragment(see HomeFragment.kt)
    private val _shouldNavigateToJobDetail = MutableLiveData<Task>()
    val shouldNavigateToJobDetail: LiveData<Task>
        get() = _shouldNavigateToJobDetail


    fun doneNavigatingToJobDetail() {
        _shouldNavigateToJobDetail.value = null
    }




    //try to handle when button_mission_detail_proposal_total in item_request.xml is pressed, will navigate to ProposalListFragment(see HomeFragment.kt)
    private val _shouldNavigateToProposalList = MutableLiveData<Task>()
    val shouldNavigateToProposalList: LiveData<Task>
        get() = _shouldNavigateToProposalList

//    fun navToProposalList() {
//        _shouldNavigateToProposalList.value = true
//    }

    fun doneNavigatingToProposalList() {
        _shouldNavigateToProposalList.value = null
    }


    //for task of status 0 (posted)
    fun getTasksResult() {
        Log.d("getTasksResult","getTasksResult")
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
        Log.d("getOnGoingTasksResult","getOnGoingTasksResult")
        coroutineScope.launch {
            val result = repository.getOnGoingTasks()

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

    //for task of status 2 (Finished)
    fun getFinishedTasksResult() {
        Log.d("getFinishedTasksResult","getFinishedTasksResult")
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


    fun convertLongToDateString(systemTime: Long): String {
        return SimpleDateFormat("MMM-dd-yyyy HH:mm:ss")
            .format(systemTime).toString()
    }

    fun clickNavigateToJobDetail(task: Task) {
        _shouldNavigateToJobDetail.value = task
    }

    fun clickNavigateToProposalList(task: Task) {
        _shouldNavigateToProposalList.value = task
    }
}
