package com.tom.helper.postrequest

import java.util.concurrent.TimeUnit
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.source.HelperRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*

class PostRequestViewModel(private val repository: HelperRepository) : ViewModel() {


    val taskTitle = MutableLiveData<String>()
    val taskProvider = MutableLiveData<String>()
    val taskPrice = MutableLiveData<Long>()
    val taskContent = MutableLiveData<String>()


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


    //get editText's data and send out to firebase
    fun submitTask() {

        _error.value = null

        //check whether taskTitle.value is not valid
        if (taskContent.value == null || taskContent.value?.isEmpty() == true) {
            _error.value = "Task Content cannot be empty"
            return
        }
        if (taskTitle.value == null || taskTitle.value?.isEmpty() == true) {
            _error.value = "Task Title cannot be empty"
            return
        }
        if (taskTitle.value == null || taskTitle.value?.isEmpty() == true) {
            _error.value = "Task Title not complete"
            return
        }

        val db = FirebaseFirestore.getInstance()

        val task = FirebaseFirestore.getInstance().collection("task")

        val document = task.document()

        val data = hashMapOf(

            "task_provider" to taskProvider.value!!,
            "task_price" to taskPrice.value!!,
            "task_content" to taskContent.value!!,
            "task_title" to taskTitle.value!!,
            "task_id" to document.id,
//            "task_create_time" to Calendar.getInstance().timeInMillis,
            "task_create_time" to convertLongToDateString(System.currentTimeMillis())
//            "task_create_time" to convertLongToTimeAgo(System.currentTimeMillis())

        )

        document.set(data as Map<String, Any>)


    }


    fun errorReceived() {

        _error.value = null
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


    private fun convertLongToDateString(systemTime: Long): String {
        return SimpleDateFormat("MMM-dd-yyyy HH:mm:ss")
            .format(systemTime).toString()
    }


    fun convertLongToTimeAgo(systemTime: Long): String{
        val currentTime = Calendar.getInstance().timeInMillis
        val diff =  currentTime - systemTime
        var minutes = TimeUnit.MINUTES.convert(diff,TimeUnit.MILLISECONDS)
        val hours = minutes / 60
            val t = diff / hours
            return "$t + 分鐘前"
    }

}


//|| taskContent.value == null || taskContent.value?.isEmpty() == true || taskProvider.value == null || taskProvider.value?.isEmpty() == true