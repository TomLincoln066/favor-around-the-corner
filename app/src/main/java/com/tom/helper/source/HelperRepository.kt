package com.tom.helper.source


//Interface to the Helper layers.

interface HelperRepository {

    suspend fun checkUser(): Result<Boolean>

    suspend fun login(id: String): Result<User>

    suspend fun getTasks(): Result<List<Task>>

    suspend fun getOnGoingTasks(): Result<List<Task>>

    suspend fun getFinishedTasks(): Result<List<Task>>

    suspend fun getProposals(task: Task): Result<List<Proposal>>

    suspend fun editOneProposal(task: Task, proposal: Proposal): Result<Boolean>

    suspend fun editOneProposalToUnaccepted(task: Task, proposal: Proposal): Result<Boolean>



//    suspend fun publish(article: Article): Result<Boolean>
//
//    suspend fun delete(article: Article): Result<Boolean>

}