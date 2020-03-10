package com.tom.helper.chatroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.helper.databinding.FragmentChatRoomBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.source.Task

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


        binding.privateMessagesListOfUsersRecycler.adapter =
            ChatRoomRecyclerAdapter(
                ChatRoomRecyclerAdapter.OnClickListener {


                },
                viewModel
            )


//        viewModel.addMessages()


        viewModel.getMessages()

        // Inflate the layout for this fragment

        return binding.root

    }


}
