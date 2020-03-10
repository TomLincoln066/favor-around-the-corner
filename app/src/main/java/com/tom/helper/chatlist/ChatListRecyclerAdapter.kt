package com.tom.helper.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.helper.databinding.ItemChatUserBinding
import com.tom.helper.source.Message

class ChatListRecyclerAdapter(private val onClickListener: OnClickListener , val viewModel: ChatListViewModel) :
    ListAdapter<Message, RecyclerView.ViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (message: Message) -> Unit) {
        fun onClick(message: Message) = clickListener(message)
    }

    class ChatListViewHolder(private var binding: ItemChatUserBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message, onClickListener: OnClickListener, viewModel: ChatListViewModel) {

//            binding.viewModel = viewModel

            binding.message = message

            binding.root.setOnClickListener { onClickListener.onClick(message)
            }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_MESSAGE = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_MESSAGE -> ChatListViewHolder(
                ItemChatUserBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ChatListViewHolder -> {
                holder.bind((getItem(position) as Message), onClickListener, viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_MESSAGE
    }
}