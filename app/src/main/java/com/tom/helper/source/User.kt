package com.tom.helper.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val level: Int = -1,
    var earning: Long = -1,
    val image: String? = ""


) : Parcelable


//@Parcelize
//data class User(
//    var id: String = "",
//    var name: String? = "",
//    var email: String? = "",
//    var image: String? =""
//): Parcelable