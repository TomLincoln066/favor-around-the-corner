package com.tom.helper.ext

import androidx.fragment.app.Fragment
import com.tom.helper.HelperApplication
import com.tom.helper.factory.*

import com.tom.helper.source.Proposal
import com.tom.helper.source.ProposalProgressContent
import com.tom.helper.source.Task

//* Extension functions for Fragment.
//*/
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as HelperApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(task: Task): TaskViewModelFactory {
    val repository = (requireContext().applicationContext as HelperApplication).repository
    return TaskViewModelFactory(repository, task)
}


fun Fragment.getVmFactory(proposal: Proposal): ProposalProgressEditViewModelFactory {
    val repository = (requireContext().applicationContext as HelperApplication).repository
    return ProposalProgressEditViewModelFactory(repository,proposal)
}


//fun Fragment.getVmFactory(proposal: Proposal): ProProgressViewModelFactory {
//    val repository = (requireContext().applicationContext as HelperApplication).repository
//    return ProProgressViewModelFactory(repository,proposal)
//}





//fun Fragment.getVmFactory(task: Task): TaskViewModelFactory {
//    val repository = (requireContext().applicationContext as HelperApplication).repository
//    return TaskViewModelFactory(repository, task)
//}




//fun Fragment.getVmFactory(task: Task): ProposalListViewModelFactory {
//    val repository = (requireContext().applicationContext as HelperApplication).repository
//    return ProposalListViewModelFactory(repository, task)
//}
