package com.tom.helper.postrequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.source.HelperRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*

class PostRequestViewModel(private val repository: HelperRepository) : ViewModel() {


    val taskTitle = MutableLiveData<String>()


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

        if (taskTitle.value == null || taskTitle.value?.isEmpty() == true) {
            _error.value = "Task Title not complete"
        } else {
            val db = FirebaseFirestore.getInstance()

            val task = FirebaseFirestore.getInstance().collection("task")

            val document = task.document()

            val data = hashMapOf(

                "task_title" to taskTitle.value!!


            )

            document.set(data as Map<String, Any>)
        }


    }


    fun errorReceived() {

        _error.value = null
    }


}