package com.tom.helper.source

// Concrete implementation to load Publisher sources.

class DefaultHelperRepository(private val remoteDataSource: HelperDataSource,
                                 private val localDataSource: HelperDataSource
) : HelperRepository {

    override suspend fun login(id: String): Result<User> {
        return localDataSource.login(id)
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return remoteDataSource.getTasks()
    }

    override suspend fun getProposals(task: Task): Result<List<Proposal>> {
        return remoteDataSource.getProposals(task)
    }

    override suspend fun getOnGoingTasks(): Result<List<Task>> {
        return remoteDataSource.getOnGoingTasks()
    }

    override suspend fun getFinishedTasks(): Result<List<Task>> {
        return remoteDataSource.getFinishedTasks()
    }

    override suspend fun editOneProposal(task: Task, proposal: Proposal): Result<Boolean>{
        return remoteDataSource.editOneProposal(task,proposal)
    }

    override suspend fun editOneProposalToUnaccepted(task: Task, proposal: Proposal): Result<Boolean>{
        return remoteDataSource.editOneProposalToUnaccepted(task,proposal)
    }

    override suspend fun checkUser(): Result<Boolean> {
        return remoteDataSource.checkUser()
    }

    override suspend fun getTasksOfMine(): Result<List<Task>> {
        return remoteDataSource.getTasksOfMine()
    }

    override suspend fun getProposalsOfMine(task: Task): Result<List<Proposal>> {
        return remoteDataSource.getProposalsOfMine(task)
    }

    override suspend fun getUserCurrent(): Result<User> {
        return remoteDataSource.getUserCurrent()
    }

    override suspend fun getProposalProgressItem(proposal: Proposal): Result<List<ProposalProgressContent>> {
        return remoteDataSource.getProposalProgressItem(proposal)
    }

    override suspend fun editOneProposalProgressItemToFinished(proposal: Proposal,proposalProgressContent: ProposalProgressContent): Result<Boolean> {
        return remoteDataSource.editOneProposalProgressItemToFinished(proposal,proposalProgressContent)
    }


    override suspend fun getProposalsOfStatusEqualToZero(task: Task): Result<List<Proposal>> {
        return remoteDataSource.getProposalsOfStatusEqualToZero(task)
    }

    override suspend fun getUsers(): Result<List<User>> {
        return remoteDataSource.getUsers()
    }

    override suspend fun getTasksWithMyProposal(): Result<List<Proposal>> {
        return remoteDataSource.getTasksWithMyProposal()
    }

    override suspend fun getMessagesFromDB(task: Task): Result<List<Message>> {
        return remoteDataSource.getMessagesFromDB(task)
    }

    override suspend fun sendMessagesToDB(task: Task): Result<List<Message>> {
        return remoteDataSource.sendMessagesToDB(task)
    }

//
//    override suspend fun publish(article: Article): Result<Boolean> {
//        return remoteDataSource.publish(article)
//    }
//
//    override suspend fun delete(article: Article): Result<Boolean> {
//        return remoteDataSource.delete(article)
//    }
}