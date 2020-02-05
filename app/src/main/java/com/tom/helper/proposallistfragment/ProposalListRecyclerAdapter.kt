package com.tom.helper.proposallistfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.helper.databinding.ItemProposalBinding
import com.tom.helper.source.Task

class ProposalListRecyclerAdapter(private val onClickListener: OnClickListener ) :
    ListAdapter<Task, RecyclerView.ViewHolder>(DiffCallback) {
    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Article]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Article]
     */
    class OnClickListener(val clickListener: (task: Task) -> Unit) {
        fun onClick(task: Task) = clickListener(task)
    }

    class ProposalViewHolder(private var binding: ItemProposalBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, onClickListener: OnClickListener) {

            binding.task = task


            binding.root.setOnClickListener { onClickListener.onClick(task) }
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
            ITEM_VIEW_TYPE_TASK -> ProposalViewHolder(
                ItemProposalBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ProposalViewHolder -> {
                holder.bind((getItem(position) as Task), onClickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_TASK
    }
}