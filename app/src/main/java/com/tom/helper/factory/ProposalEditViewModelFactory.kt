package com.tom.helper.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tom.helper.chatlist.ChatListViewModel
import com.tom.helper.chatroom.ChatRoomViewModel
import com.tom.helper.proposaleditfragment.ProposalEditViewModel
import com.tom.helper.proposallistfragment.ProposalListViewModel
import com.tom.helper.proprogresseditfragment.ProposalProgressEditViewModel
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.Proposal
import com.tom.helper.source.Task

@Suppress("UNCHECKED_CAST")
class TaskViewModelFactory(
    private val helperRepository: HelperRepository,
    private val task: Task
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ProposalEditViewModel::class.java)) {
            return ProposalEditViewModel(helperRepository, task) as T
        }

        if (modelClass.isAssignableFrom(ProposalListViewModel::class.java)) {
            return ProposalListViewModel(helperRepository, task) as T
        }

        if (modelClass.isAssignableFrom(ChatRoomViewModel::class.java)) {
            return ChatRoomViewModel(helperRepository, task) as T
        }

        if (modelClass.isAssignableFrom(ChatListViewModel::class.java)) {
            return ChatListViewModel(helperRepository, task) as T
        }




        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}