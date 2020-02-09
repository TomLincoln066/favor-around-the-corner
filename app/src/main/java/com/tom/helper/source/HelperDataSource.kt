package com.tom.helper.source


// Main entry point for accessing Publisher sources.

interface HelperDataSource {

    suspend fun login(id: String): Result<User>

    suspend fun getTasks(): Result<List<Task>>

    suspend fun getProposals(task: Task): Result<List<Proposal>>

    suspend fun getOnGoingTasks(): Result<List<Task>>

    suspend fun getFinishedTasks(): Result<List<Task>>

//    suspend fun publish(article: Article): Result<Boolean>
//
//    suspend fun delete(article: Article): Result<Boolean>

}