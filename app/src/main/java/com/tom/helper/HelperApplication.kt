package com.tom.helper

import android.app.Application
import android.content.Context
import com.tom.helper.source.HelperRepository
import com.tom.helper.util.ServiceLocator
import kotlin.properties.Delegates

// An application that lazily provides a repository. Note that this Service Locator pattern is
// used to simplify the sample. Consider a Dependency Injection framework.
//*/

class HelperApplication : Application() {

    // Depends on the flavor,
    val repository: HelperRepository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var instance: HelperApplication by Delegates.notNull()
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context =applicationContext
    }
}