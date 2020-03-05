package com.tom.helper.homefragment

import android.util.Log
import android.widget.Toast
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.HelperApplication
import com.tom.helper.LoadApiStatus
import com.tom.helper.source.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


// The [ViewModel] that is attached to the [HomeFragment].

// private val repository: HelperRepository
class HomeViewModel(private val repository: HelperRepository) : ViewModel() {




    private val _message = MutableLiveData<Message>()

    val message: LiveData<Message>
        get() = _message


    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user


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


    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status


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
        Log.d("getTasksResult", "getTasksResult")


        coroutineScope.launch {

            val result = repository.getTasks()

            _status.value = LoadApiStatus.LOADING

            when (result) {
                is Result.Success -> {

                    _status.value = LoadApiStatus.DONE
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
            _status.value = LoadApiStatus.LOADING
            when (result) {
                is Result.Success -> {
                    _status.value = LoadApiStatus.DONE
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
            _status.value = LoadApiStatus.LOADING
            when (result) {
                is Result.Success -> {
                    _status.value = LoadApiStatus.DONE
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


    //for task of status 0 (on_going)
    private val _tasksWithMyProposal = MutableLiveData<List<Proposal>>()

    val tasksWithMyProposal: LiveData<List<Proposal>>
        get() = _tasksWithMyProposal


    //for task of status 1 (on_going task which user send proposals and got accepted)
    fun getOnGoingTasksOfMineResult() {
        Log.d("Will", "getOnGoingTasksOfMineResult()")
        coroutineScope.launch {
            val result = repository.getTasksWithMyProposal()
            _status.value = LoadApiStatus.LOADING
            when (result) {
                is Result.Success -> {
                    _status.value = LoadApiStatus.DONE
                    _tasksWithMyProposal.value = result.data
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


    //    try to handle when button_item_request_close in item_request.xml is pressed, will have this task status set from 0 to 1 (see HomeFragment.kt)
    private val _shouldFinishThisTask = MutableLiveData<Task>()
    val shouldFinishThisTask: LiveData<Task>
        get() = _shouldFinishThisTask

    //handle when button_item_request_close is clicked, and it change the status of this task from 0 to 1
    fun clickFinishThisTask(task: Task) {


        val status = FirebaseFirestore.getInstance()

        val document = status.collection("tasks").document(task.id)

        document.update("status", 1)

        //redo this function so that the page will refresh when button_item_request_close
        getOnGoingTasksResult()
    }



    private val _shouldNavigateToChatRoomFragment = MutableLiveData<Task>()
    val shouldNavigateToChatRoomFragment: LiveData<Task>
        get() = _shouldNavigateToChatRoomFragment


    fun clickNavToChatRoomFragment(task: Task?) {
        Log.d("Will", "clickNavToChatRoomFragment(), message=$message")
        message?.let {
            _shouldNavigateToChatRoomFragment.value = task
        }
    }


    fun doneNavigatingToChatRoom() {
        _shouldNavigateToChatRoomFragment.value = null
    }


    fun Long.toDisplayTimePass(): String {
        val now = System.currentTimeMillis()
        val diff = (now - this) / 1000
        val years = diff / (60 * 60 * 24 * 30 * 12)
        val months = diff / (60 * 60 * 24 * 30)
        val days = diff / (60 * 60 * 24)
//    val hours = (diff - days * (60 * 60 * 24)) / (60 * 60)
        val hours = diff / (60 * 60)
//    val minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / 60
        val minutes = diff / (60)
        return when {
            years >= 1 -> "${years}年前"
            months >= 1 -> "${months}個月前"
            days >= 1 -> "${days}天前"
            hours >= 1 -> "${hours}小時前"
            minutes >= 1 -> "${minutes}分鐘前"
            else -> "剛剛"
        }
    }


    fun comingSoon() {
        Toast.makeText(
            HelperApplication.context,
            "coming soon",
            Toast.LENGTH_SHORT
        ).show()
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


}
