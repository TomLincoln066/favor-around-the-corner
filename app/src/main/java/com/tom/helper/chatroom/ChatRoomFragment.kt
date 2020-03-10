package com.tom.helper.chatroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.helper.HelperApplication
import com.tom.helper.databinding.FragmentChatRoomBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.source.Task
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_chat_room.*


/**
 * A simple [Fragment] subclass.
 */
class ChatRoomFragment : Fragment() {


    private val viewModel by viewModels<ChatRoomViewModel> {
        getVmFactory(
            ChatRoomFragmentArgs.fromBundle(arguments!!).task
        )
    }


    private lateinit var task: Task


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        task = requireArguments().get("task") as Task

        val binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.privateMessagesListOfUsersRecycler.layoutManager = LinearLayoutManager(context)

//        binding.privateMessagesListOfUsersRecycler.scrollToPosition(viewModel.messages.value!!.size  )
//        binding.privateMessagesListOfUsersRecycler.smoothScrollToPosition(viewModel.messages.count - 1 )






        val chatRoomRecyclerAdapter =
            ChatRoomRecyclerAdapter(
                ChatRoomRecyclerAdapter.OnClickListener {

                },
                viewModel
            )


        binding.privateMessagesListOfUsersRecycler.adapter = chatRoomRecyclerAdapter



//        viewModel.getMessages()

        viewModel.getMessagesLiveSnapShot()
        viewModel.messages.observe(this, Observer {
            chatRoomRecyclerAdapter.submitList(it)
        })


        binding.privateMessagesListOfUsersRecycler.scrollToPosition(chatRoomRecyclerAdapter.getItemCount() -1 )



        // Inflate the layout for this fragment

        return binding.root

    }


}
