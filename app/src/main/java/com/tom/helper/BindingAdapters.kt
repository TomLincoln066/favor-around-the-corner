package com.tom.helper

import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.tom.helper.homefragment.HomeRecyclerAdapter
import com.tom.helper.proposallistfragment.ProposalListRecyclerAdapter
import com.tom.helper.rankinglist.RankingRecyclerAdapter
import com.tom.helper.source.Proposal
import com.tom.helper.source.Rank
import com.tom.helper.source.Task

//fragment_home.xml in use
@BindingAdapter("tasks")
fun bindRecyclerView(recyclerView: RecyclerView, homeItems: List<Task>?) {
    homeItems?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is HomeRecyclerAdapter -> submitList(it)
            }
        }
    }
}

//fragment_ranking_list in use
//@BindingAdapter("addRankUser")
//fun bindRankingFragment(recyclerView: RecyclerView, homeItems: List<Rank>?) {
//    homeItems?.let {
//        recyclerView.adapter?.apply {
//            when (this) {
//                is RankingRecyclerAdapter -> submitList(it)
//                else -> {
//                    Log.i("SOMETHNG", "")
//                }
//            }
//        }
//    }
//}



//fragment_ranking_list in use
@BindingAdapter("addTasksOfMine")
fun bindRankingFragment(recyclerView: RecyclerView, homeItems: List<Task>?) {
    homeItems?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is RankingRecyclerAdapter -> submitList(it)
                else -> {
                    Log.i("SOMETHNG", "")
                }
            }
        }
    }
}



//fragment_proposal_list in use
@BindingAdapter("addProposal")
fun bindProposalFragment(recyclerView: RecyclerView, homeItems: List<Proposal>?) {
    homeItems?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is ProposalListRecyclerAdapter -> submitList(it)
                else -> {
                    Log.i("SOMETHNG", "")
                }
            }
        }
    }
}


/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().build()
        GlideApp.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.chess_png)
                    .error(R.drawable.chess_png))
            .into(imgView)
    }
}