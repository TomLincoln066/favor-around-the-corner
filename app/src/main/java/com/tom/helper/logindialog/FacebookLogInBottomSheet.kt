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


//    lateinit var callbackManager: CallbackManager
//    lateinit var binding: FragmentFacebookLogInBinding
//
//    private val viewModel: FacebookLogInViewModel by lazy {
//        ViewModelProviders.of(this).get(FacebookLogInViewModel::class.java)
//    }
//
//    companion object {
//        fun newInstance(): FacebookLogInBottomSheet =
//            FacebookLogInBottomSheet().apply {
//
//            }
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//
//        binding = FragmentFacebookLogInBinding.inflate(inflater)
////        binding.faceboolLoginView = this
//        binding.lifecycleOwner = this
//
//        callbackManager = CallbackManager.Factory.create()
//
//        LoginManager.getInstance().registerCallback(callbackManager,
//            object : FacebookCallback<LoginResult> {
//                override fun onSuccess(loginResult: LoginResult) {
//
//
//                    viewModel.getToken(loginResult.accessToken.token)
//                    Log.d("FBlogin", "${loginResult?.accessToken?.token}")
//
//
//                }
//
//                override fun onCancel() {
//                    // App code
//                    Log.d("FBlogin", "Facebook login Cancel")
//
//                }
//
//                override fun onError(exception: FacebookException) {
//                    // App code
//                    Log.d("FBlogin", "Facebook login Error")
//
//                }
//            })
//
//        viewModel.data.observe(this, Observer {
//            Log.d("Will", " viewModel.data.observe, it=$it")
//            UserManager.stylishToken = it.data?.accessToken
//            (activity as MainActivity).showDialog()
//
//
//            Log.d(
//                "Will",
//                "viewModel.data.observe, UserManager.stylishToken=${UserManager.stylishToken}"
//            )
//
//            (activity as MainActivity).changeToProfile()
//        })
//
//        return binding.root
//    }
//
//
//    fun onClick(v: View) {
//        if (v.id == binding.facebookLogIn.id) {
//            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
//        } else {
//            dismiss()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//        super.onActivityResult(requestCode, resultCode, data)
//    }


}