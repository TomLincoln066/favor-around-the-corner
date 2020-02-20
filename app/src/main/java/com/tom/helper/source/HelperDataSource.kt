package com.tom.helper.source


// Main entry point for accessing Publisher sources.

interface HelperDataSource {

    suspend fun login(id: String): Result<User>

    suspend fun getTasks(): Result<List<Task>>

    suspend fun getProposals(task: Task): Result<List<Proposal>>

    suspend fun getOnGoingTasks(): Result<List<Task>>

    suspend fun getFinishedTasks(): Result<List<Task>>

    suspend fun editOneProposal(task: Task, proposal: Proposal): Result<Boolean>

    suspend fun editOneProposalToUnaccepted(task: Task, proposal: Proposal): Result<Boolean>

    suspend fun checkUser(): Result<Boolean>

    suspend fun getTasksOfMine(): Result<List<Task>>

    suspend fun getProposalsOfMine(task: Task): Result<List<Proposal>>

    suspend fun getUserCurrent():  Result<User>

    suspend fun getProposalProgressItem(proposal: Proposal):Result<List<ProposalProgressContent>>

    suspend fun editOneProposalProgressItemToFinished(proposal: Proposal,proposalProgressContent: ProposalProgressContent): Result<Boolean>

    suspend fun getProposalsOfStatusEqualToZero(task: Task): Result<List<Proposal>>


//    suspend fun publish(article: Article): Result<Boolean>
//
//    suspend fun delete(article: Article): Result<Boolean>

}