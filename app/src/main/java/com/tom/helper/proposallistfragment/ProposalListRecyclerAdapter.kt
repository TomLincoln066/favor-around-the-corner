package com.tom.helper.proposallistfragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.helper.databinding.ItemProposalBinding
import com.tom.helper.source.Proposal


class ProposalListRecyclerAdapter(private val onClickListener: OnClickListener ,val viewModel: ProposalListViewModel ) :
    ListAdapter<Proposal, RecyclerView.ViewHolder>(DiffCallback) {
    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Article]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Article]
     */
    class OnClickListener(val clickListener: (proposal: Proposal) -> Unit) {
        fun onClick(proposal: Proposal) = clickListener(proposal)
    }

    class ProposalViewHolder(private var binding: ItemProposalBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(proposal: Proposal, onClickListener: OnClickListener, viewModel: ProposalListViewModel) {

            binding.proposal = proposal
            binding.viewModel =viewModel

            Log.i("BINDING", "PROPOSAL id=${proposal.user?.id}")
            Log.i("BINDING", "VIEWMODEL id=${viewModel.profile.value?.id}")

            val userCurrentBoolean = (proposal.user?.id == viewModel.profile.value?.id) || (proposal.taskOnwerID != viewModel.profile.value?.id)
            binding.userCurrentBoolean = userCurrentBoolean


//            binding.root.setOnClickListener { onClickListener.onClick(proposal) }

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Proposal>() {
        override fun areItemsTheSame(oldItem: Proposal, newItem: Proposal): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Proposal, newItem: Proposal): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_PROPOSAL = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_PROPOSAL -> ProposalViewHolder(
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
                holder.bind((getItem(position) as Proposal), onClickListener,viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_PROPOSAL
    }
}