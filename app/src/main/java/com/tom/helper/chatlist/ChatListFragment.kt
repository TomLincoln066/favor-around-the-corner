package com.tom.helper.chatlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tom.helper.R

class ChatListFragment : Fragment() {


//    private val viewModel by viewModels<ChatRoomViewModel> {
//        getVmFactory(
//            ChatRoomFragmentArgs.fromBundle(arguments!!).task
//        )
//    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }



}
