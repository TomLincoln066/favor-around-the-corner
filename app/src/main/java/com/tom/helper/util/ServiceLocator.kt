package com.tom.helper.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.tom.helper.source.DefaultHelperRepository
import com.tom.helper.source.HelperDataSource
import com.tom.helper.source.HelperRepository
import com.tom.helper.source.local.HelperLocalDataSource
import com.tom.helper.source.remote.HelperRemoteDataSource

//* A Service Locator for the [PublisherRepository].
//*/

object ServiceLocator {

    @Volatile
    var repository: HelperRepository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): HelperRepository {
        synchronized(this) {
            return repository
                ?: repository
                ?: createHelperRepository(context)
        }
    }

    private fun createHelperRepository(context: Context): HelperRepository {
        return DefaultHelperRepository(
            HelperRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): HelperDataSource {
        return HelperLocalDataSource(context)
    }
}