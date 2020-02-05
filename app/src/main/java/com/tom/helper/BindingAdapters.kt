package com.tom.helper

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.helper.homefragment.HomeRecyclerAdapter
import com.tom.helper.proposallistfragment.ProposalListRecyclerAdapter
import com.tom.helper.rankinglist.RankingRecyclerAdapter
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
@BindingAdapter("addRankUser")
fun bindRankingFragment(recyclerView: RecyclerView, homeItems: List<Rank>?) {
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
fun bindProposalFragment(recyclerView: RecyclerView, homeItems: List<Task>?) {
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