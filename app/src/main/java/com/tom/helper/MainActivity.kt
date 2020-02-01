package com.tom.helper

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.tom.helper.databinding.ActivityMainBinding
import com.tom.helper.logindialog.FacebookLogInBottomSheet
import com.tom.helper.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navController = findNavController(R.id.myNavHostFragment)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.navigationView)

//        if you match menu item id in bottom_menu.xml  with  fragment id in navigation.xml, then you only need the code of following line to do the navigating job.
//        bottomNavigation.setupWithNavController(navController)


        //let FacebookLogInBottomSheetDialog show up when open app
        supportFragmentManager.let {
            FacebookLogInBottomSheet().show(it,"Will")
        }


        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fragment_profile -> {
//                    if (!UserManager.isLoggedIn()){
//                        fragment = FacebookBottomSheet.newInstance()
//                        fragment.show(supportFragmentManager, "facebook_login")
//                        false
//                    } else {
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


}

