package com.tom.helper.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tom.helper.homefragment.HomeViewModel
import com.tom.helper.jobdetailfragment.JobDetailViewModel
import com.tom.helper.postrequest.PostRequestViewModel
import com.tom.helper.proposaleditfragment.ProposalEditViewModel
import com.tom.helper.proposallistfragment.ProposalListViewModel
import com.tom.helper.rankinglist.RankingListViewModel
import com.tom.helper.source.HelperRepository

//* Factory for all ViewModels.
//*/

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: HelperRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
//                isAssignableFrom(MainViewModel::class.java) ->
//                    MainViewModel(repository)

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(repository)

                isAssignableFrom(PostRequestViewModel::class.java) ->
                    PostRequestViewModel(repository)

                isAssignableFrom(RankingListViewModel::class.java) ->
                    RankingListViewModel(repository)

                isAssignableFrom(ProposalListViewModel::class.java) ->
                    ProposalListViewModel(repository)

                isAssignableFrom(JobDetailViewModel::class.java) ->
                    JobDetailViewModel(repository)

                isAssignableFrom(ProposalEditViewModel::class.java) ->
                    ProposalEditViewModel(repository)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}