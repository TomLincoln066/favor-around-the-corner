package com.tom.helper.ext

import android.app.Activity
import com.tom.helper.HelperApplication
import com.tom.helper.factory.ViewModelFactory

fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as HelperApplication).repository
    return ViewModelFactory(repository)
}