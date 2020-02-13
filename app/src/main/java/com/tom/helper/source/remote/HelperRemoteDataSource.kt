package com.tom.helper.source.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tom.helper.HelperApplication
import com.tom.helper.R
import com.tom.helper.source.*
import com.tom.helper.util.Logger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Implementation of the Helper source that from firebase.

object HelperRemoteDataSource : HelperDataSource {

    private const val PATH_TASKS = "tasks"
    private const val KEY_CREATED_TIME = "createdTime"

    private const val PATH_PROPOSALS = "proposal"


    override suspend fun login(id: String): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getTasks(): Result<List<Task>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_TASKS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .whereEqualTo("status", -1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Task>()
                    for (document in task.result!!) {
//                        Logger.d(document.id + " => " + document.data)

                        val task1 = document.toObject(Task::class.java)
                        list.add(task1)
                        Log.d("Will", "get data form firebase")
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        //                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                    }
                    continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun getProposals(task: Task): Result<List<Proposal>> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_TASKS).document(task.id).collection(PATH_PROPOSALS)
//            .collection(PATH_PROPOSALS)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
//                .whereEqualTo("status", 0)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Proposal>()
                        for (document in task.result!!) {
//                        Logger.d(document.id + " => " + document.data)

                            val proposal = document.toObject(Proposal::class.java)
                            list.add(proposal)
                            Log.d("Will", "get proposals form firebase")
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {

                            //                      Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }

        }


    override suspend fun getOnGoingTasks(): Result<List<Task>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_TASKS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .whereEqualTo("status", 0)
//            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)

            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Task>()
                    for (document in task.result!!) {


                        val task1 = document.toObject(Task::class.java)
                        list.add(task1)

                    }
                    continuation.resume(Result.Success(list))

                } else {
                    task.exception?.let {

                        Log.w("Will", "task.exception=${it.message}")
                        //                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    Log.w("Will", "under task.exception")
                    continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }


    override suspend fun getFinishedTasks(): Result<List<Task>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_TASKS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .whereEqualTo("status", 1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Task>()
                    for (document in task.result!!) {
//                        Logger.d(document.id + " => " + document.data)

                        val task1 = document.toObject(Task::class.java)
                        list.add(task1)
                        Log.d("Will", "get data form firebase")
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        //                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun editOneProposal(task: Task, proposal: Proposal): Result<Boolean> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_TASKS).document(task.id).collection(PATH_PROPOSALS)
                .document(proposal.id)
                .update("status", 0)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))

                    } else {
                        task.exception?.let {

                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }

        }




}


//    override suspend fun publish(article: Article): Result<Boolean> = suspendCoroutine { continuation ->
//        val articles = FirebaseFirestore.getInstance().collection(PATH_ARTICLES)
//        val document = articles.document()
//
//        article.id = document.id
//        article.createdTime = Calendar.getInstance().timeInMillis
//
//        document
//            .set(article)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Logger.i("Publish: $article")
//
//                    continuation.resume(Result.Success(true))
//                } else {
//                    task.exception?.let {
//
//                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
//                        continuation.resume(Result.Error(it))
//                    }
//                    continuation.resume(Result.Fail(PublisherApplication.instance.getString(R.string.you_know_nothing)))
//                }
//            }
//    }
//
//    override suspend fun delete(article: Article): Result<Boolean> = suspendCoroutine { continuation ->
//
//        when {
//            article.author?.id == "waynechen323"
//                    && article.tag.toLowerCase(Locale.TAIWAN) != "test"
//                    && article.tag.trim().isNotEmpty() -> {
//
//                continuation.resume(Result.Fail("You know nothing!! ${article.author?.name}"))
//            }
//            else -> {
//                FirebaseFirestore.getInstance()
//                    .collection(PATH_ARTICLES)
//                    .document(article.id)
//                    .delete()
//                    .addOnSuccessListener {
//                        Logger.i("Delete: $article")
//
//                        continuation.resume(Result.Success(true))
//                    }.addOnFailureListener {
//                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
//                        continuation.resume(Result.Error(it))
//                    }
//            }
//        }
//
//    }