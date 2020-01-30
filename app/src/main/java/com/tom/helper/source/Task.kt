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
    val user: User? = null,
    val status: Int = -1
) : Parcelable