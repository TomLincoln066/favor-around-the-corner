package com.tom.helper.logindialog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.tom.helper.MainActivity
import com.tom.helper.databinding.FragmentFacebookLogInBinding
import kotlinx.android.synthetic.main.fragment_facebook_log_in.*

class FacebookLogInBottomSheet : BottomSheetDialogFragment() {


    lateinit var binding: FragmentFacebookLogInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFacebookLogInBinding.inflate(inflater, container, false)
        return binding.root
    }


    //facebook login

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val callbackManager = (activity as MainActivity).callbackManager
        Button_facebook_log_in.setPermissions("email", "public_profile")
        Button_facebook_log_in.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("Will", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
//                Log.d("Auth","${auth.currentUser}")
            }

            override fun onCancel() {
                Log.d("Will", "facebook:onCancel")

            }

            override fun onError(error: FacebookException) {
                Log.d("Will", "facebook:onError", error)

            }
        })




    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val auth = (activity as MainActivity).auth


        Log.d("Will", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity as MainActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Will", "signInWithCredential:success")
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Will", "signInWithCredential:failure", task.exception)
//                    Toast.makeText(
//                        baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            }
    }







}