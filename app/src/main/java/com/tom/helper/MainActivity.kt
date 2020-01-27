package com.tom.helper

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tom.helper.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.myNavHostFragment)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.navigationView)

//        if you match menu item id in bottom_menu.xml  with  fragment id in navigation.xml, then you only need the code of following line to do the navigating job.
//        bottomNavigation.setupWithNavController(navController)


        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.fragment_profile ->{
//                    if (!UserManager.isLoggedIn()){
//                        fragment = FacebookBottomSheet.newInstance()
//                        fragment.show(supportFragmentManager, "facebook_login")
//                        false
//                    } else {
                        navController.navigate(R.id.action_global_profileFragment)
                        true
                    }


                R.id.fragment_home ->{
                    navController.navigate(R.id.action_global_homeFragment)
                    true
                }

                R.id.fragment_post_request ->{
                    navController.navigate(R.id.action_global_postRequestFragment)
                    true
                }

                R.id.fragment_ranking_list ->{
                    navController.navigate(R.id.rankingListFragment)
                    true
                }
                else ->{
                    false
                }


            }

        }


    }
}

//        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
//        val viewPager: ViewPager = findViewById(R.id.view_pager)
//        viewPager.adapter = sectionsPagerAdapter
//        val tabs: TabLayout = findViewById(R.id.tabs)
//        tabs.setupWithViewPager(viewPager)

//        val fab: FloatingActionButton = findViewById(R.id.fab)

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }