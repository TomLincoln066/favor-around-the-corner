package com.tom.helper.homefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Task
import com.tom.helper.source.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


// The [ViewModel] that is attached to the [HomeFragment].

// private val repository: HelperRepository
class HomeViewModel(private val repository: HelperRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user


    private val _tasks = MutableLiveData<List<Task>>()

    val tasks: LiveData<List<Task>>
        get() = _tasks


    //To test Mock data display of item_request on fragment_home.xml
    fun prepareTasks() {
        _tasks.value = listOf(
            Task("123", title = "My Computer Crashes",status = 1,createdTime = 20200130,taskCreator = "Tom", price = 20100,subContent = listOf("smash it","sit on it","let's do that again", "oh god it works now")),
            Task("456", title = "I need a ride to interview", subContent = listOf("kill it","sit on it","let's do that again", "oh god it works now")),
            Task("789", title = "Who want's to play UNO", subContent = listOf("go for it","sit on it","let's do that again", "oh god it works now"))
        )


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


}
