package com.tom.helper.postrequest


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.MainActivity
import com.tom.helper.databinding.FragmentPostRequestBinding
import com.tom.helper.ext.getVmFactory
import kotlinx.android.synthetic.main.fragment_post_request.*
import kotlinx.android.synthetic.main.item_request.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class PostRequestFragment : Fragment() {


    //Stylish  method of creating ViewModel
//    private val viewModel: PostRequestViewModel by lazy {
//        ViewModelProviders.of(this).get(PostRequestViewModel::class.java)
//    }


    /**
     * Lazily initialize our [HomeViewModel].
     */
    private val viewModel by viewModels<PostRequestViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPostRequestBinding.inflate(inflater, container, false)

        //handle changing the title while selecting PostRequestFragment
        (activity as MainActivity).setLogo(MainActivity.EnumCheck.POSTREQUEST)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //try to handle when button_post_request_send is clicked in fragment_post_request.xml is pressed, will navigate to Home Fragment
//        viewModel.shouldNavigateToHomeFragment.observe(this, Observer {
//            it?.let {
//
//                findNavController().navigate(PostRequestFragmentDirections.actionGlobalHomeFragment())
//
//                viewModel.doneNavigatingToHomeFragment()
//
//            }
//        })

        viewModel.shouldNavigateToHomeFragment.observe(this, Observer {
            if (it){
                findNavController().navigate(PostRequestFragmentDirections.actionGlobalHomeFragment())
            }
        })









        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_post_request, container, false)
        return binding.root

    }


    // send some mock data to fireBase
    fun sendNewRequest() {


        val task = FirebaseFirestore.getInstance().collection("task")

        val document = task.document()

        val data = hashMapOf(
            "task_provider" to "Tom",
            "task_title" to "我的電腦crashes \n" +
                    "Help me!!! \"",
            "task_content" to " 我的電腦突然沒辦法開機，徵求勇士一名，酬勞另計。 ",
            "task_createTime" to Calendar.getInstance().timeInMillis,
            "task_price" to "20000",
            "task_id" to document.id

        )

        document.set(data as Map<String, Any>)


    }





}


//        binding.buttonPostRequestSend.setOnClickListener {
//            sendNewRequest()
//            viewModel.submitTask()
//        }