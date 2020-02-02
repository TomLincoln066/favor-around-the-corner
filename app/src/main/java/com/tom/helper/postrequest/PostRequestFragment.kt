package com.tom.helper.postrequest


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.MainActivity

import com.tom.helper.R
import com.tom.helper.databinding.FragmentPostRequestBinding
import kotlinx.android.synthetic.main.fragment_post_request.*
import kotlinx.android.synthetic.main.item_request.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class PostRequestFragment : Fragment() {


    private val viewModel: PostRequestViewModel by lazy {
        ViewModelProviders.of(this).get(PostRequestViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =FragmentPostRequestBinding.inflate(inflater,container,false)

        //handle changing the title while selecting PostRequestFragment
        (activity as MainActivity).setLogo(MainActivity.EnumCheck.POSTREQUEST)

//        binding.lifecycleOwner=this
//        binding.viewModel = viewModel
        binding.buttonPostRequestSend.setOnClickListener {
            sendNewRequest()

        }




        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_post_request, container, false)
        return binding.root

    }


    fun sendNewRequest(){


        val db = FirebaseFirestore.getInstance()

        val task = FirebaseFirestore.getInstance().collection("task")

        val document = task.document()

        val data = hashMapOf(
            "task_provider" to "Tom",
            "task_title" to "我的電腦crashes \n" +
                    "Help me!!! \"",
            "task_content" to " 我的電腦突然沒辦法開機，徵求勇士一名，酬勞另計。 ",
            "task_createTime" to Calendar.getInstance()
                .timeInMillis,
            "task_price" to "20000",
            "task_id" to document.id

        )

        document.set(data as Map<String, Any>)


    }

    private fun convertLongToDateString(systemTime: Long): String {
        return SimpleDateFormat("MMM-dd-yyyy HH:mm:ss")
            .format(systemTime).toString()
    }




}


