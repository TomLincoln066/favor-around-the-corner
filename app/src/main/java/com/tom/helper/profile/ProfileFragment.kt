package com.tom.helper.profile


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tom.helper.MainActivity

import com.tom.helper.R
import com.tom.helper.databinding.FragmentProfileBinding
import com.tom.helper.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {


    private val viewModel by viewModels<ProfileViewModel>{getVmFactory()}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =FragmentProfileBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.profileMyTasksRecycler.layoutManager = LinearLayoutManager(context)
        binding.profileMyTasksRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.profileMyTasksRecycler.adapter =
            ProfileRecyclerAdapter(ProfileRecyclerAdapter.OnClickListener {
                //            Logger.d("click, it=$it")
                //            viewModel.delete(it)
            },viewModel)



        viewModel.shouldFinishThisTask.observe(this, Observer {
            Log.d("Will", "ProfileFragment.viewModel.shouldFinishThisTask.observe, it=${it}")
            it?.let {


            }
        })








//        viewModel.prepareTaskTest()
        viewModel.getTasksOfMineResult()




        //try to handle when button_mission_detail_proposal_total in item_request.xml is pressed, will navigate to ProposalListFragment(see HomeViewModel.kt)
        viewModel.shouldNavigateToProposalList.observe(this, Observer {
            it?.let {

                findNavController().navigate(
                    ProfileFragmentDirections.actionGlobalProposalListFragment(
                        it
                    )
                )

                viewModel.doneNavigatingToProposalList()

            }
        })



        //handle changing the title while selecting RankingListFragment
        (activity as MainActivity).setLogo(MainActivity.EnumCheck.PROFILE)



        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_ranking_list, container, false)
        return binding.root
    }



}
