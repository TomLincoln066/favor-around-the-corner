package com.tom.helper.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProposalProgressContent(

    var content: String = "",
    val status: Int = -1,
    val finished: Boolean = false,
    var id: String = "",
    var createdTime: Long = -1,
    val userID: String = "",
    val user: User? = null,
    val userIDMakeTheItem: User? = null,
    val userIDTakeTheCase: User? = null,
    val proposalID: String = "",
    val image:String = ""

) : Parcelable