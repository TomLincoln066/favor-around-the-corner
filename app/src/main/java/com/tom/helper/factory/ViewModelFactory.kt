package com.tom.helper.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tom.helper.homefragment.HomeViewModel
import com.tom.helper.source.HelperRepository

//* Factory for all ViewModels.
//*/

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: HelperRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
//                isAssignableFrom(MainViewModel::class.java) ->
//                    MainViewModel(repository)

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel()

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}