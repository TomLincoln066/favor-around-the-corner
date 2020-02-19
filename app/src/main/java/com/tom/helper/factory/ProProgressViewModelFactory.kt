package com.tom.helper.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.ProposalProgressContent

@Suppress("UNCHECKED_CAST")
class ProProgressViewModelFactory(
    private val helperRepository: HelperRepository,
    private val proposalProgressContent: ProposalProgressContent
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {



        if (modelClass.isAssignableFrom(ProProgressViewModelFactory::class.java)) {
            return ProProgressViewModelFactory(helperRepository,proposalProgressContent ) as T
        }


        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}