package com.tom.helper.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    var id: String = "",
    var createdTime: Long = -1,
    var title: String = "",
    var content: String = "",
    var userSender:User? = null,
    var userSenderId:String = "",
    var userReceiver:User? = null,
    var userReceiverId:String = "",
    val image:String = "",
    val taskId:String = ""

) : Parcelable