package com.tom.helper.rankinglist

import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Rank
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

// The [ViewModel] that is attached to the [RankingListFragment].

// private val repository: HelperRepository
class RankingListViewModel(private val repository: HelperRepository) : ViewModel() {


    private val _ranks = MutableLiveData<List<Rank>>()

    val ranks: LiveData<List<Rank>>
        get() = _ranks


    //To test Mock data display of item_ranking_list on fragment_ranking_list.xml
    fun addRankUser() {
        _ranks.value = listOf(
            Rank(
                "123", "will", "burger king", 1000000
            ), Rank("456", "Tom", "king", 1000), Rank("789", "Bill", "man", 20000)

        )


    }


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



}