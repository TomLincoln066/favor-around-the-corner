package com.tom.helper.chatlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.helper.databinding.FragmentChatListBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.source.Task

class ChatListFragment : Fragment() {


    private val viewModel by viewModels<ChatListViewModel> {
        getVmFactory(
            ChatListFragmentArgs.fromBundle(arguments!!).task
        )
    }


    private lateinit var task: Task


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        task = requireArguments().get("task") as Task

        val binding = FragmentChatListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.manageMyMessagesRecycler.layoutManager = LinearLayoutManager(context)


        binding.manageMyMessagesRecycler.adapter =
            ChatListRecyclerAdapter(
                ChatListRecyclerAdapter.OnClickListener {


                },
                viewModel
            )



        viewModel.getChatUser()




        return binding.root

    }


}