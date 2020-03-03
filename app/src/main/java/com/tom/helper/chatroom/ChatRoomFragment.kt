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



//    private val viewModel by viewModels<ChatRoomViewModel> { getVmFactory() }


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

                    //press buttonMissionDetail of item_request.xml, and it'll navigate to job details fragment
//                    findNavController().navigate(HomeFragmentDirections.actionGlobalJobDetailFragment(it))
                },
                viewModel
            )


//        viewModel.addMessages()


        viewModel.getMessages()

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat_room, container, false)
        return binding.root

    }



}
