package com.tom.helper.source.remote

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tom.helper.HelperApplication
import com.tom.helper.R
import com.tom.helper.source.*
import com.tom.helper.util.Logger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.firebase.firestore.FieldValue

// Implementation of the Helper source that from firebase.

object HelperRemoteDataSource : HelperDataSource {

    private const val PATH_TASKS = "tasks"

    private const val KEY_CREATED_TIME = "createdTime"

    private const val PATH_PROPOSALS = "proposal"

    private const val PATH_USERS = "users"

    private const val PATH_PROPOSALS_PROGRESSITEMS = "progressItems"

    private const val PATH_USER_EARNING = "earning"

    private const val PATH_MESSAGES = "messages"


    override suspend fun checkUser(): Result<Boolean> = suspendCoroutine { continuation ->
        val users = FirebaseFirestore.getInstance().collection(PATH_USERS)
        val userCurrent = FirebaseAuth.getInstance().currentUser
        val document = users.document(userCurrent!!.uid)

        val photoUrl = userCurrent.photoUrl.toString()

        val user =
            User(userCurrent.uid, userCurrent.displayName!!, userCurrent.email!!, 0, 0L, photoUrl)

        document
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result!!.data == null) {
                        document
                            .set(user).addOnSuccessListener {
                                Log.d("checkUser", "Success setting new user => $user")
                            }
                            .addOnFailureListener {
                                Log.w("checkUser", "Error setting new user.", it)
                            }
                    }

                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {
                        Log.d("checkUser", "Error")
                        Log.w(
                            "",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }


    override suspend fun getUserCurrent(): Result<User> = suspendCoroutine { continuation ->

        val users = FirebaseFirestore.getInstance().collection(PATH_USERS)
        val userCurrent = FirebaseAuth.getInstance().currentUser
        val photoUrl = userCurrent?.photoUrl.toString()

        users
            .document(userCurrent!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.toObject(User::class.java)!!


                    HelperApplication.user = user

                    continuation.resume(Result.Success(user))
                } else {
                    task.exception?.let {

                        Log.w(
                            "",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                }
            }

    }


    override suspend fun login(id: String): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getTasks(): Result<List<Task>> = suspendCoroutine { continuation ->

        val userCurrent = FirebaseAuth.getInstance().currentUser


        FirebaseFirestore.getInstance()
            .collection(PATH_TASKS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)

            .whereEqualTo("status", -1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Task>()

//                    val newList = list.filter { it.userId != userCurrent?.uid }


                    val tasks = task.result!!.toObjects(Task::class.java)

//                    val tasksOfInterest = tasks.filter { it.userId == userCurrent?.uid }

//                    for (document in task.result!!) {
////                        Logger.d(document.id + " => " + document.data)
//
//                        val task1 = document.toObject(Task::class.java)
//                        newList.add(task1)
//                        Log.d("Will", "get data form firebase")
//                    }
//                    continuation.resume(Result.Success(tasksOfInterest))
                    continuation.resume(Result.Success(tasks))
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

        val userCurrent = FirebaseAuth.getInstance().currentUser?.uid

//        Log.d("will","HelperRemoteDataSource.getOnGoingTasks")
//        Log.d("will","HelperApplication.user.id=${HelperApplication.user.id}")

        FirebaseFirestore.getInstance()
            .collection(PATH_TASKS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .whereEqualTo("status", 0)

//            .whereArrayContainsAny("participantsID", listOf(userCurrent.toString()))

//            .whereArrayContainsAny("participantsID", listOf(HelperApplication.user.id))
            .whereArrayContainsAny("participantsID", listOf(userCurrent))


//            .whereArrayContainsAny("participantsID", listOf("Wayne", "IU"))

//            .whereEqualTo("userId", userCurrent?.uid)

//            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)

            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Task>()

                    val tasks = task.result!!.toObjects(Task::class.java)

//                    val tasksOfInterest = tasks.filter { it.userId == userCurrent?.uid }


//                    for (document in task.result!!) {
//
//
//                        val task1 = document.toObject(Task::class.java)
//                        list.add(task1)
//
//
//
//
//
//                    }
//                    continuation.resume(Result.Success(tasksOfInterest))
                    continuation.resume(Result.Success(tasks))

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

        val userCurrent = FirebaseAuth.getInstance().currentUser

        FirebaseFirestore.getInstance()
            .collection(PATH_TASKS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .whereEqualTo("status", 1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

//                    val list = mutableListOf<Task>()

                    val tasks = task.result!!.toObjects(Task::class.java)

//                    val tasksOfInterest = tasks.filter { it.userId == userCurrent?.uid }

//                    for (document in task.result!!) {
////                        Logger.d(document.id + " => " + document.data)
//
//                        val task1 = document.toObject(Task::class.java)
//                        list.add(task1)
//                        Log.d("Will", "get data form firebase")
//                    }

//                    continuation.resume(Result.Success(tasksOfInterest))
                    continuation.resume(Result.Success(tasks))

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


    override suspend fun editOneProposalToUnaccepted(
        task: Task,
        proposal: Proposal
    ): Result<Boolean> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_TASKS).document(task.id).collection(PATH_PROPOSALS)
                .document(proposal.id)
                .update("status", -1)
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


    override suspend fun getTasksOfMine(): Result<List<Task>> = suspendCoroutine { continuation ->

        val userCurrent = FirebaseAuth.getInstance().currentUser

        FirebaseFirestore.getInstance()
            .collection(PATH_TASKS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .whereEqualTo("userId", userCurrent?.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Task>()
                    for (document in task.result!!) {


                        val task1 = document.toObject(Task::class.java)
                        list.add(task1)
                        Log.d("Will", "get data form firebase")
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {


                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }


    override suspend fun getProposalsOfMine(task: Task): Result<List<Proposal>> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_TASKS).document().collection(PATH_PROPOSALS)


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


    override suspend fun getProposalProgressItem(proposal: Proposal): Result<List<ProposalProgressContent>> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_TASKS).document(proposal.taskID).collection(PATH_PROPOSALS)
                .document(proposal.id).collection(
                    PATH_PROPOSALS_PROGRESSITEMS
                )

                .orderBy(KEY_CREATED_TIME, Query.Direction.ASCENDING)

                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<ProposalProgressContent>()
                        for (document in task.result!!) {
//                        Logger.d(document.id + " => " + document.data)

                            val proposalProgressContent =
                                document.toObject(ProposalProgressContent::class.java)
                            list.add(proposalProgressContent)
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


    override suspend fun editOneProposalProgressItemToFinished(
        proposal: Proposal,
        proposalProgressContent: ProposalProgressContent
    ): Result<Boolean> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_TASKS).document(proposal.taskID).collection(PATH_PROPOSALS)
                .document(proposal.id).collection(PATH_PROPOSALS_PROGRESSITEMS)
                .document(proposalProgressContent.id)
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


    // When Task owner finish the task, this function will get the earning of the user who post the accepted proposal, and add the task's price to the earning.
    override suspend fun getProposalsOfStatusEqualToZero(task: Task): Result<List<Proposal>> =
        suspendCoroutine { continuation ->

            Log.d("getProposalsOfStatus", "Success")
            FirebaseFirestore.getInstance()
                .collection(PATH_TASKS).document(task.id).collection(PATH_PROPOSALS)
                .whereEqualTo("status", 0)


                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)

                .get()
                .addOnCompleteListener { taskA ->
                    if (taskA.isSuccessful) {
                        val list = mutableListOf<Proposal>()
                        for (document in taskA.result!!) {
//                        Logger.d(document.id + " => " + document.data)

                            val proposal = document.toObject(Proposal::class.java)
                            Log.d("addOnCompleteListener", "Success")

                            FirebaseFirestore.getInstance()
                                .collection(PATH_USERS).document(proposal.userID).get()
                                .addOnSuccessListener {

                                    val newEarning =
                                        it.data!!["earning"].toString().toLong() + task.price
                                    Log.d("newEarning", "Success")

                                    FirebaseFirestore.getInstance()
                                        .collection(PATH_USERS).document(proposal.userID)
                                        .update("earning", newEarning).addOnSuccessListener {
                                            Log.d("addValue", "Success")
                                        }


                                }


                            list.add(proposal)
                            Log.d("Will", "get proposals form firebase")
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        taskA.exception?.let {

                            //                      Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }

        }


    override suspend fun getUsers(): Result<List<User>> = suspendCoroutine { continuation ->

        FirebaseFirestore.getInstance()
            .collection(PATH_USERS)
            .orderBy(PATH_USER_EARNING, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<User>()
                    for (document in task.result!!) {


                        val task1 = document.toObject(User::class.java)

                        list.add(task1)

                        Log.d("Will", "get ${task1} form firebase")
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {


                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }


    override suspend fun getTasksWithMyProposal(): Result<List<Proposal>> =
        suspendCoroutine { continuation ->


            val userCurrent = FirebaseAuth.getInstance().currentUser

            FirebaseFirestore.getInstance()
                .collectionGroup(PATH_PROPOSALS).whereEqualTo("userId", userCurrent?.uid)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task1 ->
                    if (task1.isSuccessful) {
                        val list = mutableListOf<Proposal>()
                        for (document in task1.result!!) {


                            val proposal = document.toObject(Proposal::class.java)
                            list.add(proposal)
                            Log.d("Will", "get proposals form firebase")
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task1.exception?.let {

                            //                      Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }

        }


    override suspend fun getMessagesFromDB(task: Task): Result<List<Message>> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()

//                .collection(PATH_MESSAGES)

                .collection(PATH_TASKS).document(task.id).collection(PATH_MESSAGES)
                .orderBy(KEY_CREATED_TIME, Query.Direction.ASCENDING)
                .get()

                .addOnCompleteListener { task1 ->
                    if (task1.isSuccessful) {

                        val list = mutableListOf<Message>()

                        for (document in task1.result!!) {


                            val message = document.toObject(Message::class.java)
                            list.add(message)

                            Log.d("Will", "get Messages form FireBase")
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task1.exception?.let {

                            //                      Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }

        }


    override suspend fun sendMessagesToDB(task: Task): Result<List<Message>> =
        suspendCoroutine { continuation ->

            val message = FirebaseFirestore.getInstance().collection("messages")

            val document = message.document()

            val user = FirebaseAuth.getInstance().currentUser!!

            val messageContent = MutableLiveData<String>()

            val taskId = task.id

            val taskOnwerId = task.userId

            val userCurrent = user.uid


            val data = Message(
                document.id,
                System.currentTimeMillis(),
                "",
                messageContent.value!!,
                null,
                userCurrent,
                null,
                taskOnwerId,
                ""
            )


            document.set(data)
                .addOnFailureListener {
                    Log.i("EXCEPTIONX", "exc = ${it.message}")
                }.addOnSuccessListener {

                    Toast.makeText(
                        HelperApplication.context,
                        "新增訊息成功",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("SUCCESS", "SU")
                }

        }


    override suspend fun addTaskProposalOwnerID(task: Task, userID: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(PATH_TASKS).document(task.id)

                .update("participantsID", FieldValue.arrayUnion(userID))
                .addOnCompleteListener { addId ->
                    if (addId.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        addId.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                        }
                        continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }


    //snapshot proposal list
    override fun getProposalsLive(task: Task): LiveData<List<Proposal>> {
        val liveData = MutableLiveData<List<Proposal>>()
        FirebaseFirestore.getInstance()
            .collection(PATH_TASKS)
            .document(task.id)
            .collection(PATH_PROPOSALS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                exception?.let {
                }

                val list = mutableListOf<Proposal>()
                for (document in snapshot!!) {

                    val proposal = document.toObject(Proposal::class.java)
                    list.add(proposal)
                    Log.d("Will", "getProposalsLive")
                }

                liveData.value = list
            }
        return liveData
    }

    override fun getProposalProgressItemLive(proposal: Proposal): LiveData<List<ProposalProgressContent>> {
        val liveData = MutableLiveData<List<ProposalProgressContent>>()
        FirebaseFirestore.getInstance()
            .collection(PATH_TASKS)
            .document(proposal.taskID)
            .collection(PATH_PROPOSALS)
            .document(proposal.id)
            .collection(PATH_PROPOSALS_PROGRESSITEMS)
            .orderBy(KEY_CREATED_TIME, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                exception?.let {
                }

                val list = mutableListOf<ProposalProgressContent>()
                for (document in snapshot!!) {

                    val proposalProgressContent =
                        document.toObject(ProposalProgressContent::class.java)
                    list.add(proposalProgressContent)
                    Log.d("Will", "getProposalProgressItemLive")
                }

                liveData.value = list
            }
        return liveData

    }


    override suspend fun getProposalsOfStatusEqualToZeroAndAddValueToWinner(proposal: Proposal): Result<List<Proposal>> =
        suspendCoroutine { continuation ->

            Log.d("getProposalsOfStatusEqualToZeroAndAddValueToWinner", "Success")
            FirebaseFirestore.getInstance()
                .collection(PATH_TASKS).document(proposal.taskID).collection(PATH_PROPOSALS)
                .whereEqualTo("status", 0)
                .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { taskA ->
                    if (taskA.isSuccessful) {
                        val list = mutableListOf<Proposal>()
                        for (document in taskA.result!!) {

                            val proposal = document.toObject(Proposal::class.java)

                            Log.d("addOnCompleteListener", "Success")

                            FirebaseFirestore.getInstance()
                                .collection(PATH_USERS).document(proposal.userID).get()
                                .addOnSuccessListener {

                                    val newEarning =
                                        it.data!!["earning"].toString().toLong() + proposal.taskPrice
                                    Log.d("newEarning", "Success")

                                    FirebaseFirestore.getInstance()
                                        .collection(PATH_USERS).document(proposal.userID)
                                        .update("earning", newEarning).addOnSuccessListener {
                                            Log.d("addValue", "Success")
                                        }


                                }


                            list.add(proposal)
                            Log.d("Will", "get proposals form firebase")
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        taskA.exception?.let {

                            //                      Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(HelperApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }

        }




    //snapshot Message list
    override fun getMessagesFromDBLive(task: Task): LiveData<List<Message>> {
        val liveData = MutableLiveData<List<Message>>()
        FirebaseFirestore.getInstance()
            .collection(PATH_TASKS).document(task.id).collection(PATH_MESSAGES)
            .orderBy(KEY_CREATED_TIME, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                exception?.let {
                }

                val list = mutableListOf<Message>()
                for (document in snapshot!!) {

                    val message = document.toObject(Message::class.java)
                    list.add(message)
                    Log.d("Will", "getMessagesFromDBLive")
                }

                liveData.value = list
            }
        return liveData
    }


}

