package com.tom.helper.homefragment


import android.os.Bundle
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
import com.tom.helper.MainActivity
import com.tom.helper.databinding.FragmentHomeBinding
import com.tom.helper.ext.getVmFactory
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
                    findNavController().navigate(HomeFragmentDirections.actionGlobalJobDetailFragment(it))
                },
                viewModel
            )

        viewModel.shouldNavigateToProposalList.observe(this, Observer {
            it?.let {

                findNavController().navigate(HomeFragmentDirections.actionGlobalProposalListFragment())

                viewModel.doneNavigatingToProposalList()

            }
        })



        //if there's no BindingAdapters(tasks), I would need the following code to do submitList(it) job.
//        viewModel.tasks.observe(this, Observer {
//            (binding.homeRequestRecycler.adapter as? HomeRecyclerAdapter)?.submitList(it)
//
//        })



//        viewModel.prepareTasks()
        viewModel.getTasksResult()

        //handle changing the title while selecting HomeFragment
        (activity as MainActivity).setLogo(MainActivity.EnumCheck.HOME)

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }





}
