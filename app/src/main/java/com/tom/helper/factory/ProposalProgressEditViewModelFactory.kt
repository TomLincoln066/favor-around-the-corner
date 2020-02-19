package com.tom.helper.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tom.helper.proprogresseditfragment.ProposalProgressEditViewModel
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Proposal
import com.tom.helper.source.Task
import com.tom.helper.taskprogressdialog.ProProgressViewModel

@Suppress("UNCHECKED_CAST")
class ProposalProgressEditViewModelFactory(
    private val helperRepository: HelperRepository,
    private val proposal:Proposal
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {



        if (modelClass.isAssignableFrom(ProposalProgressEditViewModel::class.java)) {
            return ProposalProgressEditViewModel(helperRepository,proposal ) as T
        }


        if (modelClass.isAssignableFrom(ProProgressViewModel::class.java)) {
            return ProProgressViewModel(helperRepository,proposal ) as T
        }



        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}