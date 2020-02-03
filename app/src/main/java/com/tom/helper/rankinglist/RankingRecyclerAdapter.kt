package com.tom.helper.rankinglist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.helper.databinding.ItemRankingListBinding
import com.tom.helper.source.Rank

class RankingRecyclerAdapter(private val onClickListener: OnClickListener ) :
    ListAdapter<Rank, RecyclerView.ViewHolder>(DiffCallback) {
    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Article]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Article]
     */
    class OnClickListener(val clickListener: (rank : Rank) -> Unit) {
        fun onClick(rank: Rank) = clickListener(rank)
    }

    class RankViewHolder(private var binding: ItemRankingListBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rank: Rank, onClickListener: OnClickListener) {

            binding.rank = rank

            //handle earning's Long to String , see item_ranking_list.xml <data> name="earning" type="String"  & android:text="@{earning}"
            binding.earning = rank.earning.toString()

            binding.root.setOnClickListener { onClickListener.onClick(rank) }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Rank>() {
        override fun areItemsTheSame(oldItem: Rank, newItem: Rank): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Rank, newItem: Rank): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_TASK = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_TASK -> RankViewHolder(
                ItemRankingListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is RankViewHolder -> {
                holder.bind((getItem(position) as Rank), onClickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_TASK
    }
}