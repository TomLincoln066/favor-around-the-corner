package com.tom.helper.source.local

import android.content.Context
import com.tom.helper.source.HelperDataSource
import com.tom.helper.source.Proposal
import com.tom.helper.source.Result
import com.tom.helper.source.Task
import com.tom.helper.source.User

class HelperLocalDataSource(val context: Context) : HelperDataSource {

    override suspend fun login(id: String): com.tom.helper.source.Result<User> {
        return when (id) {
            "waynechen323" -> com.tom.helper.source.Result.Success((User(
                id,
                "AKA小安老師",
                "wayne@school.appworks.tw"
            )))
            "dlwlrma" -> com.tom.helper.source.Result.Success((User(
                id,
                "IU",
                "dlwlrma@school.appworks.tw"
            )))
            //TODO add your profile here
            else -> com.tom.helper.source.Result.Fail("You have to add $id info in local data source")
        }
    }

    override suspend fun getTasks(): com.tom.helper.source.Result<List<Task>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProposals(): Result<List<Proposal>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override suspend fun publish(article: Article): Result<Boolean> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun delete(article: Article): Result<Boolean> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
}