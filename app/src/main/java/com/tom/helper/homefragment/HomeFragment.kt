package com.tom.helper.homefragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tom.helper.MainActivity
import com.tom.helper.R
import com.tom.helper.databinding.FragmentHomeBinding
import com.tom.helper.ext.getVmFactory
import com.tom.helper.homefragment.pager.HomeViewPagerAdapter
import com.tom.helper.source.Task

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    /**
     * Lazily initialize our [HomeViewModel].
     */
    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }


//    private val viewModel by lazy {
//        ViewModelProviders.of(this).get(HomeViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.homeRequestRecycler.layoutManager = LinearLayoutManager(context)
        binding.homeRequestRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.homeRequestRecycler.adapter =
            HomeRecyclerAdapter(
                HomeRecyclerAdapter.OnClickListener {

                    //press buttonMissionDetail of item_request.xml, and it'll navigate to job details fragment
//                    findNavController().navigate(HomeFragmentDirections.actionGlobalJobDetailFragment(it))
                },
                viewModel
            )

        //try to handle when button_mission_detail in item_request.xml is pressed, will navigate to JobDetailFragment(see HomeFragment.kt)
        viewModel.shouldNavigateToJobDetail.observe(this, Observer {
            it?.let {

                findNavController().navigate(HomeFragmentDirections.actionGlobalJobDetailFragment(it))

                viewModel.doneNavigatingToJobDetail()

            }
        })


        //try to handle when button_mission_detail_proposal_total in item_request.xml is pressed, will navigate to ProposalListFragment(see HomeViewModel.kt)
        viewModel.shouldNavigateToProposalList.observe(this, Observer {
            it?.let {

                findNavController().navigate(
                    HomeFragmentDirections.actionGlobalProposalListFragment(
                        it
                    )
                )

                viewModel.doneNavigatingToProposalList()

            }
        })


        //    var displayClothSize = Transformations.map(selectedProduct){
//        when (it.sizes.count()){
//            0 -> ""
//            1 -> it.sizes.first()
//            else ->  "${it.sizes.first()} - ${it.sizes.last()}"
//
//        }
//    }
//


        //if there's no BindingAdapters(tasks), I would need the following code to do submitList(it) job.
//        viewModel.tasks.observe(this, Observer {
//            (binding.homeRequestRecycler.adapter as? HomeRecyclerAdapter)?.submitList(it)
//
//        })


        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {


            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("onTabSelected","onTabSelected")
                when (tab!!.position) {

                    0 -> viewModel.getTasksResult()
                    1 -> viewModel.getOnGoingTasksResult()
                    2 -> viewModel.getFinishedTasksResult()
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

        })

//        viewModel.prepareTasks()
        viewModel.getTasksResult()

        //handle changing the title while selecting HomeFragment
        (activity as MainActivity).setLogo(MainActivity.EnumCheck.HOME)

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val viewPager = view.findViewById<ViewPager>(R.id.tab_layout_catalog_viewpager)
//        viewPager.adapter = HomeViewPagerAdapter(childFragmentManager)
//
//        val tabLayout = view.findViewById<TabLayout>(R.id.tabs)
//        tabLayout.setupWithViewPager(viewPager)
//
//    }


}
