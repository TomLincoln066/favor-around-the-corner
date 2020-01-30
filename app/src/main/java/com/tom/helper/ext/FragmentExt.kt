package com.tom.helper.ext

import androidx.fragment.app.Fragment
import com.tom.helper.HelperApplication
import com.tom.helper.factory.ViewModelFactory

//* Extension functions for Fragment.
//*/
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as HelperApplication).repository
    return ViewModelFactory(repository)
}