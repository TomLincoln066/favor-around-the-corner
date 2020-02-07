package com.tom.helper.ext

import androidx.fragment.app.Fragment
import com.tom.helper.HelperApplication
import com.tom.helper.factory.ProposalEditViewModelFactory
import com.tom.helper.factory.ViewModelFactory
import com.tom.helper.source.Task

//* Extension functions for Fragment.
//*/
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as HelperApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(task: Task): ProposalEditViewModelFactory {
    val repository = (requireContext().applicationContext as HelperApplication).repository
    return ProposalEditViewModelFactory(repository, task)
}