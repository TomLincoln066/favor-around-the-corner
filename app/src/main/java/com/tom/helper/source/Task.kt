package com.tom.helper.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    var id: String = "",
    var price: Long = -1,
    var createdTime: Long = -1,
    var title: String = "",
    var content: String = "",
    var user:User? = null,
    val taskCreator : String = "",
    var status: Int = -1,
    val proposal: List<Proposal> = listOf()



//    var subContent : List<String> = listOf()

) : Parcelable

//@Parcelize
//data class Task(
//    var id: String = "",
//    var price: Long = -1,
//    var createdTime: Long = -1,
//    var title: String = "",
//    var content: String = "",
//    val userID:String = "",
//    val taskCreator : String = "",
//    val status: Int = -1,
//    var subContent : List<String> = listOf()
//
//) : Parcelable