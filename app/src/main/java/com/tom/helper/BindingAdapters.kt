package com.tom.helper

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tom.helper.chatlist.ChatListRecyclerAdapter
import com.tom.helper.chatroom.ChatRoomRecyclerAdapter
import com.tom.helper.homefragment.HomeRecyclerAdapter
import com.tom.helper.profile.ProfileRecyclerAdapter
import com.tom.helper.proposallistfragment.ProposalListRecyclerAdapter
import com.tom.helper.rankinglist.RankingRecyclerAdapter
import com.tom.helper.source.*
import com.tom.helper.taskprogressdialog.ProProgressRecyclerAdapter

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


//fragment_home.xml in use
@BindingAdapter("messages")
fun bindRecyclerViewMessage(recyclerView: RecyclerView, homeItems: List<Message>?) {
    homeItems?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is ChatListRecyclerAdapter -> submitList(it)
            }
        }
    }
}




//fragment_ranking_list in use
@BindingAdapter("addUsers")
fun bindRankingFragment(recyclerView: RecyclerView, homeItems: List<User>?) {
    homeItems?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is RankingRecyclerAdapter -> submitList(it)
                else -> {
                    Log.i("SOMETHING", "")
                }
            }
        }
    }
}


//fragment_profile in use
@BindingAdapter("addTasksOfMineForProfile")
fun bindProfileFragment(recyclerView: RecyclerView, homeItems: List<Task>?) {
    homeItems?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is ProfileRecyclerAdapter -> submitList(it)
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
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image))
            .into(imgView)
    }
}


/**
 * Uses the Glide library to load an image by URL into an [ImageView],transform(RoundedCorners(300)) for facebook profile image.
 */
@BindingAdapter("imageUrlRound")
fun bindRoundImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().build()
        GlideApp.with(imgView.context)
            .load(imgUri)
            .transform(RoundedCorners(300))
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.icons_36px_profile_normal)
                    .error(R.drawable.icons_36px_profile_normal))
            .into(imgView)
    }
}



@BindingAdapter("proProgressItems")
fun bindProProgressRecyclerView(recyclerView: RecyclerView, homeItems: List<ProposalProgressContent>?) {
    homeItems?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is ProProgressRecyclerAdapter -> submitList(it)
            }
        }
    }
}







/**
 * According to [LoadApiStatus] to decide the visibility of [ProgressBar]
 */
@BindingAdapter("setupApiStatus")
fun bindApiStatus(view: ProgressBar, status: LoadApiStatus?) {
    Log.d("bindApiStatus.status","${status}")
    when (status) {
        LoadApiStatus.LOADING -> view.visibility = View.VISIBLE
        LoadApiStatus.DONE, LoadApiStatus.ERROR -> view.visibility = View.GONE
    }
}

/**
 * According to [message] to decide the visibility of [ProgressBar]
 */
@BindingAdapter("setupApiErrorMessage")
fun bindApiErrorMessage(view: TextView, message: String?) {
    when (message) {
        null, "" -> {
            view.visibility = View.GONE
        }
        else -> {
            view.text = message
            view.visibility = View.VISIBLE
        }
    }
}


//set the proposal's number into button text description
@BindingAdapter("proposalsCount")
fun bindMission(textView: TextView, missionCount:Int?){
    missionCount?.let {
        textView.text = HelperApplication.instance.getString(R.string.button_mission_detail_proposal_total,it) }}