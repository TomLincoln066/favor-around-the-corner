package com.tom.helper.jobdetailfragment

import android.util.Log
import android.widget.Toast
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.helper.HelperApplication
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Message
import com.tom.helper.source.Result
import com.tom.helper.source.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// private val repository: HelperRepository
class JobDetailViewModel(private val repository: HelperRepository) : ViewModel() {


    private val _message = MutableLiveData<Message>()

    val message: LiveData<Message>
        get() = _message


    private val _tasks = MutableLiveData<List<Task>>()

    val tasks: LiveData<List<Task>>
        get() = _tasks


    fun getTasksResult() {

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


    fun comingSoon() {
        Toast.makeText(
            HelperApplication.context,
            "coming soon",
            Toast.LENGTH_SHORT
        ).show()
    }


    private val _shouldNavigateToChatListFragment = MutableLiveData<Task>()
    val shouldNavigateToChatListFragment: LiveData<Task>
        get() = _shouldNavigateToChatListFragment


    fun clickNavToChatListFragment(task: Task?) {
        Log.d("Will", "clickNavToChatListFragment(), message=$message")
        message?.let {
            _shouldNavigateToChatListFragment.value = task
        }
    }


    fun doneNavigatingToChatList() {
        _shouldNavigateToChatListFragment.value = null
    }


}