package com.tom.helper.rankinglist

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.helper.databinding.ItemRankingListBinding
import com.tom.helper.databinding.ItemTaskOfMineBinding
import com.tom.helper.source.Rank
import com.tom.helper.source.Task
import com.tom.helper.source.User

class RankingRecyclerAdapter(
    private val onClickListener: OnClickListener,
    val viewModel: RankingListViewModel
) :
    ListAdapter<User, RecyclerView.ViewHolder>(DiffCallback) {
    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Article]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Article]
     */
    class OnClickListener(val clickListener: (user: User) -> Unit) {
        fun onClick(user: User) = clickListener(user)

    }

    class UserViewHolder(val binding: ItemRankingListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, onClickListener: OnClickListener, viewModel: RankingListViewModel) {


            //give HomeRecyclerAdapter HomeViewModel so item_request.xml would show price.
            binding.viewModel = viewModel

            binding.user = user
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

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        private const val ITEM_VIEW_TYPE_USER = 0x00
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_USER -> UserViewHolder(
                ItemRankingListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Log.i("RankingRecyclerAdapter","USER $position = ${getItem(position)}" )

        when (holder) {
            is UserViewHolder -> {
                holder.bind((getItem(position)), onClickListener, viewModel)
                //display ranking number
                holder.binding.textViewRankingNumber.text = "${position + 1}"
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_USER
    }
}

//
//class RankingRecyclerAdapter(
//    private val onClickListener: OnClickListener,
//    val viewModel: RankingListViewModel
//) :
//    ListAdapter<Task, RecyclerView.ViewHolder>(DiffCallback) {
//    /**
//     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Article]
//     * associated with the current item to the [onClick] function.
//     * @param clickListener lambda that will be called with the current [Article]
//     */
//    class OnClickListener(val clickListener: (task: Task) -> Unit) {
//        fun onClick(task: Task) = clickListener(task)
//
//    }
//
//    class TaskViewHolder(private var binding: ItemTaskOfMineBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(task: Task, onClickListener: OnClickListener, viewModel: RankingListViewModel) {
//
//
//            //give HomeRecyclerAdapter HomeViewModel so item_request.xml would show price.
//            binding.viewModel = viewModel
//
//            binding.task = task
//            //press buttonMissionDetail and do what HomeFragment about to do, pls check HomeFragment.kt for the code following.
//            /**
//             *        binding.homeRequestRecycler.adapter = HomeRecyclerAdapter(HomeRecyclerAdapter.OnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionGlobalJobDetailFragment(it))
//            })
//             */
//
//            binding.buttonMissionDetail.setOnClickListener { onClickListener.onClick(task) }
//
//
//            binding.executePendingBindings()
//
//
//        }
//    }
//
//    companion object DiffCallback : DiffUtil.ItemCallback<Task>() {
//        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
//            return oldItem === newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        private const val ITEM_VIEW_TYPE_TASK = 0x00
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            ITEM_VIEW_TYPE_TASK -> TaskViewHolder(
//                ItemTaskOfMineBinding.inflate(
//                    LayoutInflater.from(parent.context), parent, false
//                )
//            )
//            else -> throw ClassCastException("Unknown viewType $viewType")
//        }
//    }
//
//    /**
//     * Replaces the contents of a view (invoked by the layout manager)
//     */
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        Log.i("RankingRecyclerAdapter","TASK $position = ${getItem(position)}" )
//
//        when (holder) {
//            is TaskViewHolder -> {
//                holder.bind((getItem(position)), onClickListener, viewModel)
//            }
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return ITEM_VIEW_TYPE_TASK
//    }
//}