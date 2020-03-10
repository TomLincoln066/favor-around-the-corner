package com.tom.helper.source.local

import android.content.Context
import androidx.lifecycle.LiveData
import com.tom.helper.source.*

class HelperLocalDataSource(val context: Context) : HelperDataSource {

    override suspend fun login(id: String): com.tom.helper.source.Result<User> {
        return when (id) {
            "waynechen323" -> com.tom.helper.source.Result.Success(
                (User(
                    id,
                    "AKA小安老師",
                    "wayne@school.appworks.tw"
                ))
            )
            "dlwlrma" -> com.tom.helper.source.Result.Success(
                (User(
                    id,
                    "IU",
                    "dlwlrma@school.appworks.tw"
                ))
            )
            //TODO add your profile here
            else -> com.tom.helper.source.Result.Fail("You have to add $id info in local data source")
        }
    }

    override suspend fun getTasks(): com.tom.helper.source.Result<List<Task>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProposals(task: Task): Result<List<Proposal>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getOnGoingTasks(): Result<List<Task>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getFinishedTasks(): Result<List<Task>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun editOneProposal(task: Task, proposal: Proposal): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun editOneProposalToUnaccepted(
        task: Task,
        proposal: Proposal
    ): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun checkUser(): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getTasksOfMine(): Result<List<Task>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProposalsOfMine(task: Task): Result<List<Proposal>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserCurrent(): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProposalProgressItem(proposal: Proposal): Result<List<ProposalProgressContent>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun editOneProposalProgressItemToFinished(
        proposal: Proposal,
        proposalProgressContent: ProposalProgressContent
    ): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProposalsOfStatusEqualToZero(task: Task): Result<List<Proposal>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUsers(): Result<List<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getTasksWithMyProposal(): Result<List<Proposal>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getMessagesFromDB(task: Task): Result<List<Message>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override suspend fun sendMessagesToDB(task: Task): Result<List<Message>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun addTaskProposalOwnerID(task: Task, userID: String): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getProposalsLive(task: Task): LiveData<List<Proposal>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProposalProgressItemLive(proposal: Proposal): LiveData<List<ProposalProgressContent>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProposalsOfStatusEqualToZeroAndAddValueToWinner(proposal: Proposal): Result<List<Proposal>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getMessagesFromDBLive(task: Task): LiveData<List<Message>> {
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