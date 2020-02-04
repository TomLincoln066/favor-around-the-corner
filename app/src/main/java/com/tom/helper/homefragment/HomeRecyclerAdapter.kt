package com.tom.helper.homefragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.helper.databinding.ItemRequestBinding
import com.tom.helper.source.Task

//* This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
//* [Task], including computing diffs between lists.
//* @param onClickListener a lambda that takes the
//*/

class HomeRecyclerAdapter(
    private val onClickListener: OnClickListener,
    val viewModel: HomeViewModel
) :
    ListAdapter<Task, RecyclerView.ViewHolder>(DiffCallback) {
    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Article]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Article]
     */
    class OnClickListener(val clickListener: (task: Task) -> Unit) {
        fun onClick(task: Task) = clickListener(task)
    }

    class TaskViewHolder(private var binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, onClickListener: OnClickListener, viewModel: HomeViewModel) {


            //give HomeRecyclerAdapter HomeViewModel so item_request.xml would show price.
            binding.viewModel = viewModel

            binding.task = task
            //press buttonMissionDetail and do what HomeFragment about to do, pls check HomeFragment.kt for the code following.
            /**
             *        binding.homeRequestRecycler.adapter = HomeRecyclerAdapter(HomeRecyclerAdapter.OnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalJobDetailFragment(it))
            })
             */

            binding.buttonMissionDetail.setOnClickListener { onClickListener.onClick(task) }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_TASK = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_TASK -> TaskViewHolder(
                ItemRequestBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Log.i("TEST ADAPTER","TASK $position = ${getItem(position)}" )

        when (holder) {
            is TaskViewHolder -> {
                holder.bind((getItem(position)), onClickListener, viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_TASK
    }
}