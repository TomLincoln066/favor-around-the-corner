package com.tom.helper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.crashlytics.android.Crashlytics
import com.facebook.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.databinding.ActivityMainBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.logindialog.FacebookLogInBottomSheet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_facebook_log_in.*

class MainActivity : AppCompatActivity() {


    val viewModel by viewModels<MainActivityViewModel> { getVmFactory() }


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


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navController = findNavController(R.id.myNavHostFragment)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.navigationView)

//        if you match menu item id in bottom_menu.xml  with  fragment id in navigation.xml, then you only need the code of following line to do the navigating job.
//        bottomNavigation.setupWithNavController(navController)


        //check whether user is logged in

        val authListener: FirebaseAuth.AuthStateListener =
            FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
                val user: FirebaseUser? = auth.currentUser
                if (user == null) {
                    supportFragmentManager.let {
                        FacebookLogInBottomSheet().show(it, "supportFragmentManager")
                    }
                    Log.d("signInWithCredential", "signInWithCredential:no")
                } else {
                    viewModel.checkUserResult()

                    viewModel.getCurrentUserData()

                }
            }
        FirebaseAuth.getInstance().addAuthStateListener(authListener)


        //let FacebookLogInBottomSheetDialog show up when open app
//        supportFragmentManager.let {
//            FacebookLogInBottomSheet().show(it, "supportFragmentManager")
//        }


        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth] callback manager is an object, it contains many functions for specific events.
        callbackManager = CallbackManager.Factory.create()



        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fragment_profile -> {

//                    supportFragmentManager.let {
//                        FacebookLogInBottomSheet().show(it, "supportFragmentManager")
//                    }
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

//                R.id.fragment_my_proposals -> {
//                    navController.navigate(R.id.action_global_myProposalsFragment)
//                    true
//                }


                else -> {
                    false
                }


            }

        }


//        val crashButton = Button(this)
//        crashButton.text = "Crash!"
//        crashButton.setOnClickListener {
//            Crashlytics.getInstance().crash() // Force a crash
//        }
//
//        addContentView(crashButton, ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT))
//




    }


    //handle changing the title while selecting different fragments
    enum class EnumCheck {
        HOME, POSTREQUEST, RANKINGLIST, PROFILE, MYPROPOSALS, JOBDETAILS, PROPOSALEDIT, PROPOSALLIST, PROPOSALPROGRESSEDIT, PROPOSALPROGRESSLIST
    }

    fun setLogo(forCheck: EnumCheck) {

        when (forCheck) {
            EnumCheck.HOME -> {//when selected home
                binding.title.visibility = View.VISIBLE
                binding.textViewTitleChangeable.visibility = View.GONE
//                binding.title.visibility = View.INVISIBLE
//                binding.textViewTitleChangeable.visibility = View.VISIBLE
//                binding.textViewTitleChangeable.text = "Kraffier"



            }
            EnumCheck.POSTREQUEST -> {//postrequest
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "新增案件"
            }
            EnumCheck.RANKINGLIST -> {//rankinglist
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "排行榜"
            }
            EnumCheck.MYPROPOSALS -> {//myproposals
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "我的提案"
            }

            EnumCheck.PROFILE -> {//profile
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "個人"
            }

            EnumCheck.JOBDETAILS -> {//JOBDETAILS
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "案件細節"
            }

            EnumCheck.PROPOSALEDIT -> {//PROPOSALEDIT
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "發出提案"
            }

            EnumCheck.PROPOSALLIST -> {//PROPOSALLIST
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "提案列表"
            }

            EnumCheck.PROPOSALPROGRESSEDIT -> {//PROPOSALPROGRESSEDIT
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "進度編輯"
            }

            EnumCheck.PROPOSALPROGRESSLIST -> {//PROPOSALPROGRESSLIST
                binding.title.visibility = View.INVISIBLE
                binding.textViewTitleChangeable.visibility = View.VISIBLE
                binding.textViewTitleChangeable.text = "進度事項"
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

