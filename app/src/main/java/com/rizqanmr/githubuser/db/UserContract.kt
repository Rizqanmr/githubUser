package com.rizqanmr.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object UserContract {
    const val AUTHORITY = "com.rizqanmr.githubuser"
    const val SCHEME = "content"

    class FavColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val FAVORITE = "isFav"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}