package com.tom.helper.logindialog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.tom.helper.HelperApplication
import com.tom.helper.LoadApiStatus
import com.tom.helper.MainActivity
import com.tom.helper.databinding.FragmentFacebookLogInBinding
import kotlinx.android.synthetic.main.fragment_facebook_log_in.*

class FacebookLogInBottomSheet : BottomSheetDialogFragment() {


    lateinit var binding: FragmentFacebookLogInBinding



    override fun onStart() {
        super.onStart()
        recalculateSize()
    }

    //to handle dialog push up to match full screen
    private fun recalculateSize() {
        (dialog as? BottomSheetDialog)?.let {
            val bottomSheet =
                it.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { view ->
                view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
        val view = view
        view?.let {
            it.post {
                kotlin.run {
                    val parent = it.parent as View
                    val params = parent.layoutParams as CoordinatorLayout.LayoutParams
                    val behavior = params.behavior
                    val bottomSheetBehavior = behavior as BottomSheetBehavior
                    bottomSheetBehavior.peekHeight = view.measuredHeight
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFacebookLogInBinding.inflate(inflater, container, false)
        binding.facebookLogInDialog = this
        binding.lifecycleOwner = this
        binding.closeLoginWindow.setOnClickListener {
            (it as? Button)?.let { button ->
                funOnClick(button)
            }
        }


        return binding.root


    }


    //facebook login
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        val callbackManager = (activity as MainActivity).callbackManager
        Button_facebook_log_in.setPermissions("email", "public_profile")

        Button_facebook_log_in.loginBehavior = LoginBehavior.WEB_ONLY

        Button_facebook_log_in.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("Will", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)

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

                    //when user logged in, dismiss this dialog fragment.
                    this.dismiss()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Will", "signInWithCredential:failure", task.exception)

                }

            }
    }


    fun funOnClick(view: View) {
//        if (view.id == binding.ButtonFacebookLogIn.id) {
//            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
//        } else {
        dismiss()
//        }
    }


}