package com.tom.helper.logindialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tom.helper.MainActivity
import com.tom.helper.databinding.FragmentFacebookLogInBinding

class FacebookLogInBottomSheet : BottomSheetDialogFragment() {



    lateinit var binding: FragmentFacebookLogInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFacebookLogInBinding.inflate(inflater,container,false)

        return binding.root
    }



}