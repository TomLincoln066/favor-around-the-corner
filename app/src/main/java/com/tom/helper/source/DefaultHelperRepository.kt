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
//
//    override suspend fun publish(article: Article): Result<Boolean> {
//        return remoteDataSource.publish(article)
//    }
//
//    override suspend fun delete(article: Article): Result<Boolean> {
//        return remoteDataSource.delete(article)
//    }
}