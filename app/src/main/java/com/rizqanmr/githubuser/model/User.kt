package com.rizqanmr.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (var avatar: Int, var name: String, var username: String, var location: String,
                 var company: String, var repository: String, var followers: String, var following: String
                ) : Parcelable


