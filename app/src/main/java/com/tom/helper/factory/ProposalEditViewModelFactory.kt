package com.tom.helper.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tom.helper.proposaleditfragment.ProposalEditViewModel
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Task

@Suppress("UNCHECKED_CAST")
class ProposalEditViewModelFactory(
    private val helperRepository: HelperRepository,
    private val task: Task
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ProposalEditViewModel::class.java)) {
            return ProposalEditViewModel(helperRepository, task) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}