package com.tom.helper

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import android.view.View

import androidx.databinding.DataBindingUtil

import androidx.navigation.findNavController

import com.facebook.*

import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.databinding.ActivityMainBinding

import com.tom.helper.logindialog.FacebookLogInBottomSheet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_facebook_log_in.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    //facebook login
    lateinit var callbackManager: CallbackManager
        private set

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }


//    lateinit var bindingFacebook : FragmentFacebookLogInBinding

    // [START declare_auth]
    lateinit var auth: FirebaseAuth
        private set
    // [END declare_auth]


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        FacebookSdk.sdkInitialize(getApplicationContext())
//            AppEventsLogger.activateApp(this)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navController = findNavController(R.id.myNavHostFragment)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.navigationView)

//        if you match menu item id in bottom_menu.xml  with  fragment id in navigation.xml, then you only need the code of following line to do the navigating job.
//        bottomNavigation.setupWithNavController(navController)


        //let FacebookLogInBottomSheetDialog show up when open app
        supportFragmentManager.let {
            FacebookLogInBottomSheet().show(it, "supportFragmentManager")
        }


        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth] callback manager is an object, it contains many functions for specific events.
        callbackManager = CallbackManager.Factory.create()



        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fragment_profile -> {

//                    if (!UserManager.isLoggedIn()){
//                        fragment = FacebookLogInBottomSheet.newInstance()
//                        fragment.show(supportFragmentManager, "facebook_login")
//                        false
//                    } else {
                    supportFragmentManager.let {
                        FacebookLogInBottomSheet().show(it, "supportFragmentManager")
                    }
                    navController.navigate(R.id.action_global_profileFragment)
                    true
                }


                R.id.fragment_home -> {
                    navController.navigate(R.id.action_global_homeFragment)
                    true
                }

                R.id.fragment_post_request -> {
                    navController.navigate(R.id.action_global_postRequestFragment)
                    true
                }

                R.id.fragment_ranking_list -> {
                    navController.navigate(R.id.rankingListFragment)
                    true
                }
                else -> {
                    false
                }


            }

        }


    }


    //handle changing the title while selecting different fragments
    enum class EnumCheck {
        HOME, POSTREQUEST, RANKINGLIST, PROFILE
    }

    fun setLogo(forCheck: EnumCheck) {

        when (forCheck) {
            EnumCheck.HOME -> {//when selected home
                binding.title.visibility = View.VISIBLE
                binding.textViewTitleChangeable.visibility = View.GONE
            }
            EnumCheck.POSTREQUEST -> {//postrequest
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "新增任務"
            }
            EnumCheck.RANKINGLIST -> {//rankinglist
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "工具榜"
            }
            EnumCheck.PROFILE -> {//profile
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "個人頁面"
            }

        }
    }


    //    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

    }
    // [END on_start_check_user]


}

