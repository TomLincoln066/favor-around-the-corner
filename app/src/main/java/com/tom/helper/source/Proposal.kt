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
    val userID:String = ""

) : Parcelable