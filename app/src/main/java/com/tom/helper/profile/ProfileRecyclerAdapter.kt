package com.tom.helper.profile

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.helper.databinding.ItemTaskOfMyForProfilePageBinding
import com.tom.helper.source.Task

class ProfileRecyclerAdapter(
    private val onClickListener: OnClickListener,
    val viewModel: ProfileViewModel
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

    class TaskViewHolder(private var binding: ItemTaskOfMyForProfilePageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, onClickListener: OnClickListener, viewModel: ProfileViewModel) {


            //give HomeRecyclerAdapter HomeViewModel so item_request.xml would show price.
            binding.viewModel = viewModel

            binding.task = task
            //press buttonMissionDetail and do what HomeFragment about to do, pls check HomeFragment.kt for the code following.
            /**
             *        binding.homeRequestRecycler.adapter = HomeRecyclerAdapter(HomeRecyclerAdapter.OnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalJobDetailFragment(it))
            })
             */

//            binding.buttonMissionDetail.setOnClickListener { onClickListener.onClick(task) }


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
                ItemTaskOfMyForProfilePageBinding.inflate(
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

        Log.i("ProfileRecyclerAdapter","TASK $position = ${getItem(position)}" )

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