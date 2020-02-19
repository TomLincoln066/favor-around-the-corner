package com.tom.helper.taskprogressdialog

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.helper.databinding.ItemProposalProgressBinding
import com.tom.helper.source.ProposalProgressContent

class ProProgressRecyclerAdapter(
    private val onClickListener: OnClickListener,
    val viewModel: ProProgressViewModel
) :
    ListAdapter<ProposalProgressContent, RecyclerView.ViewHolder>(DiffCallback) {



    class OnClickListener(val clickListener: (proposalProgressContent:ProposalProgressContent) -> Unit) {
        fun onClick(proposalProgressContent:ProposalProgressContent) = clickListener(proposalProgressContent)

    }

    class ProposalProgressViewHolder(private var binding: ItemProposalProgressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(proposalProgressContent:ProposalProgressContent, onClickListener: OnClickListener, viewModel: ProProgressViewModel) {


            //give HomeRecyclerAdapter HomeViewModel so item_request.xml would show price.
            binding.viewModel = viewModel

            binding.proposalProgressContent = proposalProgressContent

            //press buttonMissionDetail and do what HomeFragment about to do, pls check HomeFragment.kt for the code following.
            /**
             *        binding.homeRequestRecycler.adapter = HomeRecyclerAdapter(HomeRecyclerAdapter.OnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalJobDetailFragment(it))
            })
             */

            binding.buttonItemProposalProgressComplete.setOnClickListener { onClickListener.onClick(proposalProgressContent) }


            binding.executePendingBindings()


        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ProposalProgressContent>() {
        override fun areItemsTheSame(oldItem: ProposalProgressContent, newItem: ProposalProgressContent): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ProposalProgressContent, newItem: ProposalProgressContent): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_PROGRESS = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_PROGRESS -> ProposalProgressViewHolder(
                ItemProposalProgressBinding.inflate(
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

        Log.i("ProProgressAdapter","ProposalProgressContent $position = ${getItem(position)}" )

        when (holder) {
            is ProposalProgressViewHolder -> {
                holder.bind((getItem(position)), onClickListener, viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_PROGRESS
    }
}