package com.tom.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: HelperRepository) : ViewModel() {







    var checkUser = MutableLiveData<Boolean>()

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

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



    private val _profile = MutableLiveData<User>()

    val profile: LiveData<User>
        get() = _profile


    //    for all tasks of mine
    fun getCurrentUserData() {

        coroutineScope.launch {
            val result = repository.getUserCurrent()

            when (result) {
                is com.tom.helper.source.Result.Success -> {
                    _profile.value = result.data

                }

                is com.tom.helper.source.Result.Error -> {
                    result.exception
                }

                is com.tom.helper.source.Result.Fail -> {
                    _error.value = result.error
                }
            }

        }

    }



    fun checkUserResult() {
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.checkUser()

            checkUser.value = when (result) {
                is com.tom.helper.source.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE

                    getCurrentUserData()

                    result.data
                }
                is com.tom.helper.source.Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is com.tom.helper.source.Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = HelperApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }

    }

}