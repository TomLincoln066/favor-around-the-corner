package com.tom.helper.chatlist

import android.util.Log
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.helper.LoadApiStatus
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Message
import com.tom.helper.source.Result
import com.tom.helper.source.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatListViewModel(private val repository: HelperRepository, private val task: Task) :
    ViewModel() {


    private val _message = MutableLiveData<Message>()

    val message: LiveData<Message>
        get() = _message




    private val _messages = MutableLiveData<List<Message>>()

    val messages: LiveData<List<Message>>
        get() = _messages

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status


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




    fun getChatUser() {

        coroutineScope.launch {
            val result = repository.getMessagesFromDB(task)

            when (result) {
                is Result.Success -> {
                    _messages.value = result.data
                    Log.d("getChatUser","getChatUser = ${_messages}")
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