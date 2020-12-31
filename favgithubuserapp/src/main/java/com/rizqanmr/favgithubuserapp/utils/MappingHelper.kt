package com.rizqanmr.favgithubuserapp.utils

import android.database.Cursor
import com.rizqanmr.favgithubuserapp.db.UserContract
import com.rizqanmr.favgithubuserapp.model.User
import java.util.*

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User> {
        val favList = ArrayList<User>()

        notesCursor?.apply {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(UserContract.FavColumns.NAME))
                val username = getString(getColumnIndexOrThrow(UserContract.FavColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(UserContract.FavColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(UserContract.FavColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(UserContract.FavColumns.LOCATION))
                val repository = getInt(getColumnIndexOrThrow(UserContract.FavColumns.REPOSITORY))
                val followers = getInt(getColumnIndexOrThrow(UserContract.FavColumns.FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(UserContract.FavColumns.FOLLOWING))
                val favorite =
                    getString(getColumnIndexOrThrow(UserContract.FavColumns.FAVORITE))
                favList.add(
                    User(
                        name,
                        username,
                        avatar,
                        company,
                        location,
                        repository,
                        followers,
                        following,
                        favorite
                    )
                )
            }
        }
        return favList
    }
}