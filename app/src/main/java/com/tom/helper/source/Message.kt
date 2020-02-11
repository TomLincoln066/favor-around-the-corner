package com.tom.helper.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    var id: String = "",
    var createdTime: Long = -1,
    var title: String = "",
    var content: String = "",
    var user:User? = null,
    val image:String = ""

) : Parcelable