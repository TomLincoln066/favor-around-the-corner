package com.tom.helper.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Proposal(
    var id: String = "",
    var createdTime: Long = -1,
    var title: String = "",
    var content: String = "",
    val senderName: String = "",
    val accepted: Boolean = false,
    val userID:String = "",
    val user: User? = null,
    val userIDTakeTheCase:User? = null,
    val status : Int = -1,
    val taskID: String = "",
    val taskOnwerID: String = "",
    val taskPrice:Long =  -1,
    val firstStep:String = "",
    val secondStep:String = "",
    val thirdStep:String = "",
    val fourthStep:String = ""

) : Parcelable

