package com.tom.helper.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rank(
    val id: String = "",
    val name: String = "",
//    val email: String = "",
//    val level: Int = -1,
    val intro: String = "",
    val earning: Long = -1
) : Parcelable