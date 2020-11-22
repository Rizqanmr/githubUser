package com.rizqanmr.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (var avatar: String? ="", var name: String? ="", var username: String? ="", var location: String? ="",
                 var company: String? ="", var repository: Int = 0, var followers: Int = 0, var following: Int = 0
                ) : Parcelable


