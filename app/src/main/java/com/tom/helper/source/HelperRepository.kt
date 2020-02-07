package com.tom.helper.source


//Interface to the Helper layers.

interface HelperRepository {

    suspend fun login(id: String): Result<User>

    suspend fun getTasks(): Result<List<Task>>

    suspend fun getProposals(task: Task): Result<List<Proposal>>

//    suspend fun publish(article: Article): Result<Boolean>
//
//    suspend fun delete(article: Article): Result<Boolean>

}