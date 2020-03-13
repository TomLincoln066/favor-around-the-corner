package com.tom.helper.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tom.helper.chatroom.ChatRoomViewModel
import com.tom.helper.source.HelperRepository

@Suppress("UNCHECKED_CAST")
class ChatRoomViewModelFactory(
    private val helperRepository: HelperRepository,
    private val taskID: String,
    private val chatRoomID: String

) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {




        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}