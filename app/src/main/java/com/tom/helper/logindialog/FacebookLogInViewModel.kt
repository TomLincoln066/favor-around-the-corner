package com.tom.helper.logindialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FacebookLogInViewModel: ViewModel(){
//    private  val _data = MutableLiveData<UserSignInResult>()
//
//    val data: LiveData<UserSignInResult>
//        get()=_data
//
//
//    private var viewModelJob = Job()
//
//    private val coroutineScope = CoroutineScope(viewModelJob+ Dispatchers.Main)
//
//
//    fun getToken(facebookToken:String){
//        val body= UserSignInBody(provider = "facebook", accessToken = facebookToken)
//        coroutineScope.launch{
//            try{
//
//                var userSignInResult = RetrofitApi.retrofitService.signInUser(body)
//                _data.value = userSignInResult
//                Log.i("stylishToken","FaceBookViewModel, userSignInResult=${userSignInResult.data?.accessToken}")
//            }catch (e:Exception){
//                Log.i("FB EXCEPTION","exception=${e}")
//                _data.value =null
//            }
//        }
//
//    }


}