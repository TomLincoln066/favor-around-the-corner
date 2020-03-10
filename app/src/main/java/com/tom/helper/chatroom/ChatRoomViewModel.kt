package com.tom.helper.chatroom

import android.util.Log
import android.widget.Toast
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.HelperApplication
import com.tom.helper.LoadApiStatus
import com.tom.helper.source.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class ChatRoomViewModel(private val repository: HelperRepository, private val task: Task) :
    ViewModel() {


    val messageContent = MutableLiveData<String>()


    private var _messages = MutableLiveData<List<Message>>()

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

    //To test Mock data display of item_message.xml on fragment_chat_room.xml
    fun addMessages() {
//        _messages.value = listOf(
//            Message(
//                "123", 12, "burger king", "wwww", null, null, ""
//            ),
//            Message("123", 12, "burger king", "wwww", null, null, "")
//
//
//        )


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


    fun sendNessages() {


        coroutineScope.launch {
            val result = repository.sendMessagesToDB(task)

            when (result) {
                is Result.Success -> {
//                    _messages.value = result.data
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



    fun getMessagesLiveSnapShot() {

        _messages = repository.getMessagesFromDBLive(task) as MutableLiveData<List<Message>>

    }


    fun getMessages() {

        coroutineScope.launch {
            val result = repository.getMessagesFromDB(task)

            when (result) {
                is Result.Success -> {
                    _messages.value = result.data
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


    fun submitMessage() {

        // to prevent situation when user clicks send request button too many times when Loading.
        if (_status.value == LoadApiStatus.LOADING) {
            return
        }

        _error.value = null

        _status.value = LoadApiStatus.LOADING

        //check whether taskTitle.value is not valid
        if (messageContent.value == null || messageContent.value?.isEmpty() == true) {
            _error.value = "Proposal Content cannot be empty"
            return
        }

        val user = FirebaseAuth.getInstance().currentUser!!


        val userCurrent = user.uid

        val photoUrl = user.photoUrl.toString()

        val message = FirebaseFirestore.getInstance().collection("tasks")

//        val document = message.document(task.id).collection("messages").document(userCurrent)
        val document = message.document(task.id).collection("messages").document()


        val userCurrentUser = User(user.uid, user.displayName!!, user.email!!, 0, 0L,photoUrl)


        val taskId = task.id

        val taskOnwerId = task.userId


        val data = Message(
            document.id,
            System.currentTimeMillis(),
            "",
            messageContent.value!!,
            userCurrentUser,
            userCurrent,
            null,
            taskOnwerId,
            "",
            taskId
        )


        document.set(data)
            .addOnFailureListener {
                Log.i("EXCEPTIONX", "exc = ${it.message}")
                _status.value = LoadApiStatus.ERROR
            }.addOnSuccessListener {

                _status.value = LoadApiStatus.DONE




                Log.i("SUCCESS", "SU")
            }


    }






    //handle converting date to string
    fun convertLongToDateString(systemTime: Long): String {
        return SimpleDateFormat("MMM-dd-yyyy HH:mm:ss")
            .format(systemTime).toString()
    }

    @InverseMethod("convertLongToDateString")
    fun convertDateStringToLong(string: String): Long {
        return SimpleDateFormat("MMM-dd-yyyy HH:mm:ss").parse(string).time
    }


    fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
        val smoothScroller = object : LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int = snapMode
            override fun getHorizontalSnapPreference(): Int = snapMode
        }
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }





}