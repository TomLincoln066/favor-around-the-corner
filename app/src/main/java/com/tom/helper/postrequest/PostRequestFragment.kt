package com.tom.helper.postrequest


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tom.helper.MainActivity

import com.tom.helper.R

/**
 * A simple [Fragment] subclass.
 */
class PostRequestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //handle changing the title while selecting PostRequestFragment
        (activity as MainActivity).setLogo(MainActivity.EnumCheck.POSTREQUEST)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_request, container, false)
    }


}
